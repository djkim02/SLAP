package com.djkim.slap.login;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

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

        //TODO: remove l8r
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        ParseFacebookUtils.initialize(getApplicationContext());

    }
}