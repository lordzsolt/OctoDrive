package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionDetailsActivity extends AppCompatActivity {

    private boolean _isNewQuestion;
    private Question _question;

    private LoadingView mLoadingView;
    private UpdateTask mUpdateUserTask;
    private ImageView ivQuestion;
    private DownloadImageTask imgDlTask = null;

    private ParseFile parseImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        Intent intent = getIntent();
        _isNewQuestion = intent.getBooleanExtra(KeyConstants.kKEY_CONSTANT_ADMIN_NEW_QUESTION, true);

        if (_isNewQuestion) {
            _question = new Question();
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

        ivQuestion = (ImageView)findViewById(R.id.imageView_edit_question);

        String imgUrl = _question.imageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            if (imgDlTask != null) {
                imgDlTask.cancel(true);
                imgDlTask = null;
            }

            imgDlTask = new DownloadImageTask(ivQuestion);
            imgDlTask.execute(imgUrl);
        }

        Button imageBtn = (Button) findViewById(R.id.imageButton);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.question_edit_image)), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case 100:
                try {
                    mLoadingView = new LoadingView(this, getString(R.string.dialog_uploading));
                    mLoadingView.showProgress(true);

                    InputStream imageStream = getContentResolver().openInputStream(imageReturnedIntent.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] image = stream.toByteArray();

                    final ParseFile file = new ParseFile(_question.objectId() + ".jpg", image);
                    file.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            parseImage = file;

                            String imgUrl = file.getUrl();
                            if (imgUrl != null && !imgUrl.isEmpty()) {
                                if (imgDlTask != null) {
                                    imgDlTask.cancel(true);
                                    imgDlTask = null;
                                }

                                imgDlTask = new DownloadImageTask(ivQuestion);
                                imgDlTask.execute(imgUrl);
                            }

                            mLoadingView.showProgress(false);
                        }
                    });
                }
                catch (Exception ex) {
                    Log.d("img", ex.toString());
                    mLoadingView.showProgress(false);
                }
                break;
        }
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

        if (parseImage != null) {
            newQuestion.setImage(parseImage);
        }

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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            bmImage.setVisibility(View.INVISIBLE);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("imgdl", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
            imgDlTask = null;
        }
    }

}
