package org.webdevelopment.servicedemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    LocalBroadcastManager broadcastManager;
    TextView textView;

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
                String str= intent.getStringExtra("service");
                System.out.println("on receive:"+str);

                textView.setText(str);
                //Get all your data from intent and do what you want
            }
        }
    };

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.startButton);
        textView = (TextView) findViewById(R.id.messageText);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(mMessageReceiver,new IntentFilter("message"));
        context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MyService.class);
                intent.putExtra(MyService.EXTRA_Message,"my message!");
                startService(intent);
            }
        });
    }
}
