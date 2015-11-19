package com.djkim.slap.models;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.parse.ParseUser;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
    @Test
    public void constructUserFromParseUser() {
        // Let's start by setting up expectations.
        ParseUser mockParseUser = mock(ParseUser.class);
        when(mockParseUser.getObjectId()).thenReturn("fakeObjectId");
        when(mockParseUser.getUsername()).thenReturn("fakeUsername");
        when(mockParseUser.getLong("facebookId")).thenReturn(0L);
        when(mockParseUser.getString("facebookProfileId")).thenReturn("fakeFacebookProfileId");

        User user = new User(mockParseUser);
        assertEquals("fakeObjectId", user.get_id());
        assertEquals("fakeUsername", user.get_name());
        assertEquals("fakeProfileId", user.get_facebook_profile_id());

        verify(mockParseUser).getObjectId();
        verify(mockParseUser).getUsername();
        verify(mockParseUser).getLong("facebookId");
        verify(mockParseUser).getString("facebookProfileId");
    }
}
