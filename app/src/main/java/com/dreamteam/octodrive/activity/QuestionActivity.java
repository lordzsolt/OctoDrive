package com.dreamteam.octodrive.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.parse.ParseException;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    private GetQuestionsTask mQuestionsTask = null;
    private LoadingView mLoadingView;

    private String userId;
    private boolean isPractice;

    private RelativeLayout rlQuestion;
    private ImageView ivQuestion;
    private TextView tvQuestion;
    private CheckBox chkAns1, chkAns2, chkAns3;
    private Button btnPrev, btnNext, btnFinish;

    private int current;
    private List<Question> questions;
    private HashMap<String, List<Boolean>> answers;

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
        chkAns2 = (CheckBox)findViewById(R.id.checkBox_answer2);
        chkAns3 = (CheckBox)findViewById(R.id.checkBox_answer3);

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
                setQuestion(current + 1);
            }
        });

        btnFinish = (Button)findViewById(R.id.button_finish);

        mLoadingView = new LoadingView(this, getString(R.string.dialog_preparing));
        mLoadingView.showProgress(true);
        mQuestionsTask = new GetQuestionsTask();
        mQuestionsTask.execute((Void) null);
    }

    private void setQuestion(int index) {
        if (index >= questions.size()) {
            index = 0;
        }
        else if (index < 0) {
            index = questions.size() - 1;
        }

        current = index;
        Question q = questions.get(index);

        //ivQuestion.set?
        tvQuestion.setText(q.message());

        List<String> ans = q.answers();
        chkAns1.setText(ans.get(0));
        chkAns2.setText(ans.get(1));
        chkAns3.setText(ans.get(2));

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
    }

    public class GetQuestionsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                questions = Question.getPredefinedNumberOfQuestions(Locale.getDefault().getLanguage());

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
        }
    }

}
