package com.example.magdakowalska.ae2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by magdakowalska on 19/02/15.
 */
public class MusicService extends Service implements OnCompletionListener, OnPreparedListener,
        OnSeekCompleteListener{

    private MediaPlayer servicePlayer = new MediaPlayer();
    private static final int NOTIFICATION_ID = 0;
    private Notification notification;
    private NotificationManager notificationManager;
    private boolean isPlaying = false;

    @Override
    public void onCreate() {
        startService(new Intent(this, MusicService.class));
        servicePlayer.reset();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int started){
        initNotification();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(servicePlayer != null){
            if(isPlaying){
                servicePlayer.stop();
            }
            servicePlayer.release();
        }
        cancelNotification();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (!isPlaying){
            playMedia();
        }
    }

    public void playMedia(){
        if(!isPlaying){
            Toast.makeText(getApplicationContext(), "Background music player", Toast.LENGTH_SHORT).show();
            servicePlayer.start();
        }
    }

    public void stopMedia(){
        if(isPlaying){
            servicePlayer.stop();
        }
    }

    // Create Notification
    private void initNotification() {
        Toast.makeText(getApplicationContext(), "Background notification ON", Toast.LENGTH_SHORT).show();
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("PIANO")
                        .setContentText("Music playing!")
                        .setContentIntent(contentIntent)
                        .build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    // Cancel Notification
    private void cancelNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) getSystemService(ns);
        notificationManager.cancelAll();
    }
}
