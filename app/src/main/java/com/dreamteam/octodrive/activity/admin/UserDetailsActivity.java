package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.Result;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.utilities.LoadingView;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    private User _user;

    private LoadingView mLoadingView;

    private ListView mListView;

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

        mLoadingView = new LoadingView(this, getString(R.string.dialog_loading));
        mLoadingView.showProgress(true);

        mListView = (ListView)findViewById(R.id.results_list_view);

        GetResultsTask mGetResultsTask = new GetResultsTask();
        mGetResultsTask.objectId = _user.objectId();
        mGetResultsTask.execute();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_question_details, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_save:
//                updateUser();
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }

    void updateUser() {
        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkbox_admin);
        boolean newState = checkBoxActive.isChecked();
        if (newState != _user.isAdmin()) {
            mLoadingView.showProgress(true);
            ParseObject user = ParseUser.createWithoutData(WebserviceConstants.kPARSE_OBJECT_USER, _user.objectId());
            user.put(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN, newState);

            UpdateUserTask mUpdateUserTask = new UpdateUserTask();
            mUpdateUserTask._parseObject = user;
            mUpdateUserTask.execute();
        }
    }

    private class GetResultsTask extends AsyncTask<Void, Void, List<Result>> {
        String objectId;
        GetResultsTask() {}

        @Override
        protected List<Result> doInBackground(Void... params) {
            try {
                return Result.resultsForUser(objectId);
            }
            catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Result> results) {
            mLoadingView.showProgress(false);
            if (results != null) {
                mListView.setAdapter(new ResultsViewAdapter(UserDetailsActivity.this, results));
            }
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
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
            UserDetailsActivity.this.finish();
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }
}
