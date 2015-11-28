package com.djkim.slap.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.parse.FindCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import com.parse.ParseException;

/**
 * Created by kylemn on 11/17/15.
 */
public class MessagingActivity extends Activity {
    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private String recipientName;
    private String currentUserName;
    public static final String RECIPIENT = "recipientName";
    public static final String CURRENTUSER = "currentUserName";
    public static final String TIMESTAMP = "timeStamp";
    private ListView messagesList;
    private MessageAdapter messageAdapter;

    public static final String DATEFORMAT = "EEE, MMM dd h:mm a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_activity);
        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);
        //get recipientId from the intent
        Intent intent = getIntent();
        recipientId = intent.getStringExtra("RECIPIENT_ID");
        recipientName = intent.getStringExtra(RECIPIENT);
        currentUserName = intent.getStringExtra(CURRENTUSER);
        currentUserId = ParseUser.getCurrentUser().getObjectId();

        messageBodyField = (EditText) findViewById(R.id.txtTextBody);

        messagesList = (ListView) findViewById(R.id.lstMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);

        TextView recipientNameField = (TextView) findViewById(R.id.relRecipientText);
        recipientNameField.setText(recipientName);

        //Todo: put into its own function
        String[] userIds = {currentUserId, recipientId};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
        query.whereContainedIn("senderId", Arrays.asList(userIds));
        query.whereContainedIn("recipientId", Arrays.asList(userIds));
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());

                        //Get object created at, convert it to a readable format for date
                        Date createdAt = messageList.get(i).getCreatedAt();
                        SimpleDateFormat newFormat = new SimpleDateFormat(DATEFORMAT);

                        try {
                            String parsedTimeStamp = newFormat.format(createdAt);
                            message.addHeader(TIMESTAMP, parsedTimeStamp);
                        } catch (Exception err) {
                            ;
                        }

                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
                            message.addHeader(RECIPIENT, currentUserName);
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            message.addHeader(RECIPIENT, recipientName);
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });

        //listen for a click on the send button
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBody = messageBodyField.getText().toString();
                if (messageBody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                messageService.sendMessage(recipientId, messageBody);
                messageBodyField.setText("");
            }
        });
    }
    //unbind the service when the activity is destroyed
    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        messageService.removeMessageClientListener(messageClientListener);
        super.onDestroy();
    }
    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.addMessageClientListener(messageClientListener);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {
        //Notify the user if their message failed to send
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            //Display an incoming message
            if (message.getSenderId().equals(recipientId)) {
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

                //Parse the headers and stuff here too
                writableMessage.addHeader(RECIPIENT, recipientName);

                SimpleDateFormat newFormat = new SimpleDateFormat(DATEFORMAT);
                try {
                    Date createdAt = new Date();
                    String parsedTimeStamp = newFormat.format(createdAt);
                    writableMessage.addHeader(TIMESTAMP, parsedTimeStamp);
                } catch (Exception err) {
                    ;
                }

                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
            }
        }
        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {
            //Display the message that was just sent
            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            //only add message to parse database if it doesn't already exist there

            //Todo: Integrate this with parse backend utility perhaps
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
            query.whereEqualTo("sinchId", message.getMessageId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                            ParseObject parseMessage = new ParseObject("ParseMessage");
                            parseMessage.put("senderId", currentUserId);
                            parseMessage.put("recipientId", writableMessage.getRecipientIds().get(0));
                            parseMessage.put("messageText", writableMessage.getTextBody());
                            parseMessage.put("sinchId", writableMessage.getMessageId());
                            parseMessage.saveInBackground();

                            //Parse the headers and stuff here three
                            writableMessage.addHeader(RECIPIENT, currentUserName);

                            SimpleDateFormat newFormat = new SimpleDateFormat(DATEFORMAT);
                            try {
                                Date createdAt = new Date();
                                String parsedTimeStamp = newFormat.format(createdAt);
                                writableMessage.addHeader(TIMESTAMP, parsedTimeStamp);
                            } catch (Exception err) {
                                ;
                            }

                            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
                        }
                    }
                }
            });
        }
        //Do you want to notify your user when the message is delivered?
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}
        //Don't worry about this right now
        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

            ParseQuery userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("objectId", writableMessage.getRecipientIds().get(0));

            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereMatchesQuery("user", userQuery);

            JSONObject data = new JSONObject();
            try {
                data.put("alert", ParseUser.getCurrentUser().getUsername()+" sent you a message");
                data.put("recipientName", ParseUser.getCurrentUser().getUsername());
                data.put("Recipient_ID", ParseUser.getCurrentUser().getObjectId());
            }
            catch (JSONException j)
            {

            }


// Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            //push.setMessage(ParseUser.getCurrentUser().getUsername()+" sent you a message");
            push.setData(data);
            push.sendInBackground(new SendCallback() {
                @Override
                public void done(com.parse.ParseException e) {

                    if(e==null){
//the push is sent!
                    }else{

//some problem occured. Analyze what is happening
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}