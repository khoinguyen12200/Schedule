package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Activity_Setting_Color extends AppCompatActivity {

    ImageView backgroundPicker,textPicker,cellPicker;
    int backBarColor = Color.WHITE;
    int textBarColor = Color.BLACK;
    int cellColor =0;
    Switch cell_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__color);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add_actionbar_title(getResources().getString(R.string.event_color));

        getdata();

        cellPicker = (ImageView) findViewById(R.id.cell_color);
        cell_switch = (Switch) findViewById(R.id.cell_radio);
        cellColor = settingData.cellColor;
        if(settingData.cellColor ==0){
            cell_switch.setChecked(true);
            cellColor=0;
        }
        else cell_switch.setChecked(false);

        cell_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cell_switch.isChecked()){
                    cellColor=0;
                    settingData.cellColor=0;
                }
                else{
                    cellColor=Color.WHITE;
                    settingData.cellColor = Color.WHITE;
                }
                setcolor();
            }
        });
        cellPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cell_switch.isChecked())
                    openColorPicker(3);
            }
        });
        backgroundPicker = (ImageView) findViewById(R.id.backgroundcolor);
        textPicker = (ImageView) findViewById(R.id.textcolor);

        backBarColor = settingData.backBarColor;
        textBarColor = settingData.textBarColor;
        setcolor();

        backgroundPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(1);
            }
        });

        textPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(2);
            }
        });

    }
    private void openColorPicker(final int status) {

        if(status==1){
            AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, backBarColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {

                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    backBarColor = color;
                    settingData.backBarColor = backBarColor;
                    setcolor();
                }
            });
            ambilWarnaDialog.show();
        }
        else if(status ==2 ){

            AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, textBarColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    textBarColor = color;
                    settingData.textBarColor = textBarColor;
                    setcolor();
                }
            });
            ambilWarnaDialog.show();
        }
        else if(status ==3){
            AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, cellColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    cellColor = color;
                    settingData.cellColor = cellColor;
                    setcolor();
                }
            });
            ambilWarnaDialog.show();
        }

    }

    public void setcolor(){

        ImageViewCompat.setImageTintList(backgroundPicker, ColorStateList.valueOf(backBarColor));

        ImageViewCompat.setImageTintList(textPicker, ColorStateList.valueOf(textBarColor));

        ImageViewCompat.setImageTintList(cellPicker, ColorStateList.valueOf(cellColor));



        savedata();


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

}