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
    private static final int kDEFAULT_VALUE = -1;

    private static int _questionCount = kDEFAULT_VALUE;

    public static void invalidate() {
        _questionCount = kDEFAULT_VALUE;
    }

    public static int questionCount() {
        if (_questionCount != kDEFAULT_VALUE) {
            return _questionCount;
        }
        int result = 0;

        String valueString = getSettingsValueForKey(WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT);
        if (valueString != null) {
            result = Integer.parseInt(valueString);
        }

        return result;
    }

    public static void setQuestionCount(int questionCount) {
        setSettingsValueForKey(WebserviceConstants.kPARSE_KEY_SETTINGS_QUESTION_COUNT, String.valueOf(questionCount));
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
            Log.e(LogTag, "Settings.getSettingsValueForKey threw and exception, please make sure " +
                    key + " key exists in the parse database. Exception is: " + e.toString());
        }

        return valueString;
    }

    private static void setSettingsValueForKey(String key, String value) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(
                WebserviceConstants.kPARSE_OBJECT_SETTINGS);
        query.whereEqualTo(WebserviceConstants.kPARSE_PROPERTY_SETTINGS_KEY, key);
        ParseObject settingsObject;
        try {
            settingsObject = query.getFirst();
            settingsObject.put(WebserviceConstants.kPARSE_PROPERTY_SETTINGS_VALUE, value);
            settingsObject.save();
        }
        catch (ParseException e) {
            Log.e(LogTag, "Settings.setSettingsValueForKey threw and exception, please make sure " +
                    key + " key exists in the parse database. Exception is: " + e.toString());
        }
    }

}
