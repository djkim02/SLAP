package com.djkim.slap.models;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by kylemn on 10/24/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "6EZmymlOoCjIFnPPnJ13XcpeyyoXNIVoXTq2RwMo", "rNsM6txwCaBsM1SU5iw64l2GUHCxQhXmhYMcePHK");
        ParseFacebookUtils.initialize(getApplicationContext());
    }
}
