package com.dreamteam.octodrive.activity.admin;
;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.model.Settings;
import com.dreamteam.octodrive.utilities.LoadingView;

public class SettingsFragment extends Fragment {

    private EditText mQuestionCountEditText;
    private LoadingView mLoadingView;
    private QuestionCountTask mQuestionCountTask;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingView = new LoadingView(this.getActivity(), getString(R.string.dialog_loading));
        mLoadingView.showProgress(true);
        mQuestionCountTask = new QuestionCountTask();
        mQuestionCountTask.execute((Void) null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mQuestionCountEditText = (EditText)view.findViewById(R.id.editText_questionCount);
        mQuestionCountEditText.setOnEditorActionListener(new QuestionCountListener());
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mQuestionCountEditText != null) {
            mQuestionCountEditText.clearFocus();
        }
    }

    private class QuestionCountListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (v == mQuestionCountEditText && actionId == EditorInfo.IME_ACTION_DONE) {
                String input = mQuestionCountEditText.getText().toString();
                if (input.isEmpty()) {
                    mQuestionCountEditText.setError(getString(R.string.invalid_input));
                    return true;
                }
                Integer inputValue = Integer.valueOf(input);
                if (inputValue <= 0) {
                    mQuestionCountEditText.setError(getString(R.string.invalid_input));
                    return true;
                }
                mLoadingView.showProgress(true);
                SetQuestionCountTask setTask = new SetQuestionCountTask(inputValue);
                setTask.execute();
            }
            return false;
        }
    }

    private class QuestionCountTask extends AsyncTask<Void, Void, Integer> {
        QuestionCountTask() {}

        @Override
        protected Integer doInBackground(Void... params) {
            int questionCount = Settings.questionCount();
            return questionCount;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mLoadingView.showProgress(false);
            mQuestionCountEditText.setText(String.valueOf(integer));
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }

    private class SetQuestionCountTask extends AsyncTask<Void, Void, Void> {
        private Integer mQuestionCount = 0;

        SetQuestionCountTask(Integer questionCount) {
            mQuestionCount = questionCount;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Settings.setQuestionCount(mQuestionCount);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mLoadingView.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }
}


