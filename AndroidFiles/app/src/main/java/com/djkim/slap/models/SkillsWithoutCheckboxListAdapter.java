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
public class SkillsWithoutCheckboxListAdapter extends ArrayAdapter<SkillWithoutCheckbox> {
    private ArrayList<SkillWithoutCheckbox> skillsList;
    private Context context;

    public SkillsWithoutCheckboxListAdapter(Context context, int textViewResourceId, ArrayList<SkillWithoutCheckbox> skillsList) {
        super(context, textViewResourceId, skillsList);
        this.context = context;
        this.skillsList = new ArrayList<>();
        this.skillsList.addAll(skillsList);
    }

    private class ViewHolder {
        ImageView icon;
        TextView skillName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.skills_without_checkbox, null);

            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.skill_without_checkbox_icon);
            holder.skillName = (TextView) convertView.findViewById(R.id.skill_without_checkbox_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SkillWithoutCheckbox skill = skillsList.get(position);
        holder.icon.setImageResource(skill.getImageId());
        holder.skillName.setText(skill.getName());
        return convertView;
    }
}
