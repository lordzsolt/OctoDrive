package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class UserDetailsActivity extends AppCompatActivity {

    private User _user;

    private LoadingView mLoadingView;
    private UpdateUserTask mUpdateUserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Intent intent = getIntent();
        _user = intent.getExtras().getParcelable(KeyConstants.kKEY_CONSTANT_ADMIN_EDIT_OBJECT);

        TextView messageEdit = (TextView)findViewById(R.id.textView_user_name);
        messageEdit.setText(_user.name());

        TextView answer1Edit = (TextView)findViewById(R.id.textView_user_email);
        answer1Edit.setText(_user.email());

        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkbox_admin);
        checkBoxActive.setChecked(_user.isAdmin());
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
                updateUser();
                break;

            default:
                break;
        }

        return true;
    }

    void updateUser() {
        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkbox_admin);
        boolean newState = checkBoxActive.isChecked();
        if (newState != _user.isAdmin()) {
            mLoadingView = new LoadingView(this, getString(R.string.dialog_loading));
            mLoadingView.showProgress(true);
            ParseObject user = ParseUser.createWithoutData(WebserviceConstants.kPARSE_OBJECT_USER, _user.objectId());
            user.put(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN, newState);

            mUpdateUserTask = new UpdateUserTask();
            mUpdateUserTask._parseObject = user;
            mUpdateUserTask.execute();
        }
    }


    private class UpdateUserTask extends AsyncTask<Void, Void, Void> {
        ParseObject _parseObject;
        UpdateUserTask() {}

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
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }
}
