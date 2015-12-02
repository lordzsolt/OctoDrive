package com.dreamteam.octodrive.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class User implements Listable, Parcelable {

    private String _objectId;

    private ParseUser _parseUser;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public User(Parcel in) {
        this();

        String objectId = in.readString();
        setObjectId(objectId);

        String name = in.readString();
        this.setName(name);

        String email = in.readString();
        this.setEmail(email);

        boolean admin = in.readByte() != 0;
        this.setAdmin(admin);
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

    public String objectId() {
        if (_objectId == null) {
            _objectId = _parseUser.getObjectId();
        }
        return _objectId;
    }

    public void setObjectId(String objectId) {
        _objectId = objectId;
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

    public void setAdmin(boolean admin) {
        _parseUser.put(WebserviceConstants.kPARSE_PROPERTY_USER_ADMIN, admin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId());
        dest.writeString(name());
        dest.writeString(email());
        dest.writeByte((byte) (isAdmin() ? 1 : 0));
    }
}
