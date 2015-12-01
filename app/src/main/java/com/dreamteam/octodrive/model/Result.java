package com.dreamteam.octodrive.model;

import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lord Zsolt on 11/26/2015.
 */
public class Result extends OctoObject {

    public static List<Result> resultsForUser(String objectId) {
        ParseQuery<ParseObject> query = new ParseQuery<>(WebserviceConstants.kPARSE_OBJECT_RESULT);
        ParseObject user = ParseObject.createWithoutData(WebserviceConstants.kPARSE_OBJECT_USER, objectId);
        query.whereEqualTo(WebserviceConstants.kPARSE_PROPERY_RESULT_USER, user);
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

    public Result(String objectId) {
        _parseObject = new ParseObject(WebserviceConstants.kPARSE_OBJECT_RESULT);
        ParseObject user = ParseObject.createWithoutData(WebserviceConstants.kPARSE_OBJECT_USER, objectId);
        ParseRelation relation = _parseObject.getRelation(WebserviceConstants.kPARSE_PROPERY_RESULT_USER);
        relation.add(user);
        _parseObject.put(WebserviceConstants.kPARSE_PROPERY_RESULT_USER, relation);
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
        _parseObject.put(WebserviceConstants.kPARSE_PROPERY_RESULT_SCORE, score);
    }

    public Date date() {
        return _parseObject.getCreatedAt();
    }
}
