package lam.fooapp;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import lam.fooapp.communication.Communicator;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;
import lam.fooapp.services.MangoService;

public class MangoApplication extends Application {

    public static NumberFormat format = NumberFormat.getInstance();
    public static DateTimeFormatter formatter;// = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Communicator communicator;// = new Communicator();
    public static OrderForm currentOrderForm = null;
    public static ArrayList<Food> currentSelectedFoods = new ArrayList<>();
    public static ArrayList<Food> foods;
    public static Integer selectedTableNo = 0; //pult id
    public static int[]orderStateColors;
    public MangoService mangoService;
    Intent mangoServiceIntent;

    @Override
    public void onCreate() {
        System.out.println("Starting create App");
        communicator=new Communicator();
       /* if(!communicator.initSocketIO()) {
            System.out.println("can not create new socket");
            return;
        }*/
        super.onCreate();
        createNotificationChannel();
        mangoService = new MangoService(getApplicationContext());
        mangoServiceIntent = new Intent(getApplicationContext(),mangoService.getClass());
        if(!isMyServiceRunning(MangoService.class))startService(mangoServiceIntent);
        System.out.println("App created");
        orderStateColors = getApplicationContext().getResources().getIntArray(R.array.order_state_colors);

    }
    public static Double parser(String value)
    {
        try {
            Number number = format.parse(value);
            return number.doubleValue();
        }
        catch (Exception e){
            return null;
        }
    }
    public static int getOrderStateColor(Order.OrderState orderState) {
        if(orderState==null) return orderStateColors[0];
        return orderStateColors[orderState.ordinal()+1];
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MangoService.NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            System.out.println("Notification channel created");
        }
    }

 /*   public void sendNotification(){
        Intent intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, MangoService.NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }*/
    @Override
    public void onTerminate() {
        System.out.println("Appliction mango terminated");
        stopService(mangoServiceIntent);
        super.onTerminate();
    }
}
