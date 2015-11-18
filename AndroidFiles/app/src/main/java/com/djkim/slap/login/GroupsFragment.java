package com.djkim.slap.login;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djkim.slap.R;

/**
 * Created by dongjoonkim on 10/8/15.
 */
public class GroupsFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.groups_fragment_layout, container, false);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Bold.otf");
        TextView loginTextView = (TextView) view.findViewById(R.id.heading);
        loginTextView.setTypeface(tf);
        TextView subheadingTextView = (TextView) view.findViewById(R.id.subheading);
        tf = Typeface.createFromAsset(getContext().getAssets(), "BebasNeue Regular.otf");
        subheadingTextView.setTypeface(tf);
        return view;
    }
}
