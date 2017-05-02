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
        return super.onStartCommand(intent, flags, startId);
    }

    //runs in a background thread.
    //the service gets an intent to start it.
    @Override
    protected void onHandleIntent(Intent intent) {
        Intent in = new Intent(BROADCAST_KEY);
        in.putExtra(SERVICE_DATA, "Hello from the service class: "+counter);
        in.putExtra(SERVICE_PROGRESS,counter);

        //Put your all data using put extra
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

        while (counter<5) {
            synchronized (this) {
                try {
                    wait(1000); //wait some seconds
                    counter++;
                    in = new Intent(BROADCAST_KEY);
                    in.putExtra(SERVICE_DATA, "Hello from the service class: "+counter);
                    in.putExtra(SERVICE_PROGRESS,counter);
                    //Put your all data using put extra
                    LocalBroadcastManager.getInstance(this).sendBroadcast(in);

                } catch (InterruptedException e) {
                   }
            }
        }
        //service is finished now
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
        showNotification("Service done",text);

    }

    public void showNotification(String title,String message)
    {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentText(message)
                .build();
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
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
