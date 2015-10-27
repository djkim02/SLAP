package com.djkim.slap.models;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;

import bolts.Task;

/**
 * Created by joannachen on 10/26/15.
 */

/** was considering using the builder method for this... but got kind of weird with the ArrayList and all
 * maybe try this out later??
 */
public class Group {
    private String group_name;
    private String group_description;
    private String group_id;
    private User group_owner;
    private int group_capacity;
    private ArrayList<User> group_members = new ArrayList<User>();


    public Group() {}
    public Group(String name, User owner, int capacity) throws ParseException {
        group_name = name;
        group_owner = owner;
        group_capacity = capacity;
        group_members.add(owner);
        createParseGroup(owner.get_id());

    }

    public void createParseGroup(String id) throws ParseException {
        ParseObject obj = new ParseObject("Group");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", group_owner.get_id());
        ParseUser parseUser = query.getFirst();


        obj.put("Name", group_name);
        // obj.put("Owner", ParseObject.createWithoutData("_User", id));
        obj.put("Owner", parseUser);
        obj.put("Capacity", group_capacity);
        ParseRelation relation = obj.getRelation("Members");


        relation.add(parseUser);
        obj.save();
        group_id = obj.getObjectId();
        Log.e("MyApp", "Saving new Group!");

    }

    public String get_name() {
        return group_name;
    }

    public String get_description() {
        return group_description;
    }

    public String get_id() {
        return group_id;
    }

    public User get_owner() {
        return group_owner;
    }

    public int get_size() {
        return group_members.size();
    }

    public int get_capacity() {
        return group_capacity;
    }

    public void addUser(User u) throws ParseException {
        group_members.add(u);
        addParseUser(u);
    }

    public void addParseUser(User u) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", u.get_id());
        ParseUser parseUser = query.getFirst();

        ParseQuery<ParseObject> group_query = ParseQuery.getQuery("Group");
        group_query.whereEqualTo("objectId", group_id);
        ParseObject parseGroup = group_query.getFirst();
        ParseRelation relation = parseGroup.getRelation("Users");
        relation.add(parseUser);
    }

}
