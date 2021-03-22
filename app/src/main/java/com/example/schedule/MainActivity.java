package com.example.schedule;

import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;


import android.annotation.SuppressLint;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.AlarmManager;

import android.app.Dialog;
import android.app.PendingIntent;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupWindow;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {





    LinearLayout linearday,linearhour;
    ConstraintLayout constable;
    ConstraintLayout main_Constrain;

    ScrollView scrollhour;
    HorizontalScrollView scrollday;
    ArrayList intervals ;
    ArrayList acti ;
    ArrayList pendingintents ;
    String[] ngay ;
    int giobatdau = 6;
    int gioketthuc =22;
    int ngaybatdau=0;
    int ngayketthuc=6;

    public final float DEFAULT_WIDTH = 100;
    public final float DEFAULT_HEIGHT = 50;
    float width =DEFAULT_WIDTH;
    float height =DEFAULT_HEIGHT;


    int idActi =0;



    AlarmManager alarmManager;





    SettingData settingData;



    public void getngay(){
        ngay = new String[7];
        ngay[0]=getResources().getString(R.string.mon);
        ngay[1]=getResources().getString(R.string.tue);
        ngay[2]=getResources().getString(R.string.wed);
        ngay[3]=getResources().getString(R.string.thu);
        ngay[4]=getResources().getString(R.string.fri);
        ngay[5]=getResources().getString(R.string.sat);
        ngay[6]=getResources().getString(R.string.sun);
    }

    private AdView mAdView;

    public static String PACKAGE_NAME;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getngay();


        PACKAGE_NAME = getApplicationContext().getPackageName();





        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        anhxa();
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        intervals = new ArrayList<Interval>();
        acti = new ArrayList<Acti>();

        timeTable = new TimeTable();
        pendingintents = new ArrayList<PendingIntent>();



        getdata();


        showTABLE();


    }

    public void testalarm(int modecode){
        Acti actix = new Acti(10000,"ly thuyet","do thi",Color.BLACK);
        actix = (Acti) acti.get(1);
        Interval intervalx = new Interval(10000,999,15,17,0);
        intervalx = (Interval) intervals.get(1);
        Intent intent1 = new Intent(MainActivity.this,AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("settingData",settingData);

        intent1.putExtra("status",modecode);
        intent1.putExtra("mode",1);

        bundle.putParcelable("activity",actix);


        bundle.putParcelable("interval",intervalx);
        intent1.putExtra("bundle",bundle);

        intent1.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,2000000,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,3);
        logCalendar("testalarm",calendar);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


    }

    private void ad_view(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }



    TimeTable timeTable;
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
    public void save(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("ALL_DATA",MODE_PRIVATE);


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


    public static int MAXINTERVAL = 100;
    int numberofTimeline = -1;
    Interval[] listInterval;
    Acti oneActi;




    Boolean timeline1 = true;

    public void testdelete(){

//        Acti actia = new Acti(acti.size(),"abc","xyz",getResources().getColor(R.color.colorDarkOrange));
//        Interval interval = new Interval(acti.size(),intervals.size(),9,11,4);
//        acti.add(actia);
//        intervals.add(interval);
        int id = acti.size()-1;


        Intent intent1 = new Intent(MainActivity.this,AlarmReceiver.class);
        intent1.putExtra("status",4);
        intent1.putExtra("idActi",id);
        intent1.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,2000000,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,5);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    public void testData(){
        acti.add(new Acti(idActi,"Hoc Bai","Phan tich thuat toan", Color.RED)); idActi++;
        acti.add(new Acti(idActi,"Di choi","The thao",Color.BLUE)); idActi++;
        acti.add(new Acti(idActi,"ABC","XYZ",Color.GRAY));  idActi++;
        acti.add(new Acti(idActi,"Activity","cham cham cham cham",Color.MAGENTA)); idActi++;
    }



    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void addANN(Acti activity,Interval interval,int status, int before){

        int num = interval.idEvent*10 + status;


        Calendar intercal = interval.timeToCalendar();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE),
                intercal.get(Calendar.HOUR_OF_DAY), intercal.get(Calendar.MINUTE),0);
        calendar.add(Calendar.MINUTE,-before);

        int eventday = interval.getDayStandar();
        while(calendar.getTimeInMillis()<=Calendar.getInstance().getTimeInMillis() || calendar.get(Calendar.DAY_OF_WEEK)!=eventday)
            calendar.add(Calendar.DATE,1);




        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("settingData",settingData);
        bundle.putParcelable("activity",activity);
        bundle.putParcelable("interval",interval);

        intent.putExtra("bundle",bundle);
        intent.putExtra("status",status);

        PendingIntent  pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,num,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        pendingintents.add(pendingIntent);


        if(activity.weekly == Acti.ONCE && interval.getCalendarStart().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            Calendar calendar1 = interval.getCalendarStart();
            calendar1.add(Calendar.MINUTE,-before);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),pendingIntent);

        }
        else    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);

        Log.d("addANN", "Acti: "+activity+"\n");



    }


    public void addMODE(Interval interval,Acti activity){


        int num = interval.idEvent*10 + HAVE_MODE_CODE + activity.mode;

        Calendar calendar1 = interval.timeToCalendar();



        int eventday = interval.getDayStandar();

        while(calendar1.getTimeInMillis()<=Calendar.getInstance().getTimeInMillis() || calendar1.get(Calendar.DAY_OF_WEEK)!=eventday)
            calendar1.add(Calendar.DATE,1);


        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);

        intent.putExtra("status",HAVE_MODE_CODE);

        intent.putExtra("mode",activity.mode);

        PendingIntent  pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,num,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        if(activity.weekly == Acti.ONCE && interval.getDay().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            Calendar calendara = interval.getCalendarStart();
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendara.getTimeInMillis(),pendingIntent);
        }
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);

        pendingintents.add(pendingIntent);

        int num2 = interval.idEvent*10 + HAVE_MODE_CODE + Interval.NORMAL;

        Calendar calendar2 = interval.timeEndToCalendar();


        while(calendar2.getTimeInMillis()<=Calendar.getInstance().getTimeInMillis() || calendar2.get(Calendar.DAY_OF_WEEK)!=eventday)
            calendar2.add(Calendar.DATE,1);


        Intent intent2 = new Intent(MainActivity.this,AlarmReceiver.class);

        intent2.putExtra("status",Acti.HAVE_MODE);

        intent2.putExtra("mode",Interval.NORMAL);

        PendingIntent  pendingIntent2 = PendingIntent.getBroadcast(
                MainActivity.this,num2,intent2,PendingIntent.FLAG_UPDATE_CURRENT);



        if(activity.weekly == Acti.ONCE && interval.getDay().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            Calendar calendara = interval.getCalendarStart();
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendara.getTimeInMillis(),pendingIntent);
        }
        else
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent2);

        pendingintents.add(pendingIntent2);


        Log.d("NUM", num+" im lang + rung ");
        Log.d("NUM", num2+" mo am thanh ");

        logCalendar("mode_calendar",calendar1);
        logCalendar("mode_calendar",calendar2);
        Log.d("mode_calendar", "\n\n");
    }



    int HAVE_ALARM_CODE =1;
    int HAVE_NOTI_CODE =2;
    int HAVE_MODE_CODE =3;
    public void resetAllANN(){
        cancelAllANN();

        for(int i=0;i<acti.size();i++){
            Acti activity = (Acti) acti.get(i);
            for(int j=0;j<intervals.size();j++){
                Interval interval = (Interval) intervals.get(j);
                if(activity.id == interval.id){
                    if(activity.alarmBefore !=-1 ){
                        addANN(activity,interval,HAVE_ALARM_CODE,activity.alarmBefore);
                    }
                    if(activity.notiBefore !=-1){
                        addANN(activity,interval,HAVE_NOTI_CODE,activity.notiBefore);
                    }
                    if(activity.mode != 0){
                        addMODE(interval,activity);
                    }
                    if (activity.weekly == Acti.ONCE && activity.autoDelete == Acti.AUTO_DELETE){
                        addDelete(activity,interval);
                    }

                }
            }
        }
    }

    private void addDelete(Acti activity, Interval interval) {
        Intent intent1 = new Intent(MainActivity.this,AlarmReceiver.class);
        intent1.putExtra("status",4);
        intent1.putExtra("id",activity.id);
        intent1.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,2000000,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar = interval.getCalendarEnd();
        calendar.add(Calendar.SECOND,10);

        pendingintents.add(pendingIntent);

        int a = (int) (calendar.getTimeInMillis()- Calendar.getInstance().getTimeInMillis());

        if(a>0)
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        else{
            Calendar now  = Calendar.getInstance();
            now.add(Calendar.SECOND,5);
            alarmManager.set(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),pendingIntent);
        }


        Log.d("addDelete", "addDelete: a="+a);
        logCalendar("addDelete: ",calendar);

    }
    public void logCalendar(String s,Calendar calendar){
        int a = (int) (calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis());
        a=a/1000;
        Log.d(s, "\ngio:"+calendar.get(Calendar.HOUR_OF_DAY)+" phut:"+calendar.get(Calendar.MINUTE)
                +"\n ngay:"+calendar.get(Calendar.DATE)+" thang:"+calendar.get(Calendar.MONTH)+" nam:"+calendar.get(Calendar.YEAR)+
                "\n thoi gian con lai (giay):"+a);
    }


    public void cancelAllANN(){
        while(pendingintents.size()!=0){
            PendingIntent pendingIntent = (PendingIntent) pendingintents.get(pendingintents.size()-1);
            alarmManager.cancel(pendingIntent);
            pendingintents.remove(pendingintents.size()-1);
        }

    }






    ArrayList listRemove;
    @Override // Tạo hàm onActivityResult
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data==null){
            getdata();
            save();
            showTABLE();

        }else{
            if(data.getIntExtra("result",0)==0){
                getdata();
                save();
                showTABLE();
            }else{

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    public void newvalue(){
        acti = new ArrayList<Acti>();
        intervals = new ArrayList<Interval>();
        giobatdau = 6;
        gioketthuc = 17;
        ngaybatdau = 0;
        ngayketthuc = 6;
        width = 100;
        height = 50;

    }





    float heiTable, widTalbe;

    public void logAll(){
        String ssss = "";
        for(int i=0;i<acti.size();i++){
            Acti ac = new Acti((Acti) acti.get(i));
            ssss=ssss+""+ac+"\n";
        }
        for(int i=0;i<intervals.size();i++){
            Interval interval = new Interval((Interval) intervals.get(i));
            ssss=ssss+""+interval+"\n";
        }


        Log.d("Alldata", "logAll: \n"+ssss);

    }

    int CLASSIC_TABLE_MODE =1, CORNERS_TABLE_MODE =2, BALANCE_TABLE_MODE=3;
    int index =0;
    public void updateid(){
        for(int vitri =0;vitri<acti.size();vitri++){
            Acti activity = (Acti) acti.get(vitri);
            if(activity.id == vitri)
                continue;
            else{
                int oldID = activity.id;
                activity.id = vitri;
                acti.remove(vitri);
                acti.add(vitri,activity);

                for(int i=0;i<intervals.size();i++){
                    Interval interval = (Interval) intervals.get(i);
                    if(interval.id == oldID){
                        interval.id = activity.id;
                        intervals.remove(i);
                        intervals.add(i,interval);
                    }
                }
            }
        }
        for(int i=intervals.size()-1;i>=0;i--){
            Interval interval = (Interval) intervals.get(i);
            interval.idEvent=i;
            intervals.remove(i);
            intervals.add(i,interval);

            if(interval.id >= acti.size() || interval.id <0)
                intervals.remove(i);
        }
    }
    int pxHeighTB;
    int pxWidthTB;

    int pxHeighHour;
    int pxWidthHour;

    int pxHeighDay;
    int pxWidthDay;

    private  void showTABLE()
    {

        vScroll.setScaleY(1);
        vScroll.setScaleX(1);
        if(!changesize){
            logAll();
            resetAllANN();
            updateWidget();
            updateid();
            index=0;
            setTitleDate();
            save();
        }

        Resources r = getResources();

        int minWid = 35, minHei=50;

         pxHeighTB = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,height,r.getDisplayMetrics());
         pxWidthTB= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,width,r.getDisplayMetrics());

        pxHeighHour = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,height,r.getDisplayMetrics());
        pxWidthHour= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minWid,r.getDisplayMetrics());

        pxHeighDay = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minHei,r.getDisplayMetrics());
        pxWidthDay = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,width,r.getDisplayMetrics());




        if(settingData.RBT==0)
            showTABLE1();
        else if(settingData.RBT==1)
            showTABLE2();
        else if(settingData.RBT==2)
            showTABLE3();


        constable.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                constable.setScrollX((int) event.getX());
                constable.setScrollY((int) event.getY());
                return false;
            }
        });

        Log.d("alldata", "showTABLE: xong");
    }
    public void setTitleDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.dateFotmat));
        String time = simpleDateFormat.format(calendar.getTime());
        add_actionbar_title(time);
    }
    public void setDay(){
        int sumwidth =0;
        Calendar calendar = Calendar.getInstance();
        int eventday=0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 2 : eventday =0; break;
            case 3 : eventday =1; break;
            case 4 : eventday =2; break;
            case 5 : eventday =3; break;
            case 6 : eventday =4; break;
            case 7 : eventday =5; break;
            case 1 : eventday =6; break;
        }

        for(int i = ngaybatdau;i<=ngayketthuc;i++){
            TextView textView = new TextView(this);
            textView.setBackgroundColor(settingData.backBarColor);
            if(i==eventday){
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(settingData.backBarColor);
                gd.setStroke(2,settingData.textBarColor);
                gd.setCornerRadius(10);
                textView.setBackground(gd);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            textView.setTextColor(settingData.textBarColor);
            textView.setText(ngay[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pxWidthDay,pxHeighDay);
            textView.setLayoutParams(params);
            linearday.addView(textView);
            sumwidth+=pxWidthDay;
        }
//        TextView textView = new TextView(this);
//        textView.setBackgroundColor(settingData.backBarColor);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2500,pxHeighDay);
//        textView.setLayoutParams(params);
//        linearday.addView(textView);

            constable.setPadding(0,0,pxWidthHour,0);

    }


    public void showTABLE1(){


        ArrayList listInterval = new ArrayList(intervals);
        for(int i = 0;i<listInterval.size();i++){
            boolean remove =false;
            Interval interval = (Interval) listInterval.get(i);
            if(interval.endTime>gioketthuc+1||interval.startTime<giobatdau)
                remove=true;
            if(interval.eventDay>ngayketthuc||interval.eventDay<ngaybatdau)
                remove = true;
            if(remove){
                listInterval.remove(i);
            }
        }

        linearday.removeAllViews();
        linearhour.removeAllViews();
        constable.removeAllViews();

        TextView title = (TextView) findViewById(R.id.textView);

        linearday.setPadding(0,0,pxWidthHour,0);
        linearhour.setPadding(0,0,0,pxHeighDay*2);


        title.setBackgroundColor(settingData.backBarColor);

        title.setWidth(pxWidthHour);
        title.setHeight(pxHeighDay-30);

        heiTable=pxHeighTB*(gioketthuc-giobatdau+1)+pxHeighDay*2+getStatusBarHeight();
        widTalbe=pxWidthTB*(ngayketthuc-ngaybatdau+1)+pxWidthHour*2;

        constable.getLayoutParams().height= (int) heiTable;
        constable.getLayoutParams().width= (int) widTalbe;

        setDay();



        for(int i=giobatdau;i<=gioketthuc;i++){
            TextView textView = new TextView(this);
            textView.setBackgroundColor(settingData.backBarColor);
            textView.setTextColor(settingData.textBarColor);
            textView.setText(i+"");
            textView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            textView.setHeight(pxHeighHour);
            textView.setWidth(pxWidthHour);
            linearhour.addView(textView);
        }

        TextView nullview = new TextView(this);
        nullview.setHeight(2500);
        nullview.setWidth(pxWidthHour);
        nullview.setBackgroundColor(settingData.backBarColor);
        linearhour.addView(nullview);





        GradientDrawable gdforcell = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {settingData.cellColor,settingData.cellColor});
        gdforcell.setStroke(1,Color.BLACK);
        LinearLayout tableLinear = new LinearLayout(this);
        tableLinear.removeAllViews();
        for(int i = ngaybatdau;i<=ngayketthuc;i++){
            TableLayout tableLayout = new TableLayout(this);
            for(int j=giobatdau;j<=gioketthuc;j++){
                TextView textView = new TextView(this);
                textView.setHeight(pxHeighTB);
                textView.setWidth(pxWidthTB);
                textView.setBackground(gdforcell);
                tableLayout.addView(textView);
            }
            tableLinear.addView(tableLayout);
        }
        constable.addView(tableLinear);


        for(int i=0;i<listInterval.size();i++){

            Interval interval = (Interval) listInterval.get(i);
            Acti activity = new Acti();
            for(int j=0;j<acti.size();j++){
                activity = (Acti) acti.get(j);
                if(interval.id == activity.id)
                    break;
            }

            LinearLayout linear1 = new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);

            for(int j=ngaybatdau;j<interval.eventDay;j++){
                TextView nulltext = new TextView(this);
                nulltext.setWidth(pxWidthTB);
                linear1.addView(nulltext);
            }
            LinearLayout linearColum = new LinearLayout(this);
            linearColum.setOrientation(LinearLayout.VERTICAL);
            TextView space = new TextView(this);
            space.setHeight( (int) ((interval.startTime-giobatdau)*pxHeighTB));
            linearColum.addView(space);
            int heightThisView = (int) ((interval.endTime-interval.startTime)*pxHeighTB);

            ///////////////////////////////////////////////// CUSTOM //////////////////////
            ItemView(linearColum,pxWidthTB,heightThisView,activity,interval);


            linear1.addView(linearColum);
            constable.addView(linear1);
        }

    }

    public void showTABLE3(){

        linearday.removeAllViews();
        linearhour.removeAllViews();
        constable.removeAllViews();

        TextView title = (TextView) findViewById(R.id.textView);


        int padding =20;

        Resources r = getResources();

        int pxPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,r.getDisplayMetrics());


        title.setWidth(0);
        title.setHeight(0);
        linearday.setPadding(0,0,pxWidthHour,0);
        linearhour.setPadding(0,0,0,pxHeighDay*2);
        title.setBackgroundColor(getResources().getColor(R.color.none_color));


        


        Log.d("pppppppp", "showTABLE: "+linearhour.getMinimumHeight());

        setDay();





        int tongremove =0;

        ArrayList listInterval = new ArrayList(intervals);
        for(int i = 0;i<listInterval.size();i++){
            boolean remove =false;
            Interval interval = (Interval) listInterval.get(i);
            if(interval.eventDay>ngayketthuc||interval.eventDay<ngaybatdau)
                remove = true;
            if(remove){
                tongremove++;
                listInterval.remove(i);
            }
        }


        int maxhei =0;
        int vitri =0;
        LinearLayout linearRow = new LinearLayout(this);
        linearRow.setOrientation(LinearLayout.HORIZONTAL);

        for(int i = ngaybatdau;i<=ngayketthuc;i++){
            int dodai=0;

            LinearLayout linearColum = new LinearLayout(this);
            linearColum.setOrientation(LinearLayout.VERTICAL);
            TextView colortext = new TextView(this);
            colortext.setWidth(pxWidthTB);
            colortext.setHeight(0);
            colortext.setBackgroundColor(getResources().getColor(R.color.colorLiteGray));
            linearColum.addView(colortext);

            TextView nulltext = new TextView(this);
            nulltext.setHeight(padding);
            linearColum.addView(nulltext);



            while(vitri<listInterval.size()){
                Interval interval = (Interval) listInterval.get(vitri);
                Acti activity = new Acti();
                for(int j=0;j<acti.size();j++){
                    activity = (Acti) acti.get(j);
                    if(interval.id == activity.id)
                        break;
                }
                if(interval.eventDay!=i){
                    break;
                }
                else{
                    int heightThisView = (int) ((interval.endTime-interval.startTime)*pxHeighTB);
                    ItemView(linearColum,pxWidthTB,heightThisView,activity,interval);

                    TextView textView = new TextView(this);
                    textView.setHeight(padding);
                    linearColum.addView(textView);
                    vitri++;
                    dodai+=(heightThisView+pxPadding);
                }
                Log.d("mothaiba111", "vitri: "+vitri+"; i="+i);
            }
            if(dodai>maxhei)
                maxhei=dodai;
            linearRow.addView(linearColum);


        }

        heiTable=maxhei+pxHeighDay*2+getStatusBarHeight();
        widTalbe=pxWidthTB*(ngayketthuc-ngaybatdau+1);

        constable.getLayoutParams().height= (int) heiTable;

        constable.getLayoutParams().width= (int) widTalbe;

        constable.setPadding(0,0,pxWidthHour,0);



        GradientDrawable gdforcell = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {settingData.cellColor,settingData.cellColor});
        gdforcell.setStroke(4,settingData.backBarColor);

        LinearLayout lineartable = new LinearLayout(this);
        lineartable.setOrientation(LinearLayout.HORIZONTAL);


        for(int i=ngaybatdau;i<=ngayketthuc;i++){
            TextView textView = new TextView(this);
            textView.setHeight(maxhei);
            textView.setWidth( pxWidthDay);
            textView.setBackgroundDrawable(gdforcell);
            lineartable.addView(textView);
        }

        constable.addView(lineartable);


        constable.addView(linearRow);




    }

    public void showTABLE2() {

        ArrayList listInterval = new ArrayList(intervals);
        for (int i = 0; i < listInterval.size(); i++) {
            boolean remove = false;
            Interval interval = (Interval) listInterval.get(i);
            if (interval.endTime > gioketthuc + 1 || interval.startTime < giobatdau)
                remove = true;
            if (interval.eventDay > ngayketthuc || interval.eventDay < ngaybatdau)
                remove = true;
            if (remove) {
                listInterval.remove(i);
            }
        }

        linearday.removeAllViews();
        linearhour.removeAllViews();


        constable.removeAllViews();

        TextView title = (TextView) findViewById(R.id.textView);



        title.setWidth(pxWidthHour);
        title.setHeight(pxHeighDay-30);
        linearday.setPadding(0, 0, pxWidthHour, 0);
        linearhour.setPadding(0, 0, 0, pxHeighDay * 2);

        title.setBackgroundColor(settingData.backBarColor);



        int GioHien[] = new int[24];
        for (int i = 0; i < GioHien.length; i++)
            GioHien[i] = 0;

        for (int i = 0; i < listInterval.size(); i++) {
            Interval interval = (Interval) listInterval.get(i);
            int st = (int) Math.floor(interval.startTime);
            int en;
            if (interval.endTime % 1 == 0)
                en = (int) (interval.endTime - 1);
            else en = (int) Math.floor(interval.endTime);
            for (int j = st; j <= en; j++)
                GioHien[j] = 1;
        }
        setDay();

        heiTable =pxHeighTB * 2 + getStatusBarHeight();
        for(int i=0;i<GioHien.length;i++)
            heiTable += pxHeighTB*(GioHien[i]);

        widTalbe = pxWidthTB * (ngayketthuc - ngaybatdau + 1) + pxWidthHour*2;

        constable.getLayoutParams().height = (int) heiTable;
        constable.getLayoutParams().width = (int) widTalbe;
        constable.setPadding(0,0,pxWidthHour,0);



        for (int i = giobatdau; i <= gioketthuc; i++) {
            if (GioHien[i] == 1) {
                TextView textView = new TextView(this);
                textView.setBackgroundColor(settingData.backBarColor);
                textView.setTextColor(settingData.textBarColor);
                textView.setText(i + "");
                textView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                textView.setHeight(pxHeighHour);
                textView.setWidth(pxWidthHour);
                linearhour.addView(textView);
            }
        }





        TextView nullview = new TextView(this);
        nullview.setHeight(2500);
        nullview.setWidth(pxWidthHour);
        nullview.setBackgroundColor(settingData.backBarColor);
        linearhour.addView(nullview);

        TextView nullview2 = new TextView(this);
        nullview2.setBackgroundColor(settingData.backBarColor);
        nullview2.setTextColor(settingData.textBarColor);
        nullview2.setGravity(Gravity.CENTER);
        nullview2.setSingleLine(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2000,pxHeighDay);
        nullview2.setLayoutParams(params);
        linearday.addView(nullview2);


        GradientDrawable gdforcell = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{settingData.cellColor, settingData.cellColor});
        gdforcell.setStroke(1, Color.BLACK);

        LinearLayout tableLinear = new LinearLayout(this);
        tableLinear.removeAllViews();
        for (int i = ngaybatdau; i <= ngayketthuc; i++) {
            TableLayout tableLayout = new TableLayout(this);
            for (int j = giobatdau; j <= gioketthuc; j++) {
                if(GioHien[j]==0)
                    continue;
                TextView textView = new TextView(this);
                textView.setHeight(pxHeighTB);
                textView.setWidth(pxWidthTB);
                textView.setBackground(gdforcell);
                tableLayout.addView(textView);
            }
            tableLinear.addView(tableLayout);
        }
        constable.addView(tableLinear);


        for(int i=0;i<listInterval.size();i++){

            Interval interval = (Interval) listInterval.get(i);
            Acti activity = new Acti(0,"","",Color.WHITE);
            for(int j=0;j<acti.size();j++){
                activity = (Acti) acti.get(j);
                if(interval.id == activity.id)
                    break;
            }

            LinearLayout linear1 = new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);

            for(int j=ngaybatdau;j<interval.eventDay;j++){
                TextView nulltext = new TextView(this);
                nulltext.setWidth(pxWidthTB);
                linear1.addView(nulltext);
            }

            LinearLayout linearColum = new LinearLayout(this);
            linearColum.setOrientation(LinearLayout.VERTICAL);


            TextView space = new TextView(this);
            int he=(int) ((interval.startTime-Math.floor(interval.startTime))*pxHeighTB);
            for(int j=(int)Math.floor(interval.startTime)-1;j>=giobatdau;j--){
                he += (pxHeighTB*GioHien[j]);
            }

            space.setHeight(he);
            linearColum.addView(space);


            int heightThisView = (int) ((interval.endTime-interval.startTime)*pxHeighTB);


            ItemView(linearColum,pxWidthTB,heightThisView,activity,interval);


            linear1.addView(linearColum);

            constable.addView(linear1);
        }


    }

    private void ItemView(LinearLayout linearColum, int pxWidthTB, int heightThisView, Acti activity, Interval interval) {
        if(settingData.itemType==1)
            ItemView1(linearColum,pxWidthTB,heightThisView,activity,interval);
        else if(settingData.itemType==2)
            ItemView2(linearColum,pxWidthTB,heightThisView,activity,interval);
        else if(settingData.itemType==3)
            ItemView3(linearColum,pxWidthTB,heightThisView,activity,interval);
    }

    public void ItemView1(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, final Interval interval){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview1_layout,null);



        TextView name = (TextView) view.findViewById(R.id.name);

        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);
        ImageView imageView = (ImageView) view.findViewById(R.id.line);
        imageView.setBackgroundColor(activity.color);


        TextView time1 = (TextView) view.findViewById(R.id.time1);
        time1.setText(interval.startString());
        time1.setTextSize(settingData.textSize);
        time1.setTextColor(activity.color);

        TextView time2 = (TextView) view.findViewById(R.id.time2);
        time2.setText(interval.endString());
        time2.setTextSize(settingData.textSize);
        time2.setTextColor(activity.color);

        name.setText(activity.name);
        name.setBackgroundColor(activity.color);
        name.setTextSize(settingData.textSize);

        TextView des = (TextView) view.findViewById(R.id.des);
        des.setText(activity.describe);
        des.setTextSize(settingData.textSize-1);
        if(activity.describe.equals("")) des.getLayoutParams().height=0;

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(interval.location);
        location.setTextSize(settingData.textSize-1);

        if(interval.location == null )location.setHeight(0);
        else if(interval.location.equals("")) location.setHeight(0);

        if(settingData.showName == SettingData.HIDE)
            name.setVisibility(View.GONE);
        if(settingData.showTime==SettingData.HIDE){
            time1.setVisibility(View.GONE);
            time2.setVisibility(View.GONE);
        }
        if(settingData.showDescribe==SettingData.HIDE)
            des.setVisibility(View.GONE);
        if(settingData.showLocation==SettingData.HIDE)
            location.setVisibility(View.GONE);

        constraintLayout.setBackgroundColor(activity.color);

        if(!havenotes(activity))
            note.setVisibility(View.INVISIBLE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);
        addAnimPopup(constraintLayout);
        linearColum.addView(constraintLayout);




        constraintLayout.setScrollContainer(true);

        clicked_activity(constraintLayout,activity,interval);



    }

    public void ItemView2(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, final Interval interval){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview2_layout,null);

        TextView time1 = (TextView) view.findViewById(R.id.time1);
        TextView time2 = (TextView) view.findViewById(R.id.time2);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView des = (TextView) view.findViewById(R.id.des);
        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);


        time1.setText(interval.startString());
        time2.setText(interval.endString());
        name.setText(activity.name);
        des.setText(activity.describe);
        time1.setTextSize(settingData.textSize);
        time2.setTextSize(settingData.textSize);
        time1.setTextColor(activity.color);
        time2.setTextColor(activity.color);
        name.setTextSize(settingData.textSize);
        des.setTextSize(settingData.textSize-1);
        if(activity.describe.equals(""))     des.setHeight(0);
        constraintLayout.setBackgroundColor(activity.color);

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(interval.location);
        location.setTextSize(settingData.textSize-1);

        if(interval.location == null )location.setHeight(0);
        else if(interval.location.equals("")) location.setHeight(0);

        if(!havenotes(activity))
            note.setVisibility(View.INVISIBLE);

        if(settingData.showName == SettingData.HIDE)
            name.setVisibility(View.GONE);
        if(settingData.showTime==SettingData.HIDE){
            time1.setVisibility(View.GONE);
            time2.setVisibility(View.GONE);
        }
        if(settingData.showDescribe==SettingData.HIDE)
            des.setVisibility(View.GONE);
        if(settingData.showLocation==SettingData.HIDE)
            location.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);
        addAnimPopup(constraintLayout);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {activity.color,activity.color});
        float mRadius = 23f;
        gd.setCornerRadius(mRadius);
        constraintLayout.setBackgroundDrawable(gd);
        linearColum.addView(constraintLayout);




        clicked_activity(constraintLayout,activity,interval);


    }

    public void ItemView3(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, final Interval interval){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview3_layout,null);

        TextView time1 = (TextView) view.findViewById(R.id.time1);
        TextView time2 = (TextView) view.findViewById(R.id.time2);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView des = (TextView) view.findViewById(R.id.des);
        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);


        time1.setText(interval.startString());
        time2.setText(interval.endString());
        name.setText(activity.name);
        des.setText(activity.describe);
        time1.setTextSize(settingData.textSize);
        time2.setTextSize(settingData.textSize);
        time1.setTextColor(activity.color);
        time2.setTextColor(activity.color);
        name.setTextSize(settingData.textSize);
        des.setTextSize(settingData.textSize-1);
        if(activity.describe.equals("")) des.setHeight(0);
        constraintLayout.setBackgroundColor(activity.color);

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(interval.location);
        location.setTextSize(settingData.textSize-1);

        if(interval.location == null )location.setHeight(0);
        else if(interval.location.equals("")) location.setHeight(0);

        if(!havenotes(activity))
            note.setVisibility(View.INVISIBLE);

        if(settingData.showName == SettingData.HIDE)
            name.setVisibility(View.GONE);
        if(settingData.showTime==SettingData.HIDE){
            time1.setVisibility(View.GONE);
            time2.setVisibility(View.GONE);
        }
        if(settingData.showDescribe==SettingData.HIDE)
            des.setVisibility(View.GONE);
        if(settingData.showLocation==SettingData.HIDE)
            location.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);
        addAnimPopup(constraintLayout);
        linearColum.addView(constraintLayout);




        clicked_activity(constraintLayout,activity,interval);


    }

    public void clicked_activity(final View view, final Acti activity, final Interval interval){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_click_popup_window(view,activity,interval);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Sort(intervals);
                Intent intentmain2 = new Intent(MainActivity.this,Main2Activity.class);
                intentmain2.putExtra("id", activity.id);

                startActivityForResult(intentmain2,0);
                return true;
            }
        });
    }

    PopupWindow popupWindow;
    private void item_click_popup_window(final View view, final Acti activity, final Interval interval){



        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = vi.inflate(R.layout.item_click_layout, null);
        Rect locationv = locateView(view);

        int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
        final boolean focusable = true;

        popupWindow = new PopupWindow(v,width1,height1,focusable);
        popupWindow.setAnimationStyle(R.style.Popup_window_anim);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nothing));
        popupWindow.showAtLocation(this.findViewById(R.id.Main_constrain), Gravity.TOP|Gravity.LEFT, locationv.right, locationv.top);


        final TextView actiTitle = (TextView) v.findViewById(R.id.eventTitle);
        actiTitle.setText(activity.name);
        final TextView actides = (TextView) v.findViewById(R.id.eventdes);
        actides.setText(activity.describe);
        final TextView actiTime = (TextView) v.findViewById(R.id.eventTime);
        actiTime.setText(interval.timeString());
        final TextView location = (TextView) v.findViewById(R.id.location);
        location.setText(interval.location);
        final TextView note = (TextView) v.findViewById(R.id.note);
        note.setText(activity.note);



        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNote(activity);
                popupWindow.dismiss();
            }
        });

        final LinearLayout event = (LinearLayout) v.findViewById(R.id.event);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sort(intervals);
                Intent intentmain2 = new Intent(MainActivity.this,Main2Activity.class);
                intentmain2.putExtra("id", activity.id);

                startActivityForResult(intentmain2,0);
                popupWindow.dismiss();
            }
        });

        final CheckBox alarm = (CheckBox) v.findViewById(R.id.alarm);
        final CheckBox noti = (CheckBox) v.findViewById(R.id.noti);

        if(activity.alarmBefore==-1){
            alarm.setChecked(false);
        }else {
            alarm.setChecked(true);
        }
        if(activity.notiBefore==-1){
            noti.setChecked(false);
        }else {
            noti.setChecked(true);
        }

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(activity.alarmBefore==-1){
                    alarm.setChecked(false);
                }else {
                    alarm.setChecked(true);
                }
                before_dialog(Acti.ALARM,activity);
                popupWindow.dismiss();
            }
        });
        noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(activity.notiBefore==-1){
                    noti.setChecked(false);
                }else {
                    noti.setChecked(true);
                }
                before_dialog(Acti.NOTIFICATION,activity);
                popupWindow.dismiss();
            }
        });

        ImageView phonemod = (ImageView) v.findViewById(R.id.phonemode);
        if(activity.mode == Interval.NORMAL)
            phonemod.setImageResource(R.drawable.ring_white_24);
        else if(activity.mode == Interval.VIBRATE)
            phonemod.setImageResource(R.drawable.vibrate_white_24);
        else if(activity.mode == Interval.SILENT)
            phonemod.setImageResource(R.drawable.silent_white_24);

        phonemod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_mode_dialog(activity);
                popupWindow.dismiss();
            }
        });




    }

    int current_minute;
    private  void before_dialog(final int type, final Acti oneActi){
        final int [] minutes = getResources().getIntArray(R.array.minute_add);
        final int MAX_minute = 600;
        current_minute =0;



        final Dialog  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.before_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.nothing);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        ConstraintLayout back = (ConstraintLayout) dialog.findViewById(R.id.background);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout main = (LinearLayout) dialog.findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView time = (TextView) dialog.findViewById(R.id.time);
        Button reset = (Button) dialog.findViewById(R.id.reset);
        Button set = (Button) dialog.findViewById(R.id.set);
        Button unset = (Button) dialog.findViewById(R.id.unset);
        FlexboxLayout flexbox = (FlexboxLayout) dialog.findViewById(R.id.flexbox);

        final Animation ping = AnimationUtils.loadAnimation(this,R.anim.up_down_lite);
        final Animation ping2 = AnimationUtils.loadAnimation(this,R.anim.down_up_lite);
        for(int i=0;i<minutes.length;i++){
            final Button button = new Button(this);
            button.setText("+ "+minutes[i]);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_minute+=minutes[finalI];
                    if(current_minute>MAX_minute)
                        current_minute=MAX_minute;
                    button.startAnimation(ping);
                    time.startAnimation(ping2);
                    setTimeTextView(time,current_minute);

                }
            });

            flexbox.addView(button);
        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_minute=0;
                time.startAnimation(ping2);
                setTimeTextView(time,current_minute);
            }
        });


        if(type == Acti.ALARM){

            title.setText(getResources().getString(R.string.alarm));
            current_minute = oneActi.alarmBefore;
            if(current_minute==-1)
                current_minute=0;
            setTimeTextView(time,current_minute);



            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneActi.alarmBefore = current_minute;
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

                }
            });
            unset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneActi.alarmBefore = -1;
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

                }
            });


        }else if(type == Acti.NOTIFICATION){

            title.setText(getResources().getString(R.string.noti));
            current_minute =oneActi.notiBefore;
            if(current_minute==-1)
                current_minute=0;
            setTimeTextView(time,current_minute);

            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneActi.notiBefore = current_minute;
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

                }
            });
            unset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneActi.notiBefore = -1;
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();


                }
            });
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                save();
                resetAllANN();
            }
        });

        dialog.show();

    }
    private  void setTimeTextView(TextView textView,int minute){
        int h = minute/60;
        int m = minute%60;
        String s = h+" "+getResources().getString(R.string.Hour)+" "+m+ " "+getResources().getString(R.string.Minute);
        textView.setText(s);

    }
    private void phone_mode_dialog(final Acti activity){
        final Dialog  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.phone_mode_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.nothing);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout back = (LinearLayout) dialog.findViewById(R.id.background);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout main = (LinearLayout) dialog.findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RadioButton normal = (RadioButton) dialog.findViewById(R.id.normalRadio);
        RadioButton vibrate = (RadioButton) dialog.findViewById(R.id.vibrateRadio);
        RadioButton silent = (RadioButton) dialog.findViewById(R.id.silentRadio);

        if(activity.mode == Interval.NORMAL)
            normal.setChecked(true);
        if(activity.mode == Interval.VIBRATE)
            vibrate.setChecked(true);
        if(activity.mode == Interval.SILENT)
            silent.setChecked(true);
        final int first = activity.mode;

        normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activity.mode = Interval.NORMAL;
            }
        });
        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activity.mode = Interval.VIBRATE;
            }
        });
        silent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activity.mode = Interval.SILENT;
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(activity.mode != first){
                    save();
                    resetAllANN();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }




    public void updateWidget(){
        Sort(intervals);
        Intent intent = new Intent(this, EventAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), EventAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        Intent intent2 = new Intent(this, EventAppWidget2.class);
        intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids2 = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), EventAppWidget2.class));
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids2);
        sendBroadcast(intent2);
    }
    Boolean changesize = false;
    public void addAnimPopup(View view){
        if(!changesize){
            Animation popup = AnimationUtils.loadAnimation(this,R.anim.pop_up);
            popup.setStartOffset(index*50);
            view.setAnimation(popup);
            index++;
        }


    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public boolean havenotes (Acti activity){
        if(activity.note.equals(""))
            return false;
        return true;

    }


    int maxW =200;
    int minW=30;
    int maxH=200;
    int minH=30;

    PopupWindow SizeChangePopup;
    boolean first = true;
    public void PopupSizeChange(){

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = vi.inflate(R.layout.popup_sizechagne, null);

        int width1 = LinearLayout.LayoutParams.MATCH_PARENT;
        int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
        final boolean focusable = true;
        SizeChangePopup = new PopupWindow(v,width1,height1,focusable);

        SeekBar seekwid = (SeekBar) v.findViewById(R.id.seekwid);
        SeekBar seekhei = (SeekBar) v.findViewById(R.id.seekhei);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.sizechangelayout);

        seekwid.setMax((maxW-minW)*100);
        seekhei.setMax((maxH-minH)*100);

        seekwid.setProgress(100*((int) (width)-minW));
        seekhei.setProgress(100*((int) (height)-minH));


        first =true;
        final Animation out = AnimationUtils.loadAnimation(this,R.anim.note_out);
        seekwid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                width = (progress)/100+minW;

                changesize=true;
                showTABLE();
                changesize=false;

                constable.setScrollX(0);
                constable.setScrollY(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                save();
            }
        });
        seekhei.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                height = (progress/100)+minH;
                changesize=true;
                showTABLE();
                changesize=false;
                constable.setScrollX(0);
                constable.setScrollY(0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                save();
            }
        });



        SizeChangePopup.setAnimationStyle(R.style.PopupAnimation);
        SizeChangePopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.nothing));
        SizeChangePopup.showAtLocation(this.findViewById(R.id.Main_constrain), Gravity.BOTTOM,50,50);



        main_Constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SizeChangePopup.dismiss();
            }
        });



    }


    boolean note_show = false;
    EditText editnote;

    public void showNote(final Acti activity){
        final Dialog  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.NoteAnimation;
        dialog.setContentView(R.layout.note_list_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.nothing);

        note_show = true;

        save();


        ConstraintLayout back = (ConstraintLayout) dialog.findViewById(R.id.noteback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tile = (TextView) dialog.findViewById(R.id.title);
        tile.setText(activity.name+" "+getResources().getString(R.string.notes));

        editnote = (EditText) dialog.findViewById(R.id.editnote);


        editnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editnote.setText(activity.note);

        ImageView clear = (ImageView) dialog.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editnote.setText("");
            }
        });



        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                changesize=true;
                activity.note = editnote.getText().toString().trim();
                showTABLE();
                save();
                changesize=false;
            }
        });

        dialog.show();



    }



    HorizontalScrollView hScroll;
    VScrollView vScroll  ;
    @SuppressLint("ClickableViewAccessibility")
    public void anhxa(){


        vScroll = findViewById(R.id.wscroll);
        hScroll = findViewById(R.id.child_wscroll);

        vScroll.sv = hScroll;

        main_Constrain = (ConstraintLayout) findViewById(R.id.Main_constrain);
        linearday = (LinearLayout) findViewById(R.id.linearday);
        linearhour = (LinearLayout) findViewById(R.id.linearhour);

        scrollhour = (ScrollView) findViewById(R.id.scrollhour);

        scrollday = (HorizontalScrollView) findViewById(R.id.scrollday);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollday.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollday.setScrollX(hScroll.getScrollX());
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollhour.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    scrollhour.setScrollY(vScroll.getScrollY());
                }
            });
        }



        constable = (ConstraintLayout) findViewById(R.id.constable);





//        final float[] BX = {0};
//        final float[] BY = {0};
//        constable.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int FX = (int) constable.getmScaleDetector().getFocusX();
//                int FY = (int) constable.getmScaleDetector().getFocusY();
//                float CX = constable.getmScaleDetector().getCurrentSpanX();
//                float CY = constable.getmScaleDetector().getCurrentSpanY();
//                Resources r = getResources();
//                float pxHeighTB =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,height,r.getDisplayMetrics());
//                float pxWidthTB=  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,width,r.getDisplayMetrics());
//                float pxminW =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minW,r.getDisplayMetrics());
//                float pxmaxW=  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,maxW,r.getDisplayMetrics());
//                float pxminH =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,minH,r.getDisplayMetrics());
//                float pxmaxH=  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,maxH,r.getDisplayMetrics());
//                Log.d("sdfsdfsdfw", "onTouch: "+BX[0]+" "+BY[0]+" "+(CY/BY[0]));
//                if(true) {
//                    float scaleX = CX/BX[0];
//                    float scaleY = CY/BY[0];
//                    if(pxWidthTB*scaleX >= pxminW && pxWidthTB*scaleX <= pxmaxW && pxHeighTB*scaleY<=pxmaxH && pxHeighTB*scaleY>=pxminH){
//                        if(CX>CY){
//                            pxWidthTB= (pxWidthTB*scaleX);
//                            width = pxToDp(pxWidthTB);
//                        }
//                        else{
//                            pxHeighTB= (pxHeighTB*scaleY);
//                            height = pxToDp(pxHeighTB);
//                        }
//                        showTABLE();
//                    }
//                }
//                    BX[0] =constable.getmScaleDetector().getCurrentSpanX();
//                    BY[0] =constable.getmScaleDetector().getCurrentSpanY();
//                return false;
//            }
//        });




//        constable.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                scrollhour.setScrollY(constable.getScrollY());
//                scrollday.setScrollX(constable.getScrollX());
//            }
//        });

        hScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollhour.setScrollY(vScroll.getScrollY());
                scrollday.setScrollX(hScroll.getScrollX());
                Log.d("a", vScroll.getScrollY()+" "+hScroll.getScrollX());
            }
        });



    }



    public void Sort(ArrayList<Interval> a){
        for(int i=0;i<a.size();i++)
        {
            for(int j=i+1;j<a.size();j++)
            {
                if(a.get(i).eventDay>a.get(j).eventDay)
                    Swap(a.get(i),a.get(j));
            }
        }
        for(int i=0; i<a.size();i++)
            for(int j=i+1;j<a.size();j++)
            {
                if(a.get(i).eventDay == a.get(j).eventDay && a.get(i).startTime >a.get(j).startTime)
                {
                    Swap(a.get(i),a.get(j));
                }
            }
    }



    public void Swap(Interval a, Interval b){
        Interval temp = new Interval(b);
        b.set(a);
        a.set(temp);

    }

    boolean out = false;
    @Override
    public void onBackPressed() {



        if(out)
            this.finish();
        else{
            out=true;
            Toast.makeText(this, ""+getResources().getString(R.string.exit), Toast.LENGTH_SHORT).show();

            CountDownTimer countDownTimer = new CountDownTimer(1500,100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    out = false;
                }
            }.start();
        }

    }
    public void add_actionbar_title(String s){
        androidx.appcompat.app.ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_actionbar, null);
        androidx.appcompat.app.ActionBar.LayoutParams params =new androidx.appcompat.app.ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.LEFT);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(s);
        abar.setCustomView(viewActionBar,params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                Intent intent = new Intent(MainActivity.this,Activity_Add.class);
                startActivityForResult(intent,0);
                break;
            case R.id.size:
                PopupSizeChange();
                break;
            case R.id.list:
                Intent intent2 = new Intent(MainActivity.this,Activity_List.class);
                startActivityForResult(intent2,0);
                break;
            case R.id.setting:
                Intent intentSetting = new Intent(MainActivity.this, Activity_Setting.class);
                startActivityForResult(intentSetting,0);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}


