package com.djkim.slap.messenger;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minglim on 11/27/15.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {
    @Override
    public void onPushOpen(Context context, Intent intent) {
        // Send a Parse Analytics "push opened" event
        ParseAnalytics.trackAppOpenedInBackground(intent);

        JSONObject pushData;
        String recipientID = "";
        String recipientName = "";
        try {
            pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            recipientID = pushData.getString("Recipient_ID");
            recipientName = pushData.getString("recipientName");
        } catch (JSONException e) {

        }

        Intent newIntent = new Intent(context, MessagingActivity.class);
        newIntent.putExtra("RECIPIENT_ID", recipientID);
        newIntent.putExtra("recipientName", recipientName);
        newIntent.putExtra("currentUserName", ParseUser.getCurrentUser().getUsername());
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);


    }
}
