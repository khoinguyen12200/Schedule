package com.example.schedule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.schedule.R.drawable.app_icon;
import static com.example.schedule.R.drawable.ic_app_icon;
import static com.example.schedule.R.drawable.ic_app_icon2;
import static com.example.schedule.R.drawable.phone_black;
import static com.example.schedule.R.drawable.whitealarm;


public class AlarmReceiver extends BroadcastReceiver {



    SettingData settingData;
    int HAVE_MODE_CODE =3;

    int NORMAL_MODE_CODE =0;
    int VIBRATE_MODE_CODE =1;
    int SILENT_MODE_CODE =2;

    int modeCode = -1;

    int ALARM =1;
    int NOTIFICATION=2;
    SharedPreferences sharedPreferences;
    SharedPreferences mPrefs;
    String[] ngay ;
    int giobatdau = 6;
    int gioketthuc =22;
    int ngaybatdau=0;
    int ngayketthuc=6;
    TimeTable timeTable;
    ArrayList acti;
    ArrayList intervals;
    float width;
    float height;
    ArrayList tableName;
    int currentTable;
    private static final String TAG = "alarmreciever";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("alarmreciever", "onReceive: ");

        AudioManager am;
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


        int status = intent.getIntExtra("status",-1);

        Log.d("alarmreciever", "onReceive: "+status);

        if(status==4){
            Log.d("addDelete", "onReceive: ");
            int idacti = intent.getIntExtra("id",-1);
            getdata(context);
            for(int i=0;i<acti.size();i++){
                Acti acti1 = (Acti) acti.get(i);
                if(idacti==acti1.id){
                    acti.remove(i);

                    for(int j=0;j<intervals.size();j++){
                        Interval interval = (Interval) intervals.get(j);
                        if(idacti==interval.id){
                            intervals.remove(j);
                        }
                    }
                    break;
                }
            }
            savedata(context);
            
        }
        else
        if(status == HAVE_MODE_CODE){
            modeCode = intent.getIntExtra("mode",-1);
            if(modeCode == NORMAL_MODE_CODE){
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                notifycustom(context,context.getResources().getString(R.string.change_normal));
            }
            else if ( modeCode == VIBRATE_MODE_CODE ){
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                notifycustom(context,context.getResources().getString(R.string.change_vibrate));
            }
            else if(modeCode == SILENT_MODE_CODE){
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                notifycustom(context,context.getResources().getString(R.string.change_silent));
            }

        }
        else{
            Bundle bundle = intent.getBundleExtra("bundle");
            if(bundle == null)
                return;

            Interval interval = bundle.getParcelable("interval");
            Acti acti = bundle.getParcelable("activity");
            settingData = bundle.getParcelable("settingData");

            if(status == ALARM){

                Intent intent1 = new Intent(context,Alarm_Activity.class);
                intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("activity",acti);
                intent1.putExtra("interval",interval);
                context.startActivity(intent1);

            }
            else if(status== NOTIFICATION){

                Intent intentcustom = new Intent(context,NotiService.class);
                intentcustom.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intentcustom.putExtra("audioNotiName",settingData.audioNotiName);
                intentcustom.putExtra("notiHaveVibration",settingData.notiHaveVibration);
                intentcustom.putExtra("interval",interval);
                intentcustom.putExtra("acti",acti);
                context.startService(intentcustom);
            }
        }


    }
    public void notifycustom(Context context,String s){

        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder ;
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(SettingData.CHANNEL_NOTIFICATION, context.getResources().getString(R.string.remind), NotificationManager.IMPORTANCE_HIGH);
            mChannel.setVibrationPattern(new long[]{500});
            mChannel.setLightColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary));
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.enableVibration(true);

            notificationManager.createNotificationChannel(mChannel);

            notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), mChannel.getId());



        } else {
            notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext());

        }
        notificationBuilder
                .setSmallIcon(ic_app_icon)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(s)
                .setLights(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary), 3000, 3000)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());
    }


    public void getdata(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("ALL_DATA",MODE_PRIVATE); // danh sach table + settingdata

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


    public void savedata(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("ALL_DATA",MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson1 = new Gson();
        String json1 = gson1.toJson(settingData);
        editor.putString("settingData",json1);

        editor.commit();

        timeTable.actis=acti;
        timeTable.intervals=intervals;

        timeTable.startTime=giobatdau;
        timeTable.endTime=gioketthuc;
        timeTable.startDay=ngaybatdau;
        timeTable.endDay=ngayketthuc;

        timeTable.wid=width;
        timeTable.hei=height;


        Gson gson = new Gson();
        String json = gson.toJson(timeTable);
        editor.putString("tableData", json);
        editor.commit();




    }


}
