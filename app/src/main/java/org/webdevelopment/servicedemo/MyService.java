package org.webdevelopment.servicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by makn on 06-02-2017.
 */

public class MyService extends IntentService {

    public static final String EXTRA_Message = "message";
    private Handler handler;



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
                wait(5000); //wait 5 seconds
            } catch (InterruptedException e)
            {

            }
        }
        String text = intent.getStringExtra(EXTRA_Message);
        showText(text);

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
