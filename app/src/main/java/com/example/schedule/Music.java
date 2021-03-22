package com.example.schedule;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class Music extends Service {
    SettingData settingData;
    MediaPlayer mediaPlayer;
    Ringtone ringtone;
    Vibrator v ;
    int start;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        start = intent.getIntExtra("start",1);
        Log.d("onStartCommand", "onStartCommand: "+start);

        if(start==1){
            settingData=intent.getExtras().getParcelable("settingData");
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+settingData.audioAlarmName);

            ringtone = RingtoneManager.getRingtone(this,uri);
            ringtone.setLooping(true);
            ringtone.play();


            if(settingData.alarmHaveVibration)
            {
                vibrate();

            }
        }
        else if(start==0){
            v.cancel();
            ringtone.stop();
        }




        return START_NOT_STICKY;
    }

    public void vibrate(){
        if(start==0)
            return;
        CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

                } else {
                    v.vibrate(500);
                }
            }
            @Override
            public void onFinish() {
                vibrate();
            }
        }.start();
    }

}
