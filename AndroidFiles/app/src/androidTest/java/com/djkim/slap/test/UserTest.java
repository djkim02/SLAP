package com.djkim.slap.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.djkim.slap.models.Skill;
import com.djkim.slap.models.User;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Random;

/**
 * Created by ryan on 11/17/15.
 */
public class UserTest extends InstrumentationTestCase{
    private User user;
    private ParseUser p_user;
    private String object_id;


    private final String FAKE_ATHLETE_JSON_ARRAY =
            "[{\"isSelected\":true,\"skill_name\":\"Running\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"Soccer\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"Basketball\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"Baseball\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Football\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Weight Training\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Frisbee\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Biking\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Bowling\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Badminton\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Ping Pong\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Cricket\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Golf\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Handball\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Yoga\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Boxing\"}]";

    private final String FAKE_HACKER_JSON_ARRAY =
            "[{\"isSelected\":false,\"skill_name\":\"Android Development\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"iOS Development\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Web Development\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Front-end Development\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Back-end Development\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"Java\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"C++\"}," +
                    "{\"isSelected\":true,\"skill_name\":\"C\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"C#\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Python\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"PHP\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"HTML\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"CSS\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"JavaScript\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Node.js\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"AngularJS\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Ruby\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Rails\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Coffeescript\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"MongoDB\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"MySQL\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"PostgreSQL\"}," +
                    "{\"isSelected\":false,\"skill_name\":\".NET\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Git\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Linux\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Photoshop\"}," +
                    "{\"isSelected\":false,\"skill_name\":\"Illustrator\"}]";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p_user = new ParseUser();
        // object_id = p_user.getObjectId();
        p_user.setUsername("Test User 88");
        p_user.setPassword("slaaaaaaap");
        p_user.setEmail("test@slap.com");

        p_user.put("facebookId", 123456789L);
        p_user.put("athlete_skills", FAKE_ATHLETE_JSON_ARRAY);
        p_user.put("hacker_skills", FAKE_HACKER_JSON_ARRAY);

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
        user = new User("123", "Test User 88", new Long(123456789L), "1");
    }

    @SmallTest
    public void testGetters(){
        givenUserIsInitialized();
        thenVerifyGetters();
    }

    private void thenVerifyGetters() {
        assertEquals(user.get_name(), "Test User 88");
        assertEquals(user.get_facebook_id(), (Long) 123456789L);
        assertEquals(user.get_id(), "123");
    }


    @Override
    protected void tearDown() throws Exception {
        p_user.deleteInBackground();
        super.tearDown();
    }
}
