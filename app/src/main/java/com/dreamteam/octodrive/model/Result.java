package com.dreamteam.octodrive.model;

import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lord Zsolt on 11/26/2015.
 */
public class Result extends OctoObject {

    public static List<Result> resultsForUser(User user) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WebserviceConstants.kPARSE_OBJECT_RESULT);
        query.whereEqualTo(WebserviceConstants.kPARSE_PROPERY_RESULT_USER, user.parseUser());
        List<Result> array = new ArrayList<>();
        List<ParseObject> results;
        try {
            results = query.find();
        }
        catch (ParseException e) {
            return array;
        }

        for (ParseObject object : results) {
            Result result = new Result(object);
            array.add(result);
        }
        return array;
    }

    public Result(User user) {
        _parseObject = new ParseObject(WebserviceConstants.kPARSE_OBJECT_RESULT);
        _parseObject.put(WebserviceConstants.kPARSE_PROPERY_RESULT_USER, user.parseUser());
    }

    public Result(ParseObject parseObject) {
        _parseObject = parseObject;
    }

    public void save() throws Exception {
        if (_parseObject.getObjectId() != null) {
            throw new Exception("Results cannot be edited");
        }
        _parseObject.save();
    }

    public boolean isLive() {
        return _parseObject.getBoolean(WebserviceConstants.kPARSE_PROPERY_RESULT_LIVE);
    }

    public void setLive(boolean live) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERY_RESULT_LIVE, live);
    }

    public int score() {
        return _parseObject.getInt(WebserviceConstants.kPARSE_PROPERY_RESULT_SCORE);
    }

    public void setScore(int score) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_CORRECT, score);
    }

    public Date date() {
        return _parseObject.getCreatedAt();
    }
}
