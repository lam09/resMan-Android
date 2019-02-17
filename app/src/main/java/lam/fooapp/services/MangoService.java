package lam.fooapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.Utils.Constant;
import lam.fooapp.activity.WaiterActivity;
import lam.fooapp.communication.FoodApi;
import lam.fooapp.communication.SpringFoodApi;
import lam.fooapp.communication.socketio.CustomListener;
import lam.fooapp.model.EventData;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.app.Notification.VISIBILITY_PUBLIC;

public class MangoService extends Service {

    private static int NOTIF_ID = 1;
    public static final String NOTIF_CHANNEL_ID = "Channel_Id";

    Context context;
    private Handler handler;
    private Runner runner;

    public Socket socketio;
    public Manager socketManager;
    public String clientId;

    int counter=0;

    public MangoService(){}
    public MangoService(Context applicationContext){
        super();
        context=applicationContext;
        System.out.println("created mango service");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runner);
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
       Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
       handler = new Handler();
       runner = new Runner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runner);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.i("EXIT", "ondestroy!");

        Intent broadcastIntent = new Intent("lam.fooapp.services.SensorRestarterBroadcastReceiver");
        sendBroadcast(broadcastIntent);

    }
    public void sendNotification(String text){

        Intent snoozeIntent = new Intent(getApplicationContext(), NewOrderBroadcastReceiver.class);
       // snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =     PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, 0);
        Uri newOrderSound = RingtoneManager.getDefaultUri(R.raw.win_line_01);
        Uri sound = Uri.parse("android.resource://"+"raw/"+R.raw.button);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,NOTIF_CHANNEL_ID )
                .setSmallIcon(R.drawable.ic_fiber_new_white_24dp)
                .setContentTitle("New Order")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(""))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(sound)
                .addAction(R.drawable.cancel, getString(R.string.app_name), snoozePendingIntent)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(++NOTIF_ID, mBuilder.build());
    }



    public boolean initSocketIO() {
        try {
            System.out.println("Initializing socket io");
            IO.Options opt = new IO.Options();
            opt.forceNew = true;
            opt.reconnection = true;
            opt.reconnectionDelay = 1000;
            opt.reconnectionDelayMax = 5000;
            opt.reconnectionAttempts = 9999999;
            socketio = IO.socket(Constant.SERVER_URL, opt);
            System.out.println("connect to " + Constant.SERVER_URL);
            socketManager = socketio.io();
            if (socketio != null) {
                System.out.println("created socket io");
                socketio.on(Socket.EVENT_CONNECT, handleOnNewConnectionCreated);
                socketio.on(Socket.EVENT_CONNECT_ERROR, handleOnConnectionError);
                socketio.on(Socket.EVENT_DISCONNECT, handleOnDisconnection);

                socketio.on("new-order", handleOnNewOrder);
                socketio.connect();
            }
        } catch (URISyntaxException e) {
            socketio=null;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    CustomListener<EventData> handleOnNewConnectionCreated = new CustomListener<EventData>(EventData.class) {
        @Override
        public void call(Object... args) {
            // System
            System.out.println("connected to socket io online server!!!!!!!!!!!!!!!!!!!");
        }
    };
    Emitter.Listener handleOnConnectionError = new Emitter.Listener() {
        public void call(Object... objects) {

            //Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();

        }
    };
    Emitter.Listener handleOnDisconnection = new Emitter.Listener() {
        public void call(Object... objects) {
            System.out.println("client disconnected");
        }
    };
    Emitter.Listener handleOnNewOrder = new Emitter.Listener() {
        public void call(Object... objects) {
            System.out.println("New order received");
            StringBuilder builder = new StringBuilder();
            builder.append("");
            sendNotification(objects[0].toString());
        }
    };

    public class Runner implements Runnable {

        int i = 0;

        @Override
        public void run() {
            i++;
            if(socketio==null)initSocketIO();
            System.out.println(i + " Keep running");
            handler.postDelayed(this, 1000 * 5);
        }

    }
}