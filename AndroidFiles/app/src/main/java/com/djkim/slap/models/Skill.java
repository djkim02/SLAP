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
public class Skill {
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

    public static ArrayList<Skill> returnAthleteSkillsList(Context context) {
        ArrayList<Skill> athleteSkillsList = new ArrayList<>();
        
        return athleteSkillsList;
    }
}