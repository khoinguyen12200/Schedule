package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Activity_List extends AppCompatActivity {

    LinearLayout main ;
    FloatingActionButton add;
    Button delete,cancel;
    LinearLayout delete_back;
    LinearLayout background_array[];
    int id_array[];
    ConstraintLayout mainConstrain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        add_actionbar_title(getResources().getString(R.string.your_event));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainConstrain = (ConstraintLayout) findViewById(R.id.Main_constrain);
        delete_back = (LinearLayout)findViewById(R.id.delete_back);
        main = (LinearLayout) findViewById(R.id.linearMain);
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_List.this,Activity_Add.class);
                startActivityForResult(intent,0);
            }
        });
        delete = (Button) findViewById(R.id.delete);
        cancel = (Button) findViewById(R.id.cancel);

        delete_back.setVisibility(View.GONE);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete_back.getVisibility() == View.VISIBLE)
                    XacNhanXoa();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_state_out();
            }
        });


        mainConstrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_state_out();
            }
        });

        getdata();


        setMain();

    }

    private void delete_state_out(){
        collapse(delete_back);
        for(int i=0;i<background_array.length;i++){
            background_array[i].setSelected(false);
        }
    }

    private void delete_state_in(){
        expand(delete_back);
        for(int i=0;i<background_array.length;i++){
            background_array[i].setSelected(false);
        }
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
    private void XacNhanXoa() { //tạo hàm xác nhận có không
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.warning));

        alertDialog.setMessage(getString(R.string.delete_activity));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { // Tạo nút Có và hành động xóa phần tử trong array
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<background_array.length;i++){
                    if(background_array[i].isSelected()){
                        for(int j=0;j<acti.size();j++){
                            Acti a = (Acti) acti.get(j);
                            if(a.id == id_array[i]){
                                acti.remove(j);
                                for(int k=0;k<intervals.size();k++){
                                    Interval interval = (Interval) intervals.get(k);
                                    if(interval.id == a.id){
                                        intervals.remove(k);
                                        k=0;
                                    }
                                }
                                break;

                            }
                        }
                    }
                }
                savedata();
                getdata();
                setMain();

            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() { // Tạo nút không
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete_state_out();
            }
        });
        alertDialog.show();
    }
    public void setMain(){
        background_array = new LinearLayout[acti.size()];
        id_array = new int[acti.size()];
        main.removeAllViews();
        for(int i=0;i<acti.size();i++){
            final Acti oneActi = (Acti) acti.get(i);
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_item_list,null);
            final LinearLayout background = (LinearLayout) view.findViewById(R.id.background);
            background.setSelected(false);
            ImageView color = (ImageView) view.findViewById(R.id.color);
            TextView name = (TextView) view.findViewById(R.id.name);

            ImageView alarm = (ImageView) view.findViewById(R.id.alarm);
            ImageView noti = (ImageView) view.findViewById(R.id.noti);


            name.setText(oneActi.name);
            alarm.setVisibility(haveann(oneActi.alarmBefore));
            noti.setVisibility(haveann(oneActi.notiBefore));

            ImageViewCompat.setImageTintList(color, ColorStateList.valueOf(oneActi.color));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(delete_back.getVisibility() == View.GONE){
                        Intent intent = new Intent(Activity_List.this,Main2Activity.class);
                        intent.putExtra("id",oneActi.id);
                        startActivityForResult(intent,0);
                    }else{
                        background.setSelected(!background.isSelected());
                    }

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(delete_back.getVisibility() == View.GONE){

                        delete_state_in();
                        CountDownTimer countDownTimer = new CountDownTimer(500,500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                background.setSelected(true);
                            }
                        }.start();
                    }
                    return true;
                }
            });
            background_array[i]=background;
            id_array[i]=oneActi.id;
            main.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getdata();
        setMain();
        delete_state_out();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int haveann(int time){
        if(time == -1)
            return View.INVISIBLE;
        else return View.VISIBLE;
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


    @Override
    public void onBackPressed() {
        if(delete_back.getVisibility()==View.GONE)
            finish();
        else{
            delete_state_out();
        }

    }
}