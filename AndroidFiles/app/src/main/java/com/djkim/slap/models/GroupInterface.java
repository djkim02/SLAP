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
    public String get_name();
    public void set_name(String name) throws ParseException;

    public String get_description();
    public void set_description(String description);

    public String get_id();

    public User get_owner();

    public int get_size();

    public int get_capacity();
    public void set_capacity(int capacity) throws ParseException;

    public boolean isOwner(User user);
    public boolean isMember(User user);

    public void addMember(User u) throws ParseException;

    public void addMembers(ArrayList<User> users) throws ParseException;

}
