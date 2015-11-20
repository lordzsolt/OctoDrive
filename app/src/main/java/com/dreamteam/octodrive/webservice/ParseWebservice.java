package com.dreamteam.octodrive.webservice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dreamteam.octodrive.model.User;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Lordzsolt on 11/19/2015.
 */
public class ParseWebservice  {

    private static final String LogTag = "ParseWebservice";

    public ParseWebservice(Context context, String applicationId, String clientKey) {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, applicationId, clientKey);
    }


    //User Methods
    public @Nullable User login(String username, String password) {
        try {
            ParseUser parseUser = ParseUser.logIn(username, password);
            return new User(parseUser);
        }
        catch (ParseException e) {
            Log.d(LogTag, e.getMessage());
            return null;
        }
    }

    public @Nullable User regiser(String username, String password, String name) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.setEmail(username);
        parseUser.put(WebserviceConstants.kPARSE_PROPERTY_USER_NAME, name);
        parseUser.put(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN, false);

        try {
            parseUser.signUp();
            return new User(parseUser);
        }
        catch (ParseException e) {
            Log.d(LogTag, e.getMessage());
            return null;
        }
    }


    //Settings Methods
    public int getQuestionCount() {
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
            Log.e(LogTag, "Webservice.getQuestionCount threw and exception, please make sure " +
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
