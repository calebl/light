package com.bigndesign.light;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigndesign.light.adapters.Adapters.MessageAdapter;

import java.util.LinkedHashMap;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.Document;

/*
The messaging portion of this app adapted from https://github.com/sinch/android-messaging-tutorial
 */
public class AskActivity extends AppCompatActivity  implements MeteorCallback {
    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private ListView messagesList;
    private MessageAdapter messageAdapter;
    private String subscriptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instance of MessageAdapter to display messages
        messagesList = (ListView) findViewById(R.id.listMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);


        recipientId = "BgsfD5KSrr2jLKv6k"; //id for admin

        MeteorSingleton.getInstance().addCallback(this);
        subscriptionId = MeteorSingleton.getInstance().subscribe("conversation", new Object[]{recipientId});

        //Query all the messages, add messages to messageAdapter

        //Test IDs

        messageBodyField = (EditText) findViewById(R.id.messageBodyField);

        //listen for a click on the send button
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send the message
                messageBody = messageBodyField.getText().toString();
                if (messageBody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }

                MeteorSingleton.getInstance().call("messages.insert", new Object[]{messageBody, MeteorSingleton.getInstance().getUserId(), recipientId});
                messageBodyField.setText("");


            }
        });
    }

    //unbind the service when the activity is destroyed
    @Override
    public void onDestroy() {
        MeteorSingleton.getInstance().unsubscribe(subscriptionId);
        super.onDestroy();
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {
        Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        if(collectionName.equals("messages")){
            Log.d("AskActivity", documentID);

            Document message = MeteorSingleton.getInstance().getDatabase().getCollection("messages").getDocument(documentID);
            LinkedHashMap sender = (LinkedHashMap) message.getField("sender");
            LinkedHashMap recipient = (LinkedHashMap) message.getField("recipient");


            //if senderID matches this user, mark as outgoing and add to messagesAdapter
            if(sender.get("id").equals(MeteorSingleton.getInstance().getUserId())){
                messageAdapter.addMessage(message.getField("text").toString(), MessageAdapter.DIRECTION_OUTGOING);

            } else if (recipient.get("id").equals(MeteorSingleton.getInstance().getUserId())){
                //if recipientID matches this user, mark as incoming and add to messagesAdapter
                messageAdapter.addMessage(message.getField("text").toString(), MessageAdapter.DIRECTION_INCOMING);
            }


        }
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {

    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {

    }
}
