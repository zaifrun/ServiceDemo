package org.webdevelopment.servicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by makn on 06-02-2017.
 */

public class MyService extends IntentService {

    public static final String EXTRA_Message = "message";
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
    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(1000); //wait 5 seconds
            } catch (InterruptedException e)
            {

            }
        }
        String text = intent.getStringExtra(EXTRA_Message);
        showText(text);
        Intent in = new Intent("message");
        in.putExtra("service", "Hello from the service class: "+counter);
        counter++;
        //Put your all data using put extra

        LocalBroadcastManager.getInstance(this).sendBroadcast(in);

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
