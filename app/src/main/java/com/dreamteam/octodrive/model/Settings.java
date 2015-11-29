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

        String valueString = getSettingsValueForKey(WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT);
        if (valueString != null) {
            result = Integer.parseInt(valueString);
        }

        return result;
    }

    private static String getSettingsValueForKey(String key) {

        String valueString = null;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(
                WebserviceConstants.kPARSE_OBJECT_SETTINGS);
        query.whereEqualTo(WebserviceConstants.kPARSE_PROPERTY_SETTINGS_KEY, key);
        ParseObject settingsObject;
        try {
            settingsObject = query.getFirst();
            valueString =  settingsObject.getString(WebserviceConstants.kPARSE_PROPERTY_SETTINGS_VALUE);
        }
        catch (ParseException e) {
            Log.e(LogTag, "Settings.questionCount threw and exception, please make sure " +
                    WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT + " key exists in the " +
                    "parse database. Exception is: " + e.toString());
        }

        return valueString;
    }

}
