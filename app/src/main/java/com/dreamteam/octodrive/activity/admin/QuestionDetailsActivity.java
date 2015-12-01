package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.Question;

import java.util.List;

public class QuestionDetailsActivity extends AppCompatActivity {

    private Question _question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        Intent intent = getIntent();
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

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
