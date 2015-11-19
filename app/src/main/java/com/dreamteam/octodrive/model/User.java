package com.dreamteam.octodrive.model;

import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseUser;

/**
 * Created by Lordzsolt on 11/19/2015.
 */
public class User {

    private ParseUser _parseUser;

    public User(ParseUser parseUser) {
        _parseUser = parseUser;
    }

    public String email() {
        return _parseUser.getEmail();
    }

    public String name() {
        return _parseUser.getString(WebserviceConstants.kPARSE_PROPERTY_USER_NAME);
    }

    public boolean isAdmin() {
        return _parseUser.getBoolean(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN);
    }


}
