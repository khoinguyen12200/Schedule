package com.example.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Date;

import static com.example.schedule.R.drawable.app_icon;
import static com.example.schedule.R.drawable.ic_app_icon;
import static com.example.schedule.R.drawable.whitealarm;

public class RingtonePlayingService extends Service {
    Ringtone r ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        NotificationManager notificationManager =(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(SettingData.CHANNEL_ALARM);
        r = RingtoneManager.getRingtone(getBaseContext(),channel.getSound());
        if(channel.getImportance()==0){
            NotificationChannel mChannel = new NotificationChannel(SettingData.CHANNEL_ALARM, getResources().getString(R.string.alarm), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            r = RingtoneManager.getRingtone(getBaseContext(),mChannel.getSound());

        }
        r.setLooping(true);
        r.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        r.stop();
        super.onDestroy();
    }
}
