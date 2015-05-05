package sg.com.temasys.skylink.sdk.sampleapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.temasys.skylink.sampleapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.com.temasys.skylink.sdk.config.SkylinkConfig;
import sg.com.temasys.skylink.sdk.listener.LifeCycleListener;
import sg.com.temasys.skylink.sdk.listener.MessagesListener;
import sg.com.temasys.skylink.sdk.listener.RemotePeerListener;
import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection;
import sg.com.temasys.skylink.sdk.rtc.SkylinkException;

/**
 * Created by augustHouse on 5/5/2015.
 */
public class MyService extends Service implements LifeCycleListener, RemotePeerListener, MessagesListener {

    private static final String TAG = ChatFragment.class.getCanonicalName();
    public static final String ROOM_NAME = "chatRoom";
    public static final String MY_USER_NAME = "chatRoomUser";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String remotePeerId;
    private Button btnSendPrivateServerMessage;
    private Button btnSendP2PPublicMessage;
    private Button btnSendPublicServerMessage;
    private ListView listViewChats;
    private TextView tvRoomDetails;
    private SkylinkConnection skylinkConnection;
    private BaseAdapter adapter;
    private List<String> chatMessageCollection;
    private String peerName;
    private Button btnSendP2PMessage;
    private boolean connected;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        //initialize views
//        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
//        listViewChats = (ListView) rootView.findViewById(R.id.lv_messages);
//        btnSendPrivateServerMessage = (Button) rootView.findViewById(R.id.btn_send_server_message);
//        btnSendPublicServerMessage = (Button) rootView.findViewById(R.id.btn_send_public_server_message);
//        btnSendP2PMessage = (Button) rootView.findViewById(R.id.btn_send_private_chat);
//        btnSendP2PPublicMessage = (Button) rootView.findViewById(R.id.btn_send_p2p_public_message);
//        tvRoomDetails = (TextView) rootView.findViewById(R.id.tv_room_details);
//        chatMessageCollection = new ArrayList();
//
//        /** Defining the ArrayAdapter to set items to ListView */
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatMessageCollection);
//
//        /** Defining a click event listener for the button "Send Private Server Message" */
//        btnSendPrivateServerMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Add chat message to the listview
//                String message = addMessageToListView(true);
//
//                //pass null for remotePeerId to send message to send mesage to all users in the room
//                //sends message using the signalling server
//                skylinkConnection.sendServerMessage(remotePeerId, message);
//
//            }
//        });

        /** Defining a click event listener for the button "Send Public Server Message" */
//        btnSendPublicServerMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Add chat message to the listview
//                String message = addMessageToListView(false);
//
//                //pass remotePeerId instead of null to send message to specific peer
//                //sends message using the signalling server
//                skylinkConnection.sendServerMessage(null, message);
//
//                adapter.notifyDataSetChanged();
//            }
//        });



//        /** Defining a click event listener for the button "Send Private Message" */
//        btnSendP2PMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (remotePeerId == null) {
//                    Toast.makeText(getApplicationContext(), "There is no peer in the room to send a private message to", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //Add chat message to the listview
//                String message = addMessageToListView(true);
//
//                try {
//                    //sends p2p message using the datachannel to the specific user
//                    skylinkConnection.sendP2PMessage(remotePeerId, message);
//                } catch (SkylinkException e) {
//                    Log.e(TAG, e.getMessage(), e);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//        });

        /** Defining a click event listener for the button "Send Public P2P Message" */
//        btnSendP2PPublicMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (remotePeerId == null) {
//                    Toast.makeText(getApplicationContext(),"There is no peer in the room to send a private message to", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                //Add chat message to the listview
//                String message = addMessageToListView(false);
//
//                try {
//                    //sends p2p message using the datachannel to the all users
//                    skylinkConnection.sendP2PMessage(null, message);
//                } catch (SkylinkException e) {
//                    Log.e(TAG, e.getMessage(), e);
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//        });

        /** Setting the adapter to the ListView */
//        listViewChats.setAdapter(adapter);
//
//        return rootView;
//    }

    /**
     * Retrives message written in edit text and adds it to the chatlistview
     *
     * @param isPrivateMessage
     * @return message that was added to the listview
     */
//    private String addMessageToListView(boolean isPrivateMessage) {
//           return "hello";
//    }

//    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","ServiceCreated");

//        String apiKey = getString(R.string.app_key);
//        String apiSecret = getString(R.string.app_secret);
//
//        // Initialize the skylink connection
//        initializeSkylinkConnection();
//
//        // Obtaining the Skylink connection string done locally
//        // In a production environment the connection string should be given
//        // by an entity external to the App, such as an App server that holds the Skylink API secret
//        // In order to avoid keeping the API secret within the application
//        String skylinkConnectionString = Utils.
//                getSkylinkConnectionString(ROOM_NAME, apiKey,
//                        apiSecret, new Date(), SkylinkConnection.DEFAULT_DURATION);
//
//        skylinkConnection.connectToRoom(skylinkConnectionString,
//                MY_USER_NAME);
//
//        connected = true;

//        ((MainActivity) getApplicationContext()).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

//    @Override
    public int onStartCommand(){
        Log.d("Service","OnStartCommand");
        String apiKey = getString(R.string.app_key);
        String apiSecret = getString(R.string.app_secret);

        // Initialize the skylink connection
        initializeSkylinkConnection();

        // Obtaining the Skylink connection string done locally
        // In a production environment the connection string should be given
        // by an entity external to the App, such as an App server that holds the Skylink API secret
        // In order to avoid keeping the API secret within the application
        String skylinkConnectionString = Utils.
                getSkylinkConnectionString(ROOM_NAME, apiKey,
                        apiSecret, new Date(), SkylinkConnection.DEFAULT_DURATION);

        skylinkConnection.connectToRoom(skylinkConnectionString,
                MY_USER_NAME);

        connected = true;

        return START_STICKY;
    }

    private void initializeSkylinkConnection() {
        if (skylinkConnection == null) {
            skylinkConnection = SkylinkConnection.getInstance();
            //the app_key and app_secret is obtained from the temasys developer console.
            skylinkConnection.init(getString(R.string.app_key),
                    getSkylinkConfig(), this.getApplicationContext());
            //set listeners to receive callbacks when events are triggered
            skylinkConnection.setLifeCycleListener(this);
            skylinkConnection.setMessagesListener(this);
            skylinkConnection.setRemotePeerListener(this);
        }
    }

    private SkylinkConfig getSkylinkConfig() {
        SkylinkConfig config = new SkylinkConfig();
        // AudioVideo config options can be NO_AUDIO_NO_VIDEO, AUDIO_ONLY, VIDEO_ONLY, AUDIO_AND_VIDEO;
        config.setAudioVideoSendConfig(SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO);
        config.setHasPeerMessaging(true);
        config.setHasFileTransfer(true);
        config.setTimeout(Constants.TIME_OUT);
        return config;
    }

    /***
     * Lifecycle Listener Callbacks -- triggered during events that happen during the SDK's lifecycle
     */

    /**
     * Triggered if the connection is successful
     *
     * @param isSuccess
     * @param message
     */

    @Override
    public void onConnect(boolean isSuccess, String message) {
        //update textview if connection is successful
        if (isSuccess) {
            Utils.setRoomDetails(false, tvRoomDetails, this.peerName, ROOM_NAME, MY_USER_NAME);
        } else {
            Toast.makeText(this, "Skylink Connection Failed\nReason : "
                    + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLockRoomStatusChange(String remotePeerId, boolean lockStatus) {
        Toast.makeText(this, "Peer " + remotePeerId +
                " has changed Room locked status to " + lockStatus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWarning(int errorCode, String message) {
        Log.d(TAG, message + "warning");
    }

    @Override
    public void onDisconnect(int errorCode, String message) {
        Log.d(TAG, message + " disconnected");
    }

    @Override
    public void onReceiveLog(String message) {
        Log.d(TAG, message + " on receive log");
    }

    /**
     * Remote Peer Listener Callbacks - triggered during events that happen when data or connection
     * with remote peer changes
     */

    @Override
    public void onRemotePeerJoin(String remotePeerId, Object userData, boolean hasDataChannel) {
        // If there is an existing peer, prevent new remotePeer from joining call.
        if (this.remotePeerId != null) {
            Toast.makeText(this, "Rejected third peer from joining conversation",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //if first remote peer to join room, keep track of user and update text-view to display details
        this.remotePeerId = remotePeerId;
        if (userData instanceof String) {
            this.peerName = (String) userData;
            Utils.setRoomDetails(true, tvRoomDetails, this.peerName, ROOM_NAME, MY_USER_NAME);
        }
    }

    @Override
    public void onRemotePeerUserDataReceive(String remotePeerId, Object userData) {
        Toast.makeText(this, "Getting user data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemotePeerLeave(String remotePeerId, String message) {
        Toast.makeText(this, "Your peer has left the room", Toast.LENGTH_SHORT).show();
        //reset peerId
        this.remotePeerId = null;
        this.peerName = null;
        //update textview to show room status
        Utils.setRoomDetails(false, tvRoomDetails, this.peerName, ROOM_NAME, MY_USER_NAME);
    }

    @Override
    public void onOpenDataConnection(String peerId) {
        Log.d(TAG, "onOpenDataConnection");
    }

    /**
     * Message Listener Callbacks - triggered during events that happen when messages are received
     * from remotePeer
     */

    @Override
    public void onServerMessageReceive(String remotePeerId, Object message, boolean isPrivate) {
        String chatPrefix = "";
        //add prefix if the chat is a private chat - not seen by other users.
        if (isPrivate) {
            chatPrefix = "<Private> ";
        }
        //add message to listview and update ui
        if (message instanceof String) {
            chatMessageCollection.add(this.peerName + " : " + chatPrefix + message);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onP2PMessageReceive(String remotePeerId, Object message, boolean isPrivate) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        Intent resultIntent = new Intent(this, MainActivity.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        //add prefix if the chat is a private chat - not seen by other users.
        String chatPrefix = "";
        if (isPrivate) {
            chatPrefix = "<Private> ";
        }
        //add message to listview and update ui
        if (message instanceof String) {
            chatMessageCollection.add(this.peerName + " : " + chatPrefix + message);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
