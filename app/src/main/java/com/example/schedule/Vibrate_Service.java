package com.example.schedule;

import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;

public class Vibrate_Service extends Service {
    Vibrator v;
    boolean looping = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        loop();
        return START_NOT_STICKY;
    }

    public void loop(){
        if(!looping)
            return;
        CountDownTimer countDownTimer =new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                v.vibrate(500);
            }

            @Override
            public void onFinish() {
                loop();
            }
        }.start();
    }


    @Override
    public void onDestroy()
    {
        looping=false;
        v.cancel();
    }
}
