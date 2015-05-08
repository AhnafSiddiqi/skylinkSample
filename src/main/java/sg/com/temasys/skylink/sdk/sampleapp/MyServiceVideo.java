package sg.com.temasys.skylink.sdk.sampleapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.temasys.skylink.sampleapp.R;

import java.util.Date;

import sg.com.temasys.skylink.sdk.config.SkylinkConfig;
import sg.com.temasys.skylink.sdk.listener.LifeCycleListener;
import sg.com.temasys.skylink.sdk.listener.MediaListener;
import sg.com.temasys.skylink.sdk.listener.MessagesListener;
import sg.com.temasys.skylink.sdk.listener.RemotePeerListener;
import sg.com.temasys.skylink.sdk.rtc.SkylinkConnection;

/**
 * Created by Peh on 5/7/2015.
 */
public class MyServiceVideo extends Service implements LifeCycleListener, MediaListener, RemotePeerListener, MessagesListener {
    private static final String TAG = VideoCallFragment.class.getCanonicalName();
    public static final String MY_USER_NAME = "videoCallUser";
    public static final String ROOM_NAME = "videoRoom";
    private static final String ARG_SECTION_NUMBER = "section_number";
    //set height width for self-video when in call
    public static final int WIDTH = 350;
    public static final int HEIGHT = 350;
    private LinearLayout parentFragment;
    private Button toggleAudioButton;
    private Button toggleVideoButton;
    private Button btnEnterRoom;
    private EditText etRoomName;
    private SkylinkConnection skylinkConnection;
    private String peerId;
    private ViewGroup.LayoutParams selfLayoutParams;
    private boolean audioMuted;
    private boolean videoMuted;
    private boolean connected;
    private AudioRouter audioRouter;
    private String peerName;
    private TextView tvRoomDetails;
    private String remotePeerId;

    @Override
    public void onCreate() {
        super.onCreate();
        // Allow volume to be controlled using volume keys
        //this.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        Log.d("Service","onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service","onStart");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        String roomName = "bob";


        String apiKey = getString(R.string.app_key);
        String apiSecret = getString(R.string.app_secret);

        // Initialize the skylink connection
        initializeSkylinkConnection();

        // Initialize the audio router
        initializeAudioRouter();

        // Obtaining the Skylink connection string done locally
        // In a production environment the connection string should be given
        // by an entity external to the App, such as an App server that holds the Skylink API secret
        // In order to avoid keeping the API secret within the application
        String skylinkConnectionString = Utils.
                getSkylinkConnectionString(roomName, apiKey,
                        apiSecret, new Date(), SkylinkConnection.DEFAULT_DURATION);

        skylinkConnection.connectToRoom(skylinkConnectionString,
                MY_USER_NAME);
        Log.d("Service","ConnectToRoom");
        // Use the Audio router to switch between headphone and headset
        audioRouter.startAudioRouting(this.getApplicationContext());
        connected = true;

        return START_STICKY;
    }
    private void initializeAudioRouter() {
        if (audioRouter == null) {
            audioRouter = AudioRouter.getInstance();
            audioRouter.init(((AudioManager) this.
                    getSystemService(android.content.Context.AUDIO_SERVICE)));
        }
    }

    private void initializeSkylinkConnection() {
        if (skylinkConnection == null) {
            skylinkConnection = SkylinkConnection.getInstance();
            //the app_key and app_secret is obtained from the temasys developer console.
            skylinkConnection.init(getString(R.string.app_key),
                    getSkylinkConfig(), this.getApplicationContext());
            //set listeners to receive callbacks when events are triggered
            skylinkConnection.setLifeCycleListener(this);
            skylinkConnection.setMediaListener(this);
            skylinkConnection.setRemotePeerListener(this);
        }
    }

    private SkylinkConfig getSkylinkConfig() {
        SkylinkConfig config = new SkylinkConfig();
        //AudioVideo config options can be NO_AUDIO_NO_VIDEO, AUDIO_ONLY, VIDEO_ONLY, AUDIO_AND_VIDEO;
        config.setAudioVideoSendConfig(SkylinkConfig.AudioVideoConfig.AUDIO_AND_VIDEO);
        config.setHasPeerMessaging(true);
        config.setHasFileTransfer(true);
        config.setTimeout(Constants.TIME_OUT);
        return config;
    }





    /***
     * Lifecycle Listener Callbacks -- triggered during events that happen during the SDK's lifecycle
     */

    /**
     * Triggered when connection is successful
     *
     * @param isSuccess
     * @param message
     */

    @Override
    public void onConnect(boolean isSuccess, String message) {
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

        Toast.makeText(this, "Warning is errorCode" + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect(int errorCode, String message) {
        Log.d(TAG, message + " disconnected");
        Toast.makeText(this, "onDisconnect " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveLog(String message) {
        Log.d(TAG, message + " on receive log");
    }

    /**
     * Media Listeners Callbacks - triggered when receiving changes to Media Stream from the remote peer
     */

    /**
     * Triggered after the user's local media is captured.
     *
     * @param videoView
     * @param size
     */
    @Override
    public void onLocalMediaCapture(GLSurfaceView videoView, Point size) {

    }

    @Override
    public void onVideoSizeChange(GLSurfaceView videoView, Point size) {
        Log.d(TAG, videoView + " got size");
    }

    @Override
    public void onRemotePeerAudioToggle(String remotePeerId, boolean isMuted) {
        String message = null;
        if (isMuted) {
            message = "Your peer muted their audio";
        } else {
            message = "Your peer unmuted their audio";
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemotePeerVideoToggle(String peerId, boolean isMuted) {
        String message = null;
        if (isMuted)
            message = "Your peer muted video";
        else
            message = "Your peer unmuted their video";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Remote Peer Listener Callbacks - triggered during events that happen when data or connection
     * with remote peer changes
     */

    @Override
    public void onRemotePeerJoin(String remotePeerId, Object userData, boolean hasDataChannel) {
        Toast.makeText(this, "Your peer has just connected", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Notification")
                        .setContentText("A client is calling");

        Intent resultIntent = new Intent(this, MainActivity.class);
        String strName = "No";
        resultIntent.putExtra("Msg",strName);
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
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.remotePeerId != null) {
            Toast.makeText(getActivity(), "Rejected third peer from joining conversation",
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
    public void onRemotePeerMediaReceive(String remotePeerId, GLSurfaceView videoView, Point size) {

    }

    @Override
    public void onRemotePeerLeave(String remotePeerId, String message) {
        Toast.makeText(this, "Your peer has left the room", Toast.LENGTH_SHORT).show();
        if (remotePeerId != null && remotePeerId.equals(this.peerId)) {
            this.peerId = null;
            View peerView = parentFragment.findViewWithTag("peer");
            parentFragment.removeView(peerView);

            // Resize self view to original size
            if (this.selfLayoutParams != null) {
                View self = parentFragment.findViewWithTag("self");
                self.setLayoutParams(selfLayoutParams);
            }
        }
    }

    @Override
    public void onRemotePeerUserDataReceive(String remotePeerId, Object userData) {
        Log.d(TAG, "onRemotePeerUserDataReceive " + remotePeerId);
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
        Log.d("Service","onServerMessageReceive");
        Toast.makeText(this, "Message Received", Toast.LENGTH_SHORT).show();
        String chatPrefix = "";
        //add prefix if the chat is a private chat - not seen by other users.
        if (message instanceof String) {
            Toast.makeText(this, (String) message, Toast.LENGTH_SHORT).show();
            if (message.equals("/joincall")) {
                Toast.makeText(this, "Client knocking", Toast.LENGTH_SHORT).show();
                skylinkConnection.disconnectFromRoom();
                skylinkConnection.setLifeCycleListener(null);
                skylinkConnection.setMediaListener(null);
                skylinkConnection.setRemotePeerListener(null);
                connected = false;
                audioRouter.stopAudioRouting(this.getApplicationContext());
            }
        }


    }

    @Override
    public void onP2PMessageReceive(String remotePeerId, Object message, boolean isPrivate) {
        //add message to listview and update ui
        Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
        if (message instanceof String) {
            Log.d("P2P",(String)message);
            Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
            if(message.equals("/joincall")){
                Toast.makeText(this, "Client knocking", Toast.LENGTH_SHORT).show();
            }
            //chatMessageCollection.add(this.peerName + " : " + chatPrefix + message);
            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }
}
