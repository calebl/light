package com.bigndesign.light;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigndesign.light.adapters.Adapters.MessageAdapter;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

/*
The messaging portion of this app adapted from https://github.com/sinch/android-messaging-tutorial
 */
public class AskActivity extends AppCompatActivity {
    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private ListView messagesList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        //Instance of MessageAdapter to display messages
        messagesList = (ListView) findViewById(R.id.listMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);

        //Query all the messages, add messages to messageAdapter
        Realm realm = Realm.getDefaultInstance();
        List<com.bigndesign.light.Model.Message> listOfMessages = realm.where(com.bigndesign.light.Model.Message.class).findAllSorted("id");
        for (int i = 0; i < listOfMessages.size(); i++) {
            WritableMessage message = new WritableMessage("mustHaveNonEmptyUserId", listOfMessages.get(i).getMessageText());
            if(listOfMessages.get(i).getDirection() == 1){
                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
            } else {
                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
            }
        }

        //Test IDs
        //recipientId = "gjab152ub4rve0q5uk08dqu9rk"; //id for S7
        recipientId = "pr9edrj97etmh25u5b34i8k68t"; //id for S5

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
            Toast.makeText(AskActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
        }


        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            //Display an incoming message
            if (message.getSenderId().equals(recipientId)) {
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);

                //Save message to DB
                saveMessage(message, MessageAdapter.DIRECTION_INCOMING);
            }
        }

        @Override
        public void onMessageSent(MessageClient client, final Message message, String recipientId) {
            //Display the message that was just sent
            WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

            //Save message to DB
            saveMessage(message, MessageAdapter.DIRECTION_OUTGOING);
        }

        public void saveMessage(final Message message, final int direction){
            //Save outgoing message to DB
            Realm realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try{
                        com.bigndesign.light.Model.Message outgoingMessage = new com.bigndesign.light.Model.Message();

                        outgoingMessage.setId(getNextMessageKey(realm));
                        outgoingMessage.setMessageText(message.getTextBody());
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                        outgoingMessage.setTimeStamp(timeStamp);
                        outgoingMessage.setDirection(direction);

                        realm.copyToRealm(outgoingMessage);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        public int getNextMessageKey(Realm realm)
        {
            int key;
            try {
                key = realm.where(com.bigndesign.light.Model.Message.class).max("id").intValue() + 1;
            } catch(ArrayIndexOutOfBoundsException ex) {
                key = 0; // when there is no object in the database yet
            }
            return key;
        }

        //Do you want to notify your user when the message is delivered?
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        //Don't worry about this right now
        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }
}
