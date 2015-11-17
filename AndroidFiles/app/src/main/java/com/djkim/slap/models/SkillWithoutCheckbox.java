package com.djkim.slap.models;

import android.content.Context;
import android.widget.ImageView;

import com.djkim.slap.R;
import com.djkim.slap.profile.CreateProfileActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/26/15.
 */
public class SkillWithoutCheckbox {
    private int icon;
    private String skillName;

    public SkillWithoutCheckbox(int icon, String skillName) {
        this.icon = icon;
        this.skillName = skillName;
    }

    public String getName() {
        return skillName;
    }

    public void setName(String skillName) {
        this.skillName = skillName;
    }

    public int getImageId() {
        return icon;
    }

    public static ArrayList<SkillWithoutCheckbox> returnHackerSkillsList() {
        ArrayList<SkillWithoutCheckbox> hackerSkillsList = new ArrayList<>();

        SkillWithoutCheckbox hackerSkill = new SkillWithoutCheckbox(R.mipmap.android, "Android Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.ios, "iOS Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.webdev, "Web Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.frontend, "Front-end Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.backend, "Back-end Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.java, "Java");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.cplusplus, "C++");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.c, "C");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.csharp, "C#");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.python, "Python");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.php, "PHP");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.html, "HTML");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.css3, "CSS");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.javascript, "JavaScript");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.nodejs, "Node.js");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.angularjs, "AngularJS");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.ruby, "Ruby");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.rails, "Rails");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.coffeescript, "Coffeescript");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.mongodb, "MongoDB");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.mysql, "MySQL");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.postgresql, "PostgreSQL");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.dotnet, ".NET");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.git, "Git");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.linux, "Linux");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.photoshop, "Photoshop");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new SkillWithoutCheckbox(R.mipmap.illustrator, "Illustrator");
        hackerSkillsList.add(hackerSkill);

        return hackerSkillsList;
    }

    public static ArrayList<SkillWithoutCheckbox> returnAthleteSkillsList() {
        ArrayList<SkillWithoutCheckbox> athleteSkillsList = new ArrayList<>();

        SkillWithoutCheckbox athleteSkill = new SkillWithoutCheckbox(R.mipmap.running, "Running");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.soccer, "Soccer");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.basketball, "Basketball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.baseball, "Baseball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.football, "Football");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.weight, "Weight Training");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.frisbee, "Frisbee");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.biking, "Biking");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.bowling, "Bowling");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.badminton, "Badminton");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.pingpong, "Ping Pong");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.cricket, "Cricket");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.golf, "Golf");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.handball, "Handball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.yoga, "Yoga");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new SkillWithoutCheckbox(R.mipmap.boxing, "Boxing");
        athleteSkillsList.add(athleteSkill);

        return athleteSkillsList;
    }

    public static ArrayList<SkillWithoutCheckbox> returnMyProfileList() {
        ArrayList<SkillWithoutCheckbox> myProfileList = new ArrayList<>();

        SkillWithoutCheckbox profileSkill = new SkillWithoutCheckbox(R.mipmap.programming, "My Hacker Skills");
        myProfileList.add(profileSkill);

        profileSkill = new SkillWithoutCheckbox(R.mipmap.athlete, "My Athlete Skills");
        myProfileList.add(profileSkill);

        profileSkill = new SkillWithoutCheckbox(R.mipmap.edit, "Edit Profile");
        myProfileList.add(profileSkill);

        return myProfileList;
    }

    public static ArrayList<SkillWithoutCheckbox> returnOthersProfileList() {
        ArrayList<SkillWithoutCheckbox> othersProfileList = new ArrayList<>();

        SkillWithoutCheckbox profileSkill = new SkillWithoutCheckbox(R.mipmap.programming, "Hacker Skills");
        othersProfileList.add(profileSkill);

        profileSkill = new SkillWithoutCheckbox(R.mipmap.athlete, "Athlete Skills");
        othersProfileList.add(profileSkill);

        return othersProfileList;
    }
}