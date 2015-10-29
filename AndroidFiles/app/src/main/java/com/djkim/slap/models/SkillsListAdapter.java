package com.djkim.slap.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.djkim.slap.R;

import java.util.ArrayList;

/**
 * Created by dongjoonkim on 10/27/15.
 */
public class SkillsListAdapter extends ArrayAdapter<Skill> {
    private ArrayList<Skill> skillsList;
    private Context context;

    public SkillsListAdapter(Context context, int textViewResourceId, ArrayList<Skill> skillsList) {
        super(context, textViewResourceId, skillsList);
        this.context = context;
        this.skillsList = new ArrayList<>();
        this.skillsList.addAll(skillsList);
    }

    private class ViewHolder {
        ImageView icon;
        TextView skillName;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.skills_info, null);

            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.skill_icon);
            holder.skillName = (TextView) convertView.findViewById(R.id.skill_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Skill skill = (Skill) cb.getTag();
                    skill.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Skill skill = skillsList.get(position);
        holder.icon.setImageResource(skill.getImageId());
        holder.skillName.setText(skill.getName());
        holder.checkBox.setChecked(skill.isSelected());
        holder.checkBox.setTag(skill);

        return convertView;
    }
}
