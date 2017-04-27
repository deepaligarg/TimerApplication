package com.example.deepali.timerapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.security.Provider;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    TextView timerTextView;
    Button b;
    long startTime = 0;
    Notification myNotication;



    Handler timerHandler = new Handler() {
        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord record) {

        }
    };
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerTextView.postDelayed(this,500);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        timerTextView = (TextView) findViewById(R.id.timerTextView);

        b = (Button) findViewById(R.id.b);

        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    b.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    b.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });

    }




    @Override
    public void onPause() {
        try {
            createNotification();
        } catch (Exception e) {
            Log.d("Nhi chala", "no notification");
        }
        super.onPause();
    }
    private void createNotification()
    {

        Log.i("enter","entered in func");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);

        Log.i("intent","intent");

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(MainActivity.this);

        builder.setAutoCancel(true);
        builder.setTicker("this is ticker text");
        //Log.i("ticker", "ticker");
        builder.setContentTitle("TimerApp Notification");
        //Log.i("title", "title");

        if(b.getText()=="stop") {
            builder.setContentText("The timer is running");

        }
            else
        {
            builder.setContentText("The timer has stopped");
            builder.setSubText(timerTextView.getText());
        }
       // Log.i("timerview", "timerview");
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);

        builder.build();
        myNotication = builder.getNotification();
        mNotificationManager.notify(11, myNotication);
    }


   @Override
    public void onResume()
    {


        super.onResume();
    }

    @Override
    public void onDestroy() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(11);
        super.onDestroy();
    }

       
}
