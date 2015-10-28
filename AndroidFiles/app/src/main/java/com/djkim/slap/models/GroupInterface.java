package com.djkim.slap.models;

import com.parse.ParseException;

import java.util.ArrayList;

/**
 * Created by ryan on 10/27/15.
 */
public interface GroupInterface{
    /* Constructor
     *  public Group(String name, User owner, int capacity, String description = "") throws ParseException;
     */

    /*
     * Getters and Setters
     */

    /*
     * CALL SYNC() BEFORE GETTERS if you want up-to-date data from Parse
     * Best effort approach :3
     */
    public void sync();

    public String get_name();
    public void set_name(String name);

    public String get_description();
    public void set_description(String description);

    public String get_id();

    public User get_owner();

    public int get_size();

    public int get_capacity();
    public void set_capacity(int capacity);

    public ArrayList<User> getMembers();

    public boolean isOwner(User user);
    public boolean isMember(User user);

    public void addMember(User u);

    public void addMembers(ArrayList<User> users);

    public void save();

}
