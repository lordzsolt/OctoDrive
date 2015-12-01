package com.dreamteam.octodrive.model;

import com.parse.ParseObject;

/**
 * Created by Lord Zsolt on 11/26/2015.
 */
public abstract class OctoObject {

    protected String _objectId;

    protected ParseObject _parseObject;

    public ParseObject parseObject() {
        return _parseObject;
    }

    public void setObjectId(String objectId) {
        _objectId = objectId;
    }

    public String objectId() {
        if (_objectId == null) {
            _objectId = _parseObject.getObjectId();
        }
        return _objectId;
    }
}
