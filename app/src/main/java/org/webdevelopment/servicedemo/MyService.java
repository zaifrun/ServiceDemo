package org.webdevelopment.servicedemo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by makn on 06-02-2017.
 */

public class MyService extends IntentService {

    /* constant used for passing data to the Service */
    public static final String EXTRA_MESSAGE = "message";

    /* key for what the BroadCastReceiver */
    public static final String BROADCAST_KEY = "broadcastdata";

    //returning data from the service to the broadcast receiver */
    public static final String SERVICE_DATA = "servicedata";

    //progress information from Service to BroadCastReceiver */
    public static final String SERVICE_PROGRESS = "serviceProgress";

    private Handler handler;
    private int counter = 0;



    //needed to start the worker thread in the super class.
    public MyService()
    {
        super("MyService");
    }



    //runs BEFORE onHandleIntent on the MAIN thread
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        super.onStartCommand(intent, flags, startId);
        //the return statement says that we want Android
        //to restart the service if our app is killed.
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onCreate() {
        Log.d("MYSERVICE","Service CREATED");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("MYSERVICE","Service Destroyed");
        super.onDestroy();
    }

    //runs in a background thread.
    //the service gets an intent to start it.
    @Override
    protected void onHandleIntent(Intent intent) {
        //make a new intent to send to the activity
        Intent in = new Intent(BROADCAST_KEY);
        in.putExtra(SERVICE_DATA, "Hello from the service class: "+counter);
        in.putExtra(SERVICE_PROGRESS,counter);
        Log.d("MYSERVICE","count: "+counter);
        //Put your all data using put extra
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

        while (counter<5) {
            synchronized (this) {
                try {
                    wait(1000); //wait some seconds
                    counter++;
                    Log.d("MYSERVICE","count: "+counter);
                    in = new Intent(BROADCAST_KEY);
                    in.putExtra(SERVICE_DATA, "Hello from the service class: "+counter);
                    in.putExtra(SERVICE_PROGRESS,counter);
                    //update the activity by sending a broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                } catch (InterruptedException e) {
                    Log.d("MYSERVICE","INTERRUPTED EXCEPTION");
                   }
            }
        }
        //service is finishing now - send final notification
        //and show a toast.
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        if (text==null)
            text = "";
        if (text!=null) {
            if (text!="") showText(text);
            showNotification("Service done", text);
        }

    }

    //This shows a toast
    public void showNotification(String title,String message)
    {
        //build the notification using the builder.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  //icon
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentText(message)
                .build();

        //use the built in notification service
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //the 42 is just a ID number you choose.
        manager.notify(42,notification);

    }

    //runs in the background thread also.
    public void showText(final String message)
    {
        Log.d("from my service: ",message);
        handler.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }
}
