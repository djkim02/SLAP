package com.djkim.slap.login;

import java.io.Serializable;

/**
 * Created by dongjoonkim on 10/25/15.
 * This class is mainly a data structure to hold all the necessary data
 * for the users. It is used to communicate with Parse and to create a
 * profile for a user
 */
public class User implements Serializable {
    private String user_id;
    private String user_name;
    private CircularImageView user_profile_pic;

    public User(String id) {
        user_id = id;
    }

    public String get_id() {
        return user_id;
    }

    public String get_name() {
        return user_name;
    }

    public void set_name(String name) {
        user_name = name;
    }

    public CircularImageView get_user_profile_pic() {
        return user_profile_pic;
    }

    public void set_user_profile_pic(CircularImageView profilePic) {
        user_profile_pic = profilePic;
    }
}
