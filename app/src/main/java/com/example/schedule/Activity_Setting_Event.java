package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Activity_Setting_Event extends AppCompatActivity {

    LinearLayout showlayout;
    Interval interval;
    Acti activity;


    SettingData settingData;
    TimeTable timeTable;
    ArrayList acti;
    ArrayList intervals;
    float width;
    float height;
    int giobatdau=0,gioketthuc=0;
    int ngaybatdau=0,ngayketthuc=0;




    RadioButton classic;
    RadioButton corner;
    RadioButton balance;

    TextView textViewSize;

    CheckBox CBname;
    CheckBox CBtime;
    CheckBox CBlocation;
    CheckBox CBdescribe;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_actionbar_title(getResources().getString(R.string.event_card));

        getdata();


        CBname = (CheckBox) findViewById(R.id.name);
        CBtime = (CheckBox) findViewById(R.id.time);
        CBlocation = (CheckBox) findViewById(R.id.location);
        CBdescribe = (CheckBox) findViewById(R.id.describe);

        if(settingData.showName==SettingData.SHOW){
            CBname.setChecked(true);
        }
        CBname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.showName = SettingData.SHOW;
                else
                    settingData.showName = SettingData.HIDE;
                ItemView();
            }
        });
        if(settingData.showTime==SettingData.SHOW){
            CBtime.setChecked(true);
        }
        CBtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.showTime = SettingData.SHOW;
                else
                    settingData.showTime = SettingData.HIDE;
                ItemView();
            }
        });
        if(settingData.showLocation==SettingData.SHOW){
            CBlocation.setChecked(true);
        }
        CBlocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.showLocation = SettingData.SHOW;
                else
                    settingData.showLocation = SettingData.HIDE;
                ItemView();
            }
        });
        if(settingData.showDescribe==SettingData.SHOW){
            CBdescribe.setChecked(true);
        }
        CBdescribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.showDescribe = SettingData.SHOW;
                else
                    settingData.showDescribe = SettingData.HIDE;
                ItemView();
            }
        });










        showlayout = (LinearLayout) findViewById(R.id.showlayout);
        activity = new Acti(0,getResources().getString(R.string.exname),getResources().getString(R.string.exdes),getResources().getColor(R.color.colorPrimaryBlue));
        interval = new Interval(0,0,8,10,0);
        interval.location = getString(R.string.location);


        classic = (RadioButton) findViewById(R.id.classic);
        corner = (RadioButton) findViewById(R.id.corners);
        balance = (RadioButton) findViewById(R.id.balance);

        textViewSize = (TextView) findViewById(R.id.textviewsize);









        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(10);
        seekBar.setProgress(4);

        textViewSize.setText(settingData.textSize+"");
        seekBar.setProgress(settingData.textSize-10);



        if(settingData.itemType==1){
            classic.setChecked(true);
        }
        else if(settingData.itemType==2)
        {
            corner.setChecked(true);

        }
        else if(settingData.itemType==3)
        {
            balance.setChecked(true);

        }

        ItemView();


        classic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ItemView();
                    settingData.itemType=1;
                }
                savedata();
            }
        });
        corner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ItemView();
                    settingData.itemType=2;
                }
                savedata();

            }
        });
        balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ItemView();
                    settingData.itemType=3;
                }
                savedata();
            }
        });


        RadioGroup radioGroup = findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ItemView();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                settingData.textSize=progress+10;
                textViewSize.setText((progress+10)+"");
                ItemView();
                savedata();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    public void add_actionbar_title(String s){
        androidx.appcompat.app.ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_actionbar, null);
        androidx.appcompat.app.ActionBar.LayoutParams params =new androidx.appcompat.app.ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(s);
        abar.setCustomView(viewActionBar,params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
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

    public void savedata(){
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

    private void ItemView() {

        savedata();
        if(classic.isChecked())
            ItemView1(showlayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,activity,interval);
        else if(corner.isChecked())
            ItemView2(showlayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,activity,interval);
        else if(balance.isChecked())
            ItemView3(showlayout,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,activity,interval);

    }


    public void save(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else finish();
    }



    public void ItemView1(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, Interval interval){
        linearColum.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview1_layout,null);

        TextView time1 = (TextView) view.findViewById(R.id.time1);
        TextView time2 = (TextView) view.findViewById(R.id.time2);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView des = (TextView) view.findViewById(R.id.des);
        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);
        ImageView imageView = (ImageView) view.findViewById(R.id.line);
        imageView.setBackgroundColor(activity.color);

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


        time1.setText(interval.startString());
        time2.setText(interval.endString());
        name.setText(activity.name);
        name.setBackgroundColor(activity.color);

        des.setText(activity.describe);
        time1.setTextSize(settingData.textSize);
        time2.setTextSize(settingData.textSize);
        time1.setTextColor(activity.color);
        time2.setTextColor(activity.color);
        name.setTextSize(settingData.textSize);
        des.setTextSize(settingData.textSize-1);
        constraintLayout.setBackgroundColor(activity.color);
        note.setVisibility(View.INVISIBLE);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);

        linearColum.addView(constraintLayout);






    }

    public void ItemView2(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, Interval interval){
        linearColum.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview2_layout,null);

        TextView time1 = (TextView) view.findViewById(R.id.time1);
        TextView time2 = (TextView) view.findViewById(R.id.time2);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView des = (TextView) view.findViewById(R.id.des);
        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);

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
        constraintLayout.setBackgroundColor(activity.color);
        note.setVisibility(View.INVISIBLE);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {activity.color,activity.color});
        float mRadius = 23f;
        gd.setCornerRadius(mRadius);
        constraintLayout.setBackgroundDrawable(gd);
        linearColum.addView(constraintLayout);





    }

    public void ItemView3(LinearLayout linearColum, int pxWidthTB, int heightThisView, final Acti activity, Interval interval){
        linearColum.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.itemview3_layout,null);

        TextView time1 = (TextView) view.findViewById(R.id.time1);
        TextView time2 = (TextView) view.findViewById(R.id.time2);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView des = (TextView) view.findViewById(R.id.des);
        ImageView note = (ImageView) view.findViewById(R.id.note);
        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.full);

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
        constraintLayout.setBackgroundColor(activity.color);


        note.setVisibility(View.INVISIBLE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxWidthTB,heightThisView);


        constraintLayout.setLayoutParams(layoutParams);

        linearColum.addView(constraintLayout);


    }

    private void XacNhanThoat() { //tạo hàm xác nhận có không
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.warning));

        alertDialog.setMessage(getResources().getString(R.string.without_saving));
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() { // Tạo nút Có và hành động xóa phần tử trong array
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
                else finish();

            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() { // Tạo nút không
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        save();
    }
}
