package com.dreamteam.octodrive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.activity.admin.AdminActivity;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.Result;
import com.dreamteam.octodrive.model.Settings;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.parse.ParseException;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    private GetQuestionsTask mQuestionsTask = null;
    private FinishTestTask mFinishTask = null;
    private LoadingView mLoadingView;

    private String userId;
    private boolean isPractice, isEvaluating;

    private RelativeLayout rlQuestion;
    private ImageView ivQuestion;
    private TextView tvQuestion;
    private CheckBox chkAns1, chkAns2, chkAns3;
    private Button btnPrev, btnNext, btnFinish;

    private int current;
    private List<Question> questions;
    private HashMap<String, List<Boolean>> answers;

    private boolean processEvents = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("userId")) {
            userId = bundle.getString("userId");
            isPractice = bundle.getBoolean("isPractice", true);
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(QuestionActivity.this, R.string.error_invalid_intent, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuestionActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setTitle(isPractice ? R.string.button_practice : R.string.button_exam);

        rlQuestion = (RelativeLayout)findViewById(R.id.relativelayout_question);
        rlQuestion.setVisibility(View.INVISIBLE);

        ivQuestion = (ImageView)findViewById(R.id.imageView_question);
        tvQuestion = (TextView)findViewById(R.id.textView_question);

        chkAns1 = (CheckBox)findViewById(R.id.checkBox_answer1);
        chkAns1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAnswer(0, isChecked);
            }
        });

        chkAns2 = (CheckBox)findViewById(R.id.checkBox_answer2);
        chkAns2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAnswer(1, isChecked);
            }
        });

        chkAns3 = (CheckBox)findViewById(R.id.checkBox_answer3);
        chkAns3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAnswer(2, isChecked);
            }
        });

        btnPrev = (Button)findViewById(R.id.button_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuestion(current - 1);
            }
        });

        btnNext = (Button)findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPractice) {
                    if (isEvaluating || (!chkAns1.isChecked() && !chkAns2.isChecked() && !chkAns3.isChecked())) {
                        setQuestion(current + 1);
                    }
                    else {
                        checkAnswer();
                    }
                }
                else {
                    setQuestion(current + 1);
                }
            }
        });

        btnFinish = (Button)findViewById(R.id.button_finish);
        btnFinish.setEnabled(isPractice);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTest();
            }
        });

        mLoadingView = new LoadingView(this, getString(R.string.dialog_preparing));
        mLoadingView.showProgress(true);
        mQuestionsTask = new GetQuestionsTask();
        mQuestionsTask.execute((Void) null);
    }

    private void checkAnswer() {
        if (!isPractice) {
            return;
        }

        isEvaluating = true;

        Question q = questions.get(current);
        List<Boolean> cor = q.correctAnswers();

        chkAns1.setTextColor(cor.get(0) ? Color.GREEN : Color.RED);
        chkAns2.setTextColor(cor.get(1) ? Color.GREEN : Color.RED);
        chkAns3.setTextColor(cor.get(2) ? Color.GREEN : Color.RED);

        chkAns1.setEnabled(false);
        chkAns2.setEnabled(false);
        chkAns3.setEnabled(false);
    }

    private void setQuestion(int index) {
        if (index >= questions.size()) {
            index = 0;
        }
        else if (index < 0) {
            index = questions.size() - 1;
        }

        if (isPractice) {
            isEvaluating = false;

            chkAns1.setTextColor(Color.BLACK);
            chkAns2.setTextColor(Color.BLACK);
            chkAns3.setTextColor(Color.BLACK);

            chkAns1.setEnabled(true);
            chkAns2.setEnabled(true);
            chkAns3.setEnabled(true);
        }

        current = index;
        Question q = questions.get(index);

        //ivQuestion.set?
        tvQuestion.setText(q.message());

        List<String> ans = q.answers();
        chkAns1.setText(ans.get(0));
        chkAns2.setText(ans.get(1));
        chkAns3.setText(ans.get(2));

        processEvents = false;

        if (answers.containsKey(q.objectId())) {
            List<Boolean> chk = answers.get(q.objectId());
            chkAns1.setChecked(chk.get(0));
            chkAns2.setChecked(chk.get(1));
            chkAns3.setChecked(chk.get(2));
        }
        else {
            chkAns1.setChecked(false);
            chkAns2.setChecked(false);
            chkAns3.setChecked(false);
        }

        processEvents = true;
    }

    private void setAnswer(int index, boolean value) {
        if (!processEvents) {
            return;
        }

        Question q = questions.get(current);

        if (answers.containsKey(q.objectId())) {
            List<Boolean> chk = answers.get(q.objectId());
            chk.set(index, value);

            boolean any = false;
            for (int i = 0; i < 3; i++) {
                if (chk.get(i)) {
                    any = true;
                    break;
                }
            }

            if (any) {
                answers.put(q.objectId(), chk);
            }
            else {
                answers.remove(q.objectId());
            }
        }
        else {
            List<Boolean> chk = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                chk.add(i == index && value);
            }

            answers.put(q.objectId(), chk);
        }

        if (!isPractice) {
            btnFinish.setEnabled(answers.size() == questions.size());
        }
    }

    private void finishTest() {
        if (isPractice) {
            QuestionActivity.this.finish();
            return;
        }

        if (questions.size() != answers.size()) {
            return;
        }

        mLoadingView = new LoadingView(this, getString(R.string.dialog_processing));
        mLoadingView.showProgress(true);
        mFinishTask = new FinishTestTask();
        mFinishTask.execute((Void) null);
    }

    public class GetQuestionsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (isPractice) {
                    questions = Question.getQuestions(100, Locale.getDefault().getLanguage(), true);
                }
                else {
                    questions = Question.getPredefinedNumberOfQuestions(Locale.getDefault().getLanguage());
                }

                if (questions.size() < 1) {
                    return false;
                }
            }
            catch (ParseException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                answers = new HashMap<>();
                setQuestion(0);
                rlQuestion.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(QuestionActivity.this, R.string.error_fetch_question, Toast.LENGTH_SHORT).show();
                QuestionActivity.this.finish();
            }

            mQuestionsTask = null;
            mLoadingView.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mQuestionsTask = null;
            mLoadingView.showProgress(false);
            QuestionActivity.this.finish();
        }
    }

    public class FinishTestTask extends AsyncTask<Void, Void, Boolean> {

        private boolean passed;
        private int correct;

        @Override
        protected Boolean doInBackground(Void... params) {
            correct = 0;

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                List<Boolean> cor = q.correctAnswers();
                List<Boolean> ans = answers.get(q.objectId());

                boolean all = true;

                for (int j = 0; j < 3; j++) {
                    if (cor.get(j) != ans.get(j)) {
                        all = false;
                    }
                }

                if (all) {
                    correct++;
                }
            }

            int min = Settings.minimumPass();

            if (min <= 0) {
                return false;
            }

            passed = correct >= min;

            Result res = new Result(userId);
            res.setLive(true);
            res.setScore(correct);

            try {
                res.save();
            }
            catch (Exception ex) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(QuestionActivity.this, FinishActivity.class);
                intent.putExtra("passed", passed);
                intent.putExtra("correct", correct);
                intent.putExtra("total", questions.size());
                startActivity(intent);
                QuestionActivity.this.finish();
            }
            else {
                Toast.makeText(QuestionActivity.this, R.string.error_db_comm, Toast.LENGTH_SHORT).show();
            }

            mQuestionsTask = null;
            mLoadingView.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mQuestionsTask = null;
            mLoadingView.showProgress(false);
        }
    }

}
