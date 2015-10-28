package com.djkim.slap.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjoonkim on 10/25/15.
 * This class is mainly a data structure to hold all the necessary data
 * for the users. It is used to communicate with Parse and to create a
 * profile for a user
 */
public class User implements Serializable, UserInterface {

    private String m_objectId;
    private String m_username;
    private Long m_facebookId;
    private ParseUser m_parseUser;
    // private ParseUser parseUser;

    // Constructors
    public User(Long facebook_id) throws ParseException {
        Log.e("MyApp", "In the user constructor");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("facebookId", facebook_id);
        // ParseUser parseUser = null;
        try {
            m_parseUser = query.getFirst();
            Log.e("MyApp", "Set m_parseUser initialized!");
        } catch (ParseException e) {
            throw new ParseException(e);
        }
        m_objectId = m_parseUser.getObjectId();
        // sync();
        // Log.e("MyApp", "constructor sync!");
        m_username = m_parseUser.getUsername();
        m_facebookId = m_parseUser.getLong("facebookId");
    }

    /*
     * DEPRECATED
     * TO THINK ABOUT: USERNAME NOT UNIQUE
     */
    private User(String username) throws ParseException{
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        ParseUser parseUser = query.getFirst();
        m_objectId = parseUser.getObjectId();
        m_username = parseUser.getUsername();
        m_facebookId = parseUser.getLong("facebookId");
    }

    // Getter for the corresponding ParseUser object
    public ParseUser getParseUser(){
//        try {
//            ParseQuery<ParseUser> query = ParseUser.getQuery();
//            return query.get(m_objectId);
//        }catch(ParseException e){
//            throw new ParseException(e);
//        }
        return m_parseUser;
    }

    public void sync(){
        m_parseUser.fetchInBackground();
    }

    // Getters and Setters
    private String get_id() {
        return m_objectId;
    }

    private void set_id(String id) throws ParseException{
        m_objectId = id;
        ParseUser user = null;

        user = getParseUser();

        user.put("objectId", id);
    }

    public String get_name(){
        m_username = getParseUser().getUsername();
        return m_username;
    }

    public void set_name(String name){
        m_username = name;
        ParseUser user = null;
        user = getParseUser();
        user.put("username", name);
    }

    public Long get_facebook_id() {
        return m_facebookId;
    }

    public void set_facebook_id(Long facebook_id){
        m_facebookId = facebook_id;
        ParseUser parseUser = null;
        parseUser = getParseUser();
        parseUser.put("facebookId", facebook_id);

    }

    public boolean equals(User anotherUser){
        return (anotherUser.get_facebook_id().equals(this.m_facebookId));
    }

    public void save(){
        m_parseUser.saveInBackground();
    }

}

