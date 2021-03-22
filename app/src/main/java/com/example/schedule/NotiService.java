package com.example.schedule;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;

import static com.example.schedule.R.drawable.app_icon;
import static com.example.schedule.R.drawable.ic_app_icon;
import static com.example.schedule.R.drawable.whitealarm;

public class NotiService extends Service {
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean showed =false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String audioNotiName="none";
        Boolean notiHaveVibration = false;
        audioNotiName = intent.getStringExtra("audioNotiName");
        notiHaveVibration = intent.getBooleanExtra("notiHaveVibration",false);
        Acti acti = intent.getParcelableExtra("acti");
        Interval interval = intent.getParcelableExtra("interval");

        if(showed)
            return START_NOT_STICKY;

        showed=true;

        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder ;
        NotificationManager notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(SettingData.CHANNEL_NOTIFICATION, getResources().getString(R.string.remind), NotificationManager.IMPORTANCE_HIGH);
            mChannel.setVibrationPattern(new long[]{500, 500, 500, 500});
            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), mChannel.getId());



        } else {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

        }
        notificationBuilder
                .setLights(Color.RED, 3000, 3000)
                .setSmallIcon(ic_app_icon)
                .setContentTitle(acti.name)
                    .setContentText(interval.timeString()+" - "+getResources().getString(R.string.at)+" "+interval.location)
                .setLights(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), 3000, 3000)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);


        if(notiHaveVibration){
            notificationBuilder.setVibrate(new long[]{600, 600, 600, 600});
            final Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));

            } else {
                v.vibrate(600);
            }
        }

        notificationManager.notify(0, notificationBuilder.build());

        return START_NOT_STICKY;
    }
}
