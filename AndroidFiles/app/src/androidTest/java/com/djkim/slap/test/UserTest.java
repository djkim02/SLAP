package com.djkim.slap.test;

import android.test.InstrumentationTestCase;

import com.djkim.slap.models.User;

/**
 * Created by ryan on 11/17/15.
 */
public class UserTest extends InstrumentationTestCase{
    private User user;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSaveUser(){
        givenUserIsInitialized();
        whenUserIsSaved();
        thenVerifyUserIsSaved();
    }

    private void givenUserIsInitialized() {
        user = new User();
    }
    private void whenUserIsSaved() {
    }
    private void thenVerifyUserIsSaved() {
        assertTrue(true);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
