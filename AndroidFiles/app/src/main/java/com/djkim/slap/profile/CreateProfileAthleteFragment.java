package com.djkim.slap.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.models.User;

/**
 * Created by dongjoonkim on 10/25/15.
 */
public class CreateProfileAthleteFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_profile_athlete_fragment_layout, container, false);
    }
}
