package com.djkim.slap.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    private String m_facebookProfileId;

    // Constructors
    public User(String objectId, String username, Long facebookId)
    {
        m_objectId = objectId;
        m_username = username;
        m_facebookId = facebookId;
    }

    public User(ParseUser parseUser) {
        m_objectId = parseUser.getObjectId();
        m_username = parseUser.getUsername();
        m_facebookId = parseUser.getLong("facebookId");
        m_facebookProfileId = parseUser.getString("facebookProfileId");
        syncHackerSkills(parseUser);
        syncAthleteSkills(parseUser);
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
            syncAthleteSkills(parseUser);
            syncHackerSkills(parseUser);
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

//    public String get_image_url() {
//        return m_facebookProfileId;
//    }

    public boolean equals(User anotherUser){
        return (anotherUser.get_facebook_id().equals(this.m_facebookId));
    }

    public List<Group> getGroups() {
        try {
            ParseUser parseUser = ParseUser.getQuery().get(m_objectId);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
            query.whereEqualTo("members", parseUser);
            query.orderByDescending("createdAt");
            List<ParseObject> parseGroups = query.find();
            List<Group> groups = new ArrayList<Group>();
            for (ParseObject parseGroup : parseGroups) {
                Group group = new Group(parseGroup);
                groups.add(group);
            }
            return groups;
        } catch (ParseException e) {
            // No group is found. Return an empty list.
            return new ArrayList<Group>();
        }
    }

    public void getGroupsInBackground(final GroupsCallback callback)
    {
        ParseUser.getQuery().getInBackground(m_objectId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
                    query.whereEqualTo("members", parseUser);
                    query.orderByDescending("createdAt");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseGroups, ParseException e) {
                            if (e == null) {
                                List<Group> groups = new ArrayList<Group>();
                                for (ParseObject parseGroup : parseGroups) {
                                    Group group = new Group(parseGroup);
                                    groups.add(group);
                                }
                                callback.done(groups);
                            }
                        }
                    });
                }
            }
        });
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
        //parseUser.put("imageUrl", m_facebookProfileId);
        uploadAthleteSkills(parseUser);
        uploadHackerSkills(parseUser);
    }

    public void save() {
        try {
            ParseUser parseUser = ParseUser.getQuery().get(m_objectId);
            saveAllFieldsToParse(parseUser);
            parseUser.saveInBackground();
        } catch (ParseException e) {
            Log.e("", "Could not save ParseUser");
        }
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
        if (hacker_skills == null) {
            hacker_skills = Skill.returnHackerSkillsList();
        }
        //revert back to ArrayList
        for(int i = 0; i < arr.length(); i++)
        {
            try {
                JSONObject j = arr.getJSONObject(i);
                Boolean hasSkill = j.getBoolean("isSelected");
                hacker_skills.get(i).setSelected(hasSkill);
            } catch (JSONException e) {
                System.out.println("Cannot get hacker skills from JSONArray.\n");
            }
        }
    }

    private void convertJSONtoArrayList_Athlete(JSONArray arr)	//convert JSONArray from server and update the user's Athlete Skills list
    {
        if (athlete_skills == null) {
            athlete_skills = Skill.returnAthleteSkillsList();
        }
        //revert back to ArrayList
        for(int i = 0; i < arr.length(); i++)
        {
            try {
                JSONObject j = arr.getJSONObject(i);
                Boolean hasSkill = j.getBoolean("isSelected");
                athlete_skills.get(i).setSelected(hasSkill);
            } catch (JSONException e) {
                System.out.println("Cannot get athlete skills from JSONArray.\n");
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

    public void set_facebook_profile_id(String fbpid) {
        m_facebookProfileId = fbpid;
    }

    public String get_facebook_profile_id() {
        return m_facebookProfileId;
    }
}

