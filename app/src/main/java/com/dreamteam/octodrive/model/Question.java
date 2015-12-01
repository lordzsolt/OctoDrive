package com.dreamteam.octodrive.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreamteam.octodrive.interfaces.Listable;
import com.dreamteam.octodrive.webservice.WebserviceConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lord Zsolt on 11/20/2015.
 */
public class Question extends OctoObject implements Listable, Parcelable {

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public static List<Question> getPredefinedNumberOfQuestions(String language) throws ParseException {
        int predefinedCount = Settings.questionCount();
        return getQuestions(predefinedCount, language, true);
    }

    public static List<Question> getQuestions(int count, String language, boolean onlyActive) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(
                WebserviceConstants.kPARSE_OBJECT_QUESTION);
        if (count > 0) {
            query.setLimit(count);
        }
        if (onlyActive) {
            query.whereEqualTo(WebserviceConstants.kPARSE_PROPERTY_QUESTION_ACTIVE, true);
        }
        if (language != null) {
            query.whereEqualTo(WebserviceConstants.kPARSE_PROPERTY_QUESTION_LANGUAGE, language);
        }
        List<ParseObject> questions = query.find();

        List<Question> array = new ArrayList<>();
        for (ParseObject object : questions) {
            Question question = new Question(object);
            array.add(question);
        }
        return array;
    }

    public static List<Question> getAllQuestions() throws ParseException {
        return getQuestions(0, null, false);
    }

    public Question() {
        _parseObject = new ParseObject(WebserviceConstants.kPARSE_OBJECT_QUESTION);
    }

    public Question(ParseObject question) {
        _parseObject = question;
    }

    public Question(Parcel in) {
        this();

        String objectId = in.readString();
        _parseObject.setObjectId(objectId);

        String message = in.readString();
        this.setMessage(message);

        List<String> answers = new ArrayList<>(3);
        in.readList(answers, null);
        this.setAnswers(answers);

        List<Boolean> correntAnswers = new ArrayList<>(3);
        in.readList(correntAnswers, null);
        this.setCorrectAnswers(correntAnswers);

        String language = in.readString();
        this.setLanguage(language);

        boolean active = in.readByte() != 0;
        this.setActive(active);
    }

    @Override
    public String displayText() {
        return message();
    }

    public void save() throws ParseException {
        _parseObject.save();
    }

    public String message() {
        return _parseObject.getString(WebserviceConstants.kPARSE_PROPERTY_QUESTION_MESSAGE);
    }

    public void setMessage(String message) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_MESSAGE, message);
    }

    public List<String> answers() {
        return _parseObject.getList(WebserviceConstants.kPARSE_PROPERTY_QUESTION_ANSWERS);
    }

    public void setAnswers(List<String> answers) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_ANSWERS, answers);
    }

    public List<Boolean> correctAnswers() {
        int correct = _parseObject.getInt(WebserviceConstants.kPARSE_PROPERTY_QUESTION_CORRECT);

        List<Boolean> correctAnswers = new ArrayList<>();
        int mask = 1;
        for (int index = 0; index < 3; index++) {
            int value = correct & mask;
            correctAnswers.add(value != 0);
            mask *= 2;
        }

        return correctAnswers;
    }

    public void setCorrectAnswers(List<Boolean> correctAnswers) {
        int correct = 0;

        int mask = 1;
        for (int index = 0; index < 3; index++) {
            if (correctAnswers.get(index)) {
                correct += mask;
            }
            mask *= 2;
        }

        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_CORRECT, correct);
    }

    public void image() {

    }

    public void setImage() {

    }

    public boolean active() {
        return _parseObject.getBoolean(WebserviceConstants.kPARSE_PROPERTY_QUESTION_ACTIVE);
    }

    public void setActive(boolean active) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_ACTIVE, active);
    }

    public String language() {
        return _parseObject.getString(WebserviceConstants.kPARSE_PROPERTY_QUESTION_LANGUAGE);
    }

    public void setLanguage(String language) {
        _parseObject.put(WebserviceConstants.kPARSE_PROPERTY_QUESTION_LANGUAGE, language);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId());
        dest.writeString(message());
        dest.writeList(answers());
        dest.writeList(correctAnswers());
        dest.writeString(language());
        dest.writeByte((byte) (active() ? 1 : 0));
        //TODO: Write Image
    }
}
