package com.example.schedule;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class ItemWidgetService2 extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ItemWidgetFactory(getApplicationContext(),intent);
    }
    class ItemWidgetFactory implements RemoteViewsFactory{
        private Context context;
        private int appWidgetid;
        private Intent intent;


        ItemWidgetFactory(Context context, Intent intent){
            this.context = context;
            this.appWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
            this.intent = intent;
        }
        ArrayList acti;
        ArrayList intervals;
        String[] ngay ;
        int [] item_arrays;
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

        @Override
        public void onCreate() {
            getngay();
            getdata();



            int item_array[] = new int[intervals.size()+8];
            int vitri=0;
            int ngay=-1;
            for(int i=0;i<intervals.size();i++){
                Interval interval = new Interval((Interval) intervals.get(i));
                if(interval.eventDay!=ngay){
                    ngay=interval.eventDay;
                    item_array[vitri]=ngay;
                    vitri++;
                    item_array[vitri]=i+10;
                    vitri++;
                }
                else{
                    item_array[vitri]=i+10;
                    vitri++;
                }
            }
            item_arrays = new int[vitri];
            for(int i=0;i<vitri;i++)
                item_arrays[i]=item_array[i];


        }
        TimeTable timeTable;

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


        @Override
        public void onDataSetChanged() {
            this.onCreate();

        }


        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return item_arrays.length;
        }



        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views;

            if(item_arrays[position]>=10){
                views = new RemoteViews(context.getPackageName(),R.layout.event_widget_item2);
                Interval interval = (Interval) intervals.get(item_arrays[position]-10);
                Acti activity = null;
                for(int i=0;i<acti.size();i++){
                    Acti temp = (Acti) acti.get(i);
                    if(interval.id == temp.id)
                        activity = new Acti(temp);
                }
//                views.setInt(R.id.layout2,"setBackgroundColor",activity.color);
                views.setTextViewText(R.id.item_widget_time,interval.startString()+"\n"+interval.endString());
                views.setTextViewText(R.id.item_widget_name,activity.name);
                views.setTextViewText(R.id.item_widget_location,interval.location);
                views.setTextColor(R.id.item_widget_name,activity.color);


                Intent configIntent = new Intent(context, MainActivity.class);
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetid, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.item_widget_background, configPendingIntent);


            }
            else{
                views = new RemoteViews(context.getPackageName(),R.layout.item_day);
                views.setTextViewText(R.id.day,ngay[item_arrays[position]]);
                if(position==0)
                    views.setInt(R.id.linewid,"setBackgroundColor",getResources().getColor(R.color.none_color));
                else
                    views.setInt(R.id.linewid,"setBackgroundColor",getResources().getColor(R.color.colorBlack));


            }

            return views;



        }



        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }

        public int getNext(){
            Calendar calendar = Calendar.getInstance();
            int eventday = getweekday(calendar.get(Calendar.DAY_OF_WEEK));
            int time = calendar.get(Calendar.HOUR_OF_DAY)+(calendar.get(Calendar.MINUTE)/60);

            int position =0;
            for(int i=0;i<intervals.size();i++){

                Interval interval = (Interval) intervals.get(i);
                if(eventday<interval.eventDay)
                    position=i;
                else if(eventday==interval.eventDay && time < interval.endTime )
                    position=i;
            }
            return position;

        }
        public int getweekday(int day){
            int eventday=0;
            switch (day) {
                case 2 : eventday =0; break;
                case 3 : eventday =1; break;
                case 4 : eventday =2; break;
                case 5 : eventday =3; break;
                case 6 : eventday =4; break;
                case 7 : eventday =5; break;
                case 1 : eventday =6; break;
            }
            return eventday;
        }
//        public void sort(){
//            int ngaydautien = getNext();
//            for(int i=0;i<ngaydautien;i++){
//                Interval interval = new Interval((Interval) intervals.get(0));
//                intervals.remove(0);
//                intervals.add(intervals.size(),interval);
//            }
//        }
    }


}
