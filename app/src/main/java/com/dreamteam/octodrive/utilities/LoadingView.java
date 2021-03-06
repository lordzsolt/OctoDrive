package com.dreamteam.octodrive.utilities;

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingView {
    private ProgressDialog mProgressDialog;

    public LoadingView(final Activity activity, final String message) {
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
    }

    public void showProgress(final boolean show) {
        if (show) {
            mProgressDialog.show();
        }
        else {
            mProgressDialog.dismiss();
        }
    }
}
