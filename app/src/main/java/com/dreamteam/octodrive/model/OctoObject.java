package com.dreamteam.octodrive.model;

import com.parse.ParseObject;

/**
 * Created by Lord Zsolt on 11/26/2015.
 */
public abstract class OctoObject {

    protected ParseObject _parseObject;

    public ParseObject parseObject() {
        return _parseObject;
    }
}
