package com.dreamteam.octodrive.webservice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dreamteam.octodrive.model.User;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
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
}
