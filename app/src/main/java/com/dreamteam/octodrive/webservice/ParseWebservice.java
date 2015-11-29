package com.dreamteam.octodrive.webservice;

import android.content.Context;

import com.parse.Parse;

/**
 * Created by Lordzsolt on 11/19/2015.
 */
public class ParseWebservice  {

    public static void initialise(Context context, String applicationId, String clientKey) {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, applicationId, clientKey);
    }
}
