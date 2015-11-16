package com.djkim.slap.models;

import android.content.Context;
import android.widget.ImageView;

import com.djkim.slap.R;
import com.djkim.slap.profile.CreateProfileActivity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/26/15.
 */
public class Skill implements Serializable {
    private int icon;
    private String skillName;
    private boolean selected;

    public Skill(int icon, String skillName) {
        this.icon = icon;
        this.skillName = skillName;
        this.selected = false;
    }

    public String getName() {
        return skillName;
    }

    public void setName(String skillName) {
        this.skillName = skillName;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getImageId() {
        return icon;
    }

    public static ArrayList<Skill> returnHackerSkillsList() {
        ArrayList<Skill> hackerSkillsList = new ArrayList<>();

        Skill hackerSkill = new Skill(R.mipmap.android, "Android Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.ios, "iOS Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.webdev, "Web Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.frontend, "Front-end Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.backend, "Back-end Development");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.java, "Java");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.cplusplus, "C++");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.c, "C");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.csharp, "C#");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.python, "Python");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.php, "PHP");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.html, "HTML");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.css3, "CSS");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.javascript, "JavaScript");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.nodejs, "Node.js");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.angularjs, "AngularJS");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.ruby, "Ruby");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.rails, "Rails");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.coffeescript, "Coffeescript");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.mongodb, "MongoDB");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.mysql, "MySQL");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.postgresql, "PostgreSQL");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.dotnet, ".NET");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.git, "Git");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.linux, "Linux");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.photoshop, "Photoshop");
        hackerSkillsList.add(hackerSkill);

        hackerSkill = new Skill(R.mipmap.illustrator, "Illustrator");
        hackerSkillsList.add(hackerSkill);

        return hackerSkillsList;
    }

    public static ArrayList<Skill> returnAthleteSkillsList() {
        ArrayList<Skill> athleteSkillsList = new ArrayList<>();

        Skill athleteSkill = new Skill(R.mipmap.running, "Running");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.soccer, "Soccer");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.basketball, "Basketball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.baseball, "Baseball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.football, "Football");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.weight, "Weight Training");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.frisbee, "Frisbee");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.biking, "Biking");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.bowling, "Bowling");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.badminton, "Badminton");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.pingpong, "Ping Pong");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.cricket, "Cricket");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.golf, "Golf");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.handball, "Handball");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.yoga, "Yoga");
        athleteSkillsList.add(athleteSkill);

        athleteSkill = new Skill(R.mipmap.boxing, "Boxing");
        athleteSkillsList.add(athleteSkill);

        return athleteSkillsList;
    }

    public static ArrayList<String> returnHackerSkillsList(ArrayList<Skill> hackerSkills) {
        ArrayList<String> hackerSkillsStringList = null;
        for(int i = 0; i < hackerSkills.size(); i++) {
            hackerSkillsStringList.add(hackerSkills.get(i).getName());
        }
        return hackerSkillsStringList;
    }
}