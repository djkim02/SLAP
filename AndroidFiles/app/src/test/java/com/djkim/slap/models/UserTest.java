package com.djkim.slap.models;

import android.test.InstrumentationTestCase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

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

    private List<Skill> fakeAthleteSkills = Skill.returnAthleteSkillsList();
    private List<Skill> fakeHackerSkills = Skill.returnHackerSkillsList();
    private List<ParseObject> fakeParseGroupList = new ArrayList<>();
    private JSONArray fakeAthleteJSONArray;
    private JSONArray fakeHackerJSONArray;


    @Mock
    ParseUser mockParseUser;

    @Mock
    ParseRelation<ParseObject> mockParseRelation;

    @Mock
    ParseQuery<ParseObject> mockParseQuery;

    @Mock
    ParseObject mockParseGroup;

    @Before @SuppressWarnings("unchecked")
    public void setUp() {
        mockParseUser = mock(ParseUser.class);
        when(mockParseUser.getObjectId()).thenReturn("fakeObjectId");
        when(mockParseUser.getUsername()).thenReturn("fakeUsername");
        when(mockParseUser.getLong("facebookId")).thenReturn(0L);
        when(mockParseUser.getString("facebookProfileId")).thenReturn("fakeFacebookProfileId");

        try {
            fakeAthleteJSONArray = new JSONArray(FAKE_ATHLETE_JSON_ARRAY);
            when(mockParseUser.getJSONArray("athlete_skills"))
                    .thenReturn(fakeAthleteJSONArray);
        } catch (JSONException e) {}

        try {
            fakeHackerJSONArray = new JSONArray(FAKE_HACKER_JSON_ARRAY);
            when(mockParseUser.getJSONArray("hacker_skills"))
                    .thenReturn(fakeHackerJSONArray);
        } catch (JSONException e) {}

        for (int i = 0; i < 4; i++) {
            fakeAthleteSkills.get(i).setSelected(true);
        }

        for (int i = 5; i < 8; i++) {
            fakeHackerSkills.get(i).setSelected(true);
        }

        mockParseGroup = mock(ParseObject.class);
        when(mockParseGroup.getObjectId()).thenReturn("fakeObjectId");
        when(mockParseGroup.getString("name")).thenReturn("fakeName");
        when(mockParseGroup.getString("description")).thenReturn("fakeDescription");
        when(mockParseGroup.getString("facebookGroupId")).thenReturn("fakeFacebookGroupId");
        when(mockParseGroup.getString("type")).thenReturn("fakeType");
        when(mockParseGroup.getString("skills")).thenReturn("Fake, Skills");
        when(mockParseGroup.getInt("capacity")).thenReturn(5);
        fakeParseGroupList.add(mockParseGroup);

        mockParseRelation = (ParseRelation<ParseObject>) mock(ParseRelation.class);
        mockParseQuery = (ParseQuery<ParseObject>) mock(ParseQuery.class);

        try {
            when(mockParseRelation.getQuery()).thenReturn(mockParseQuery);
            when(mockParseQuery.find()).thenReturn(fakeParseGroupList);
        } catch (ParseException e) {}

        when(mockParseUser.getRelation("memberOf")).thenReturn(mockParseRelation);
        when(mockParseUser.getRelation("ownerOf")).thenReturn(mockParseRelation);
    }

    @Test
    public void constructUserFromParseUser() {
        User user = new User(mockParseUser);
        assertEquals("fakeObjectId", user.get_id());
        assertEquals("fakeUsername", user.get_name());
        assertEquals("fakeFacebookProfileId", user.get_facebook_profile_id());

        List<Skill> athleteSkills = user.get_athlete_skills();
        for (int i = 0; i < athleteSkills.size(); i++) {
            assertEquals(fakeAthleteSkills.get(i).isSelected(), athleteSkills.get(i).isSelected());
        }

        List<Group> memberOfGroups = user.getGroups();
        for (int i = 0; i < memberOfGroups.size(); i++) {
            assertEquals("fakeObjectId", memberOfGroups.get(i).get_id());
        }

        verify(mockParseUser).getObjectId();
        verify(mockParseUser).getUsername();
        verify(mockParseUser).getLong("facebookId");
        verify(mockParseUser).getString("facebookProfileId");
    }
}
