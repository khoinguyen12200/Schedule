package com.example.schedule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Alarm_Activity extends AppCompatActivity {

    String audioAlarmName="";
    Boolean alarmHaveVibration=false;
    Boolean cancel;
    Vibrator v ;
    SharedPreferences sharedPreferences;
    SettingData settingData;
    int start=0;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_);

        if(start==1)
            return;
        start=1;



        settingData = new SettingData();
        getdata();


        getSupportActionBar().hide();

        PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");
        wakeLock.acquire();
        wakeLock.release();

        DigitalClock clock = (DigitalClock) findViewById(R.id.clock);
        final TextView eventname =(TextView) findViewById(R.id.eventname);
        TextView time = (TextView) findViewById(R.id.time);
        final SwipeButton swipeButton = (SwipeButton) findViewById(R.id.button);
        ImageView dongholac = (ImageView) findViewById(R.id.dongholac);

        final Intent intent = getIntent();

        Acti acti = intent.getParcelableExtra("activity");
        final Interval interval = intent.getParcelableExtra("interval");



        final Calendar calenInter = Calendar.getInstance();
        calenInter.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),
                interval.timeToCalendar().get(Calendar.HOUR_OF_DAY),
                interval.timeToCalendar().get(Calendar.MINUTE),0);
        if(calenInter.get(Calendar.DAY_OF_WEEK) != interval.eventDay)
            calenInter.set(Calendar.DAY_OF_WEEK,-1);


        eventname.setText(acti.name);
        time.setText(interval.timeString());
        Animation lac = AnimationUtils.loadAnimation(this,R.anim.laclaclac);
        dongholac.startAnimation(lac);

        cancel = false;

        Intent startIntent = new Intent(this.getApplicationContext(), RingtonePlayingService.class);
        this.startService(startIntent);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                out();
            }
        });



        if(settingData.alarmHaveVibration)
        {
            Intent vibrate_service = new Intent(this, Vibrate_Service.class);
            this.startService(vibrate_service);
        }

    }
    public void out(){
        Intent stopIntent = new Intent(this, RingtonePlayingService.class);
        this.stopService(stopIntent);

        Intent vibrate_service = new Intent(this, Vibrate_Service.class);
        this.stopService(vibrate_service);
        finish();
    }

    @Override
    protected void onDestroy() {
        out();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        out();
        super.onBackPressed();
    }
    TimeTable timeTable;
    ArrayList acti;
    ArrayList intervals;
    float width;
    float height;
    int giobatdau=0,gioketthuc=0;
    int ngaybatdau=0,ngayketthuc=0;


    public void getdata(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("ALL_DATA",MODE_PRIVATE); // danh sach table + settingdata

        Gson gson1 = new Gson();
        String json1 = sharedPreferences.getString("settingData", "");
        if(!json1.equals("")){
            settingData=gson1.fromJson(json1,SettingData.class);
        }
        else settingData = new SettingData();


        Gson gson = new Gson();
        String json = sharedPreferences.getString("tableData", "");


        if(!json.equals("")){
            timeTable = gson.fromJson(json, TimeTable.class);
            acti=timeTable.actis;
            intervals=timeTable.intervals;
            giobatdau=timeTable.startTime;
            gioketthuc=timeTable.endTime;
            ngaybatdau=timeTable.startDay;
            ngayketthuc=timeTable.endDay;
            width=timeTable.wid;
            height=timeTable.hei;
        }else{
            timeTable = new TimeTable();
        }
    }






}
