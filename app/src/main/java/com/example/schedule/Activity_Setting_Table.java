package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import me.bendik.simplerangeview.SimpleRangeView;

public class Activity_Setting_Table extends AppCompatActivity {
    String[] ngay ;




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

    RadioButton show1,show2,show3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__table_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_actionbar_title(getResources().getString(R.string.table));
        getngay();

        getdata();



        final TextView time1 = (TextView) findViewById(R.id.time1);
        final TextView time2 = (TextView) findViewById(R.id.time2);
        SimpleRangeView timeRange = (SimpleRangeView) findViewById(R.id.rangetime);
        final TextView day1 = (TextView) findViewById(R.id.day1);
        final TextView day2 = (TextView) findViewById(R.id.day2);
        SimpleRangeView dayRange = (SimpleRangeView) findViewById(R.id.rangeday);



        show1 = (RadioButton) findViewById(R.id.show1);
        show2 = (RadioButton) findViewById(R.id.show2);
        show3 = (RadioButton) findViewById(R.id.show3);

        if(settingData.RBT == 0)
            show1.setChecked(true);
        else if(settingData.RBT == 1)
            show2.setChecked(true);
        else if(settingData.RBT == 2)
            show3.setChecked(true);


        show1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.RBT=0;
                savedata();
            }
        });
        show2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.RBT=1;
                savedata();
            }
        });
        show3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    settingData.RBT=2;
                savedata();
            }
        });


        time1.setText(giobatdau+"");
        time2.setText(gioketthuc+"");
        day1.setText(ngay[ngaybatdau]);
        day2.setText(ngay[ngayketthuc]);
        timeRange.setStart(giobatdau);
        timeRange.setEnd(gioketthuc);
        dayRange.setStart(ngaybatdau);
        dayRange.setEnd(ngayketthuc);




        timeRange.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                time1.setText(i+"");
                giobatdau=i;

                savedata();
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                time2.setText(i+"");
                gioketthuc=i;

                savedata();
            }
        });
        dayRange.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                day1.setText(ngay[i]);
                ngaybatdau=i;
                savedata();
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                day2.setText(ngay[i]);
                ngayketthuc=i;

                savedata();
            }
        });

    }
    public void save(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else finish();
    }

    TimeTable timeTable;
    ArrayList acti;
    ArrayList intervals;
    float width;
    float height;
    int giobatdau=0,gioketthuc=0;
    int ngaybatdau=0,ngayketthuc=0;
    SettingData settingData;

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

}

