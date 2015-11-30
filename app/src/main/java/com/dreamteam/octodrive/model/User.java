package com.dreamteam.octodrive.model;

import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lordzsolt on 11/19/2015.
 */
public class User implements Listable {

    private ParseUser _parseUser;

    public static List<User> getAllUsers() throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        List<ParseUser> users = query.find();

        List<User> array = new ArrayList<>();
        for (ParseUser object : users) {
            User user = new User(object);
            array.add(user);
        }
        return array;
    }

    public static User login(String username, String password) throws ParseException {
        ParseUser pUser = ParseUser.logIn(username, password);
        return new User(pUser);
    }

    public User() {
        _parseUser = new ParseUser();
        _parseUser.put(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN, false);
    }

    public User(ParseUser parseUser) {
        _parseUser = parseUser;
    }

    @Override
    public String displayText() {
        return name();
    }

    public void save() throws ParseException {
        if (_parseUser.getObjectId() == null) {
            _parseUser.signUp();
        }
        else {
            _parseUser.save();
        }
    }

    public ParseUser parseUser() {
        return _parseUser;
    }

    public String name() {
        return _parseUser.getString(WebserviceConstants.kPARSE_PROPERTY_USER_NAME);
    }

    public void setName(String name) {
        _parseUser.put(WebserviceConstants.kPARSE_PROPERTY_USER_NAME, name);
    }

    public String email() {
        return _parseUser.getEmail();
    }

    public void setEmail(String email) {
        _parseUser.setEmail(email);
    }

    public String username() {
        return _parseUser.getUsername();
    }

    public void setUsername(String username) {
        _parseUser.setUsername(username);
    }

    public void setPassword(String password) {
        _parseUser.setPassword(password);
    }

    public boolean isAdmin() {
        return _parseUser.getBoolean(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN);
    }

}
