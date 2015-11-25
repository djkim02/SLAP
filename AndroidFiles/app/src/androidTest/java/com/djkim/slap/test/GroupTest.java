package com.djkim.slap.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.djkim.slap.models.Group;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class GroupTest extends InstrumentationTestCase{
    private ParseUser testUser;
    private ParseObject testGroup;
    private Group group;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // create a testParseUser
        testUser = new ParseUser();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setEmail("test@slap.com");
        testUser.put("facebookId", 1L);
        testUser.put("facebookProfileId", "1");
        testUser.signUpInBackground(new SignUpCallback() {
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

        testGroup = new ParseObject("group");
        testGroup.put("name", "test group");
        testGroup.put("description", "description for test group");
        testGroup.put("type", "Hacker");
        testGroup.put("capacity", 2);
        testGroup.put("skills", "C, C#");
        testGroup.put("facebookGroupId", 2L);
        testGroup.put("ownerName", "testUser");
        testGroup.put("ownerFacebookProfileId", "1");
        testGroup.put("size", 1);
        testGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    assertTrue(true);
                } else {
                    assertFalse(false);
                }
            }
        });
    }

    private void givenGroupIsInitialized() {
        group = new Group(testGroup);
    }

    @SmallTest
    public void testSetWithParseObject(){
        givenGroupIsInitialized();
        thenVerifySetWithParseObject();
    }

    private void thenVerifySetWithParseObject() {
        assertEquals("test group", group.get_name());
        assertEquals("description for test group", group.get_description());
        assertEquals("Hacker", group.get_type());
        assertEquals(2, group.get_capacity());
        assertEquals("C, C#", group.get_skills());
        assertEquals("testUser", group.get_owner_name());
        assertEquals(1, group.get_size());
    }

    private void givenSizeIncremented() {
        givenGroupIsInitialized();
        group.increment_size();
    }
    @SmallTest
    public void testIncrementSize() {
        givenSizeIncremented();
        thenVerifySizeIncremented();
    }

    private void thenVerifySizeIncremented() {
        assertEquals(2, group.get_size());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
