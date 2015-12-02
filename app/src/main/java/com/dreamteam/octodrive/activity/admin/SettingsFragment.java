package com.dreamteam.octodrive.activity.admin;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.model.Settings;
import com.dreamteam.octodrive.utilities.LoadingView;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {

    private EditText mQuestionCountEditText;
    private EditText mMimimumPassEditText;
    private LoadingView mLoadingView;
    private QuestionCountTask mQuestionCountTask;

    private final static int kDEFAULT_QUESTION_COUNT_VALUE = -1;
    private int mQuestionCount = kDEFAULT_QUESTION_COUNT_VALUE;
    private int mMinimumPass = kDEFAULT_QUESTION_COUNT_VALUE;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mQuestionCountEditText = (EditText)view.findViewById(R.id.editText_questionCount);
        mMimimumPassEditText = (EditText)view.findViewById(R.id.editText_minimumPass);

        Button saveButton = (Button)view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput();
            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mQuestionCountEditText != null) {
            mQuestionCountEditText.clearFocus();
        }
        else if(isVisibleToUser && mQuestionCount == kDEFAULT_QUESTION_COUNT_VALUE) {
            mLoadingView = new LoadingView(this.getActivity(), getString(R.string.dialog_loading));
            mLoadingView.showProgress(true);
            mQuestionCountTask = new QuestionCountTask();
            mQuestionCountTask.execute((Void) null);
        }
    }

    private void saveInput() {
        String inputQuestionCount = mQuestionCountEditText.getText().toString();
        if (inputQuestionCount.isEmpty()) {
            mQuestionCountEditText.setError(getString(R.string.invalid_input));
            return;
        }
        Integer inputValueQC = Integer.valueOf(inputQuestionCount);
        if (inputValueQC <= 0) {
            mQuestionCountEditText.setError(getString(R.string.invalid_input));
            return;
        }

        String inputMinimumPass = mMimimumPassEditText.getText().toString();
        if (inputMinimumPass.isEmpty()) {
            mMimimumPassEditText.setError(getString(R.string.invalid_input));
            return;
        }
        Integer inputValueMP = Integer.valueOf(inputMinimumPass);
        if (inputValueMP <= 0) {
            mMimimumPassEditText.setError(getString(R.string.invalid_input));
            return;
        }
        mLoadingView.showProgress(true);
        mQuestionCount = inputValueQC;
        mMinimumPass = inputValueMP;
        SaveTask saveTask = new SaveTask(inputValueQC, inputValueMP);
        saveTask.execute();
    }

    private class QuestionCountTask extends AsyncTask<Void, Void, List<Integer>> {
        QuestionCountTask() {}

        @Override
        protected List<Integer> doInBackground(Void... params) {
            int questionCount = Settings.questionCount();
            int minimumPass = Settings.minimumPass();
            return Arrays.asList(questionCount, minimumPass);
        }

        @Override
        protected void onPostExecute(List<Integer> response) {
            mLoadingView.showProgress(false);
            mQuestionCount = response.get(0);
            mQuestionCountEditText.setText(String.valueOf(mQuestionCount));
            mMinimumPass = response.get(1);
            mMimimumPassEditText.setText(String.valueOf(mMinimumPass));
        }

        @Override
        protected void onCancelled() {
            mLoadingView.showProgress(false);
        }
    }

    private class SaveTask extends AsyncTask<Void, Void, Void> {
        private Integer mQuestionCount = 0;
        private Integer mMinimumPass = 0;

        SaveTask(Integer questionCount, Integer minimumPass) {
            mQuestionCount = questionCount;
            mMinimumPass = minimumPass;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Settings.setQuestionCount(mQuestionCount);
            Settings.setMinimumPass(mMinimumPass);
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


