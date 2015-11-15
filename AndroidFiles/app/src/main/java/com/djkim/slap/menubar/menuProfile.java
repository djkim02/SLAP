package com.djkim.slap.menubar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djkim.slap.R;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Calvin on 10/27/15.
 */
public class menuProfile extends Fragment {

    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu_profile, container, false);
        User currUser = Utils.get_current_user();
        TextView nameTextView = (TextView) rootview.findViewById(R.id.profile_name);
        nameTextView.setText(currUser.get_name());
        Profile profile = Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) rootview.findViewById(R.id.profile_pic);
        profilePictureView.setProfileId(profile.getId());


        return rootview;
    }
}
