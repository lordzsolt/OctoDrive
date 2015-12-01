package com.dreamteam.octodrive.webservice;

import android.content.Context;

import com.parse.Parse;

public class ParseWebservice  {

    private static boolean initialized = false;

    public static void initialise(Context context, String applicationId, String clientKey) {
        if (initialized) {
            // otherwise parse will cry like a little *cough*
            return;
        }

        Parse.enableLocalDatastore(context);
        Parse.initialize(context, applicationId, clientKey);

        initialized = true;
    }
}
