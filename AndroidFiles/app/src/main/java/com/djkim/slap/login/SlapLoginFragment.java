package com.djkim.slap.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djkim.slap.R;

/**
 * Created by dongjoonkim on 10/8/15.
 */
public class SlapLoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slap_login_fragment_layout, container, false);
    }
}
