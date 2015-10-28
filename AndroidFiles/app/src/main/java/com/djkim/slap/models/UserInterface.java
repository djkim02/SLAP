package com.djkim.slap.models;

import com.parse.ParseException;

/**
 * Created by ryan on 10/27/15.
 */
public interface UserInterface {

    /* Construtor
     *  public User(long facebook_id) throws ParseException;
     */

    /*
     * Getters and Setters
     */
    public String get_name();

    public void set_name(String name) throws ParseException;

    public Long get_facebook_id();

    public void set_facebook_id(Long facebook_id) throws ParseException;

    /*
     * Check if two users are the same
     * the User objects may differ in some fields because they might get initialized/constructed at different time
     * this function checks whether they refer to the same object in database
     */
    public boolean equals(User anotherUser);

    /*
     * This action will write to the database
     * All previous modifications on the User object is considered as an ENTIRE transaction
     */
    public void save();
}
