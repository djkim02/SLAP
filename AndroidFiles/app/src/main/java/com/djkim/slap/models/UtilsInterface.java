package com.djkim.slap.models;

import com.parse.ParseException;

/**
 * Created by ryan on 10/27/15.
 */
public interface UtilsInterface{

    public User get_current_user() throws ParseException;

    public User get_user_by_facebook_id(Long facebook_id) throws ParseException;

}
