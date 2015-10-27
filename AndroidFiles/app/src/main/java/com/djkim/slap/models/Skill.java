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
    private ImageView icon;
    private String skillName;
    private boolean selected;

    public Skill(ImageView icon, String skillName) {
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

    public static ArrayList<Skill> returnHackerSkillsList(Context context) {
        ArrayList<Skill> hackerSkillsList = new ArrayList<>();
        ImageView icon = new ImageView(context);

        icon.setImageResource(R.drawable.android);
        Skill hackerSkill = new Skill(icon, "Android Development");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.ios);
        hackerSkill = new Skill(icon, "iOS Development");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.webdev);
        hackerSkill = new Skill(icon, "Web Development");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.frontend);
        hackerSkill = new Skill(icon, "Front-end Development");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.backend);
        hackerSkill = new Skill(icon, "Back-end Development");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.java);
        hackerSkill = new Skill(icon, "Java");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.cplusplus);
        hackerSkill = new Skill(icon, "C++");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.c);
        hackerSkill = new Skill(icon, "C");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.csharp);
        hackerSkill = new Skill(icon, "C#");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.python);
        hackerSkill = new Skill(icon, "Python");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.php);
        hackerSkill = new Skill(icon, "PHP");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.html);
        hackerSkill = new Skill(icon, "HTML");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.css3);
        hackerSkill = new Skill(icon, "CSS");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.javascript);
        hackerSkill = new Skill(icon, "JavaScript");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.nodejs);
        hackerSkill = new Skill(icon, "Node.js");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.angularjs);
        hackerSkill = new Skill(icon, "AngularJS");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.ruby);
        hackerSkill = new Skill(icon, "Ruby");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.rails);
        hackerSkill = new Skill(icon, "Rails");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.coffeescript);
        hackerSkill = new Skill(icon, "Coffeescript");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.mongodb);
        hackerSkill = new Skill(icon, "MongoDB");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.mysql);
        hackerSkill = new Skill(icon, "MySQL");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.postgresql);
        hackerSkill = new Skill(icon, "PostgreSQL");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.dotnet);
        hackerSkill = new Skill(icon, ".NET");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.git);
        hackerSkill = new Skill(icon, "Git");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.linux);
        hackerSkill = new Skill(icon, "Linux");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.photoshop);
        hackerSkill = new Skill(icon, "Photoshop");
        hackerSkillsList.add(hackerSkill);

        icon.setImageResource(R.drawable.illustrator);
        hackerSkill = new Skill(icon, "Illustrator");
        hackerSkillsList.add(hackerSkill);

        return hackerSkillsList;
    }
}