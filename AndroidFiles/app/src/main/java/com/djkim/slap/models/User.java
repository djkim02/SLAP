package com.djkim.slap.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjoonkim on 10/25/15.
 * This class is mainly a data structure to hold all the necessary data
 * for the users. It is used to communicate with Parse and to create a
 * profile for a user
 */
public class User implements Serializable {
    private ArrayList<Skill> hacker_skills;
    private ArrayList<Skill> athlete_skills;
    private String m_objectId;
    private String m_username;
    private Long m_facebookId;
//    private ArrayList<Group> m_groups; // all groups the user is a part of
//    private ArrayList<Group> m_groupsWhereUserIsOwner; // all groups the user is an owner of

    // Constructors
    public User(){}
    public User(String objectId, String username, Long facebookId)
    {
        m_objectId = objectId;
        m_username = username;
        m_facebookId = facebookId;
//        m_groups = new ArrayList<Group>();
//        m_groupsWhereUserIsOwner = new ArrayList<Group>();
    }

    public User(ParseUser parseUser) {
        m_objectId = parseUser.getObjectId();
        m_username = parseUser.getUsername();
        m_facebookId = parseUser.getLong("facebookId");
    }

//    public void addGroup (Group group) {
//        m_groups.add(group);
//    }

//    public void syncUsername(ParseUser parseUser) {
//        m_username = parseUser.getUsername();
//    }
//
//    public void syncFacebookId(ParseUser parseUser) {
//        m_facebookId = parseUser.getLong("facebookId");
//    }
//
//    public void syncGroups(ParseUser parseUser) {
//        // get groups from relation
//        // clear m_groupsWhereUserIsMember
//        // fill m_groupsWhereUserIsMember
//        ParseRelation<ParseUser> relation = parseUser.getRelation("groups");
//        ParseQuery query = relation.getQuery();
//    }

    public void syncAthleteSkills(ParseUser parseUser)	//retrieve data from Parse and update user's athlete skills
    {
        JSONArray fetched_array = parseUser.getJSONArray("athlete_skills");
        convertJSONtoArrayList_Athlete(fetched_array);
    }

    public void syncHackerSkills(ParseUser parseUser)	//retrieve data from Parse and update user's hacker skills
    {
        JSONArray fetched_array = parseUser.getJSONArray("hacker_skills");
        convertJSONtoArrayList_Hacker(fetched_array);
    }

    // based on current m_object ID,
    // update the Java User's data with
    // the ParseUser's data from the database
    public void sync(){
        // query for user based on m_objectId
        // update username and other fields
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            ParseUser parseUser = query.get(m_objectId);
//            syncGroups(parseUser);

        } catch (ParseException e) {
            return; // didn't find anything
        }
    }

    // Getters and Setters
    public String get_id() {
        return m_objectId;
    }

    public void set_id(String id) {
        m_objectId = id;
    }

    public String get_name(){
        return m_username;
    }

    public void set_name(String name){
        m_username = name;
    }

    public Long get_facebook_id() {
        return m_facebookId;
    }

    public void set_facebook_id(Long facebook_id){
        m_facebookId = facebook_id;
    }

    public boolean equals(User anotherUser){
        return (anotherUser.get_facebook_id().equals(this.m_facebookId));
    }

    public void setFieldsWithParseUser(ParseUser parseOwner) {
        m_objectId = parseOwner.getObjectId();
        m_username = parseOwner.getUsername();
        m_facebookId = parseOwner.getLong("facebookId");
        syncHackerSkills(parseOwner);
        syncAthleteSkills(parseOwner);
        // TODO: set Arrays too!
    }

//    public void saveGroupsToParse(ParseUser parseUser) {
//        ParseRelation<ParseObject> relation = parseUser.getRelation("groups");
//        for (Group group: m_groups) {
//            ParseObject parseGroup = ParseObject.createWithoutData("Group", group.get_id());
//            relation.add(parseGroup);
//        }
//    }

    public List<Group> getGroups() {
        try {
            ParseUser parseUser = ParseUser.getQuery().get(m_objectId);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
            query.whereEqualTo("members", parseUser);
            List<ParseObject> parseGroups = query.find();
            List<Group> groups = new ArrayList<Group>();
            for (ParseObject parseGroup : parseGroups) {
                Group group = new Group(parseGroup);
                groups.add(group);
            }
            return groups;
        } catch (ParseException e) {
            Log.e("", "Cannot get list of groups");
            return null;
        }
    }

    public ParseUser toParseUser()
    {
        ParseUser parseUser = new ParseUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        try {
            parseUser = query.get(m_objectId);
            return parseUser;
        } catch (ParseException e) {
            return parseUser;
        }
    }

    private void saveAllFieldsToParse(ParseUser parseUser) {
        parseUser.put("username", m_username);
        parseUser.put("facebookId", m_facebookId);
        uploadAthleteSkills(parseUser);
        uploadHackerSkills(parseUser);
//        saveGroupsToParse(parseUser);
    }

    public void save() {
        try {
            ParseUser parseUser = ParseUser.getQuery().get(m_objectId);
            saveAllFieldsToParse(parseUser);
            parseUser.saveInBackground();
        } catch (ParseException e) {
            Log.e("", "Could not save ParseUser");
        }
//        ParseUser parseUser = new ParseUser();
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("facebook_id", m_facebookId);
//        try {
//            parseUser = query.getFirst();
//            saveAllFieldsToParse(parseUser);
//            parseUser.saveInBackground();
//        } catch (ParseException e) {
//            saveAllFieldsToParse(parseUser);
//            parseUser.saveInBackground();
//            m_objectId = parseUser.getObjectId(); // do I need to do this?
//        }
    }

    //helper functions for uploading to and retrieving from Parse
    public void uploadAthleteSkills(ParseUser parseUser)	//send data to Parse
    {
        JSONArray new_array = this.convertToJSON(this.athlete_skills);
        parseUser.put("athlete_skills", new_array);
    }

    public void uploadHackerSkills(ParseUser parseUser)	//send data to Parse
    {
        JSONArray new_array = this.convertToJSON(this.hacker_skills);
        parseUser.put("hacker_skills", new_array);
    }

    private JSONArray convertToJSON(ArrayList<Skill> list)
    {
        JSONArray json_arr_skills = new JSONArray();
        for(int i = 0; i < list.size(); i++)
        {
            JSONObject obj = new JSONObject();
            try {
                obj.accumulate("skill_name", list.get(i).getName());
            } catch (JSONException e) {
                System.out.println("Could not construct JSON Object for Skill Name.");
            }
            try {
                obj.accumulate("isSelected", list.get(i).isSelected());
            } catch (JSONException e) {
                System.out.println("Could not construct JSON Object for isSelected.");
            }
            json_arr_skills.put(obj);
        }
        return json_arr_skills;
    }

    private void convertJSONtoArrayList_Hacker(JSONArray arr)	//convert JSONArray from server and update Hacker Skills list
    {
        //revert back to ArrayList
        for(int i = 0; i < arr.length(); i++)
        {
            try {
                JSONObject j = arr.getJSONObject(i);
                JSONArray hasSkill = j.getJSONArray("isSelected");
                Boolean hasSkill1 = hasSkill.getBoolean(0);

                hacker_skills.get(i).setSelected(hasSkill1);

            } catch (JSONException e) {
                System.out.println("Cannot get keys/values from JSONArray.\n");
            }
        }
    }

    private void convertJSONtoArrayList_Athlete(JSONArray arr)	//convert JSONArray from server and update the user's Athlete Skills list
    {
        //revert back to ArrayList
        for(int i = 0; i < arr.length(); i++)
        {
            try {
                JSONObject j = arr.getJSONObject(i);
                JSONArray hasSkill = j.getJSONArray("isSelected");
                Boolean hasSkill1 = hasSkill.getBoolean(0);

                athlete_skills.get(i).setSelected(hasSkill1);

            } catch (JSONException e) {
                System.out.println("Cannot get keys/values from JSONArray.\n");
            }
        }
    }

    public ArrayList<Skill> get_hacker_skills() {
        return hacker_skills;
    }

    public ArrayList<Skill> get_athlete_skills() {
        return athlete_skills;
    }

    public void set_hacker_skills(ArrayList<Skill> hacker_skills) {
        this.hacker_skills = hacker_skills;
    }

    public void set_athlete_skills(ArrayList<Skill> athlete_skills) {
        this.athlete_skills = athlete_skills;
    }
}

