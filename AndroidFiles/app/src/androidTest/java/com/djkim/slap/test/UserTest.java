package com.djkim.slap.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.djkim.slap.models.User;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;
import java.util.Random;

/**
 * Created by ryan on 11/17/15.
 */
public class UserTest extends InstrumentationTestCase{
    private User user;
    private ParseUser p_user;
    private String object_id;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p_user = new ParseUser();
        object_id = p_user.getObjectId();
        p_user.setUsername("Test User 88");
        p_user.setPassword("slaaaaaaap");
        p_user.setEmail("test@slap.com");

        p_user.put("facebookId", 123456789L);

        p_user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    assertTrue(true);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    assertFalse(false);
                }
            }
        });
    }

    private void givenUserIsInitialized() {
        user = new User(p_user);
    }

    @SmallTest
    public void testGetters(){
        givenUserIsInitialized();
        thenVerifyGetters();
    }

    private void thenVerifyGetters() {
        assertEquals(user.get_name(), "Test User 88");
        assertEquals(user.get_facebook_id(), new Long(123456789L));
        assertEquals(user.get_id(), object_id);
        // Log.d("test", ""+user.get_facebook_id());
    }

    @SmallTest
    public void testSetters(){
        givenUserIsInitialized();
        whenSetterIsCalled();
        thenVerifyGettersAfterSetters();
    }

    private void whenSetterIsCalled() {
        user.set_name("Viktorian");
        user.set_facebook_id(new Long(987654321L));
        user.set_id("KWANKWAN");
    }

    private void thenVerifyGettersAfterSetters() {
        assertEquals(user.get_name(), "Viktorian");
        assertEquals(user.get_facebook_id(), new Long(987654321L));
        assertEquals(user.get_id(), "KWANKWAN");
    }

    @SmallTest
    public void testSaveUser(){
        givenUserIsInitialized();

        Random r = new Random();
        Long random_l = new Long(r.nextLong());
        Integer random_i = new Integer(r.nextInt());

        whenUserIsSaved(random_l, random_i);
        thenVerifyUserIsSaved(random_l, random_i);
    }

    private void whenUserIsSaved(Long random_l, Integer random_i) {
        user.set_name("Random name");
        user.set_facebook_id(random_l);
        user.set_id(new String(String.valueOf(random_i)));
        user.save();
    }
    private void thenVerifyUserIsSaved(Long random_l, Integer random_i) {
        assertEquals(new User(String.valueOf(random_i), "Random name", random_l), new User(user.toParseUser()));
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
