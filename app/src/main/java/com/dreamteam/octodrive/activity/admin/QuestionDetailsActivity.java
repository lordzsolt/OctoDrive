package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionDetailsActivity extends AppCompatActivity {

    private boolean _isNewQuestion;
    private Question _question;

    private LoadingView mLoadingView;
    private UpdateTask mUpdateUserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        Intent intent = getIntent();
        _isNewQuestion = intent.getBooleanExtra(KeyConstants.kKEY_CONSTANT_ADMIN_NEW_QUESTION, true);

        if (_isNewQuestion) {
            return;
        }

        _question = intent.getExtras().getParcelable(KeyConstants.kKEY_CONSTANT_ADMIN_EDIT_OBJECT);

        EditText messageEdit = (EditText)findViewById(R.id.question_edit_message);
        messageEdit.setText(_question.message());

        List<String> answers = _question.answers();
        EditText answer1Edit = (EditText)findViewById(R.id.question_edit_answer1);
        answer1Edit.setText(answers.get(0));

        EditText answer2Edit = (EditText)findViewById(R.id.question_edit_answer2);
        answer2Edit.setText(answers.get(1));

        EditText answer3Edit = (EditText)findViewById(R.id.question_edit_answer3);
        answer3Edit.setText(answers.get(2));

        List<Boolean> correctAnswers = _question.correctAnswers();
        CheckBox correct1 = (CheckBox)findViewById(R.id.checkBox_correct_answer1);
        correct1.setChecked(correctAnswers.get(0));

        CheckBox correct2 = (CheckBox)findViewById(R.id.checkBox_correct_answer2);
        correct2.setChecked(correctAnswers.get(1));

        CheckBox correct3 = (CheckBox)findViewById(R.id.checkBox_correct_answer3);
        correct3.setChecked(correctAnswers.get(2));

        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.activeCheckBox);
        checkBoxActive.setChecked(_question.active());

        Spinner languageSpinner = (Spinner)findViewById(R.id.language_spinner);
        Resources res = this.getResources();
        String[] languages = res.getStringArray(R.array.languages);
        int index = Arrays.asList(languages).indexOf(_question.language());
        languageSpinner.setSelection(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                updateQuestion();
                break;

            default:
                break;
        }

        return true;
    }

    void updateQuestion() {
        Question newQuestion = Question.newQuestionWithObjectId(_question.objectId());

        EditText messageEdit = (EditText)findViewById(R.id.question_edit_message);
        String newMessage = messageEdit.getText().toString();
        newQuestion.setMessage(newMessage);

        EditText answer1Edit = (EditText)findViewById(R.id.question_edit_answer1);
        String newAnswer1 = answer1Edit.getText().toString();
        EditText answer2Edit = (EditText)findViewById(R.id.question_edit_answer2);
        String newAnswer2 = answer2Edit.getText().toString();
        EditText answer3Edit = (EditText)findViewById(R.id.question_edit_answer3);
        String newAnswer3 = answer3Edit.getText().toString();
        newQuestion.setAnswers(Arrays.asList(newAnswer1, newAnswer2, newAnswer3));

        CheckBox correct1Checkbox = (CheckBox)findViewById(R.id.checkBox_correct_answer1);
        boolean correct1 = correct1Checkbox.isChecked();
        CheckBox correct2Checkbox = (CheckBox)findViewById(R.id.checkBox_correct_answer2);
        boolean correct2 = correct2Checkbox.isChecked();
        CheckBox correct3Checkbox = (CheckBox)findViewById(R.id.checkBox_correct_answer3);
        boolean correct3 = correct3Checkbox.isChecked();
        newQuestion.setCorrectAnswers(Arrays.asList(correct1, correct2, correct3));

        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.activeCheckBox);
        boolean newState = checkBoxActive.isChecked();
        newQuestion.setActive(newState);

        Spinner languageSpinner = (Spinner)findViewById(R.id.language_spinner);
        String language = languageSpinner.getSelectedItem().toString();
        newQuestion.setLanguage(language);

        mLoadingView = new LoadingView(this, getString(R.string.dialog_loading));
        mLoadingView.showProgress(true);

        mUpdateUserTask = new UpdateTask();
        mUpdateUserTask._parseObject = newQuestion.parseObject();
        mUpdateUserTask.execute();
    }


    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        ParseObject _parseObject;
        UpdateTask() {}

        @Override
        protected Void doInBackground(Void... params) {
            try {
                _parseObject.save();
            }
            catch (ParseException e) {
                //TODO: Do something
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void integer) {
            mLoadingView.showProgress(false);
            QuestionDetailsActivity.this.finish();
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }

}
