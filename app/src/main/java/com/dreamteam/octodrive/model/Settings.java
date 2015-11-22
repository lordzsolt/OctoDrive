package com.dreamteam.octodrive.model;

import android.util.Log;

import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Lord Zsolt on 11/22/2015.
 */
public class Settings {

    private static final String LogTag = "SETTINGS";

    public static int questionCount() {
        int result = 0;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(
                WebserviceConstants.kPARSE_OBJECT_SETTINGS);
        query.whereEqualTo(WebserviceConstants.kPARSE_PROPERY_SETTINGS_KEY,
                           WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT);
        ParseObject settingsObject = null;
        try {
            settingsObject = query.getFirst();
        }
        catch (ParseException e) {
            Log.e(LogTag, "Settings.questionCount threw and exception, please make sure " +
                    WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT + " key exists in the " +
                    "parse database. Exception is: " + e.toString());
        }

        String valueString = settingsObject.getString(WebserviceConstants.kPARSE_PROPERY_SETTINGS_VALUE);
        if (valueString != null) {
            result = Integer.parseInt(valueString);
        }

        return result;
    }

}
