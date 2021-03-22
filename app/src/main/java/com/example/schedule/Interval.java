package com.example.schedule;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Interval implements Parcelable {
    public int id;
    public int idEvent;
    public double startTime, endTime;
    public int eventDay;
    public String location="";

    public int ngay,thang,nam;

    public static int NORMAL =0;
    public static int VIBRATE =1;
    public static int SILENT =2;




    @NonNull
    @Override
    public String toString() {
        return "id = "+id+"; idEvent = "+idEvent+"; start = "+startTime+"; end = "+endTime+"; eventDay = "+eventDay;
    }

    public Interval(){
        this.id = -1;
        this.idEvent=-1;
        this.startTime = 9;
        this.endTime = 10;
        this.eventDay = 0;
        location="";
        ngay=-1;
        thang=-1;
        nam=-1;
    }
    public Interval(int id,int idEvent, double startTime, double endTime, int eventDay) {
        this.id = id;
        this.idEvent=idEvent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventDay = eventDay;
        location="";
        ngay=-1;
        thang=-1;
        nam=-1;
    }


    public Interval(Interval a){
        id = a.id;
        idEvent = a.idEvent;
        startTime = a.startTime;
        endTime = a.endTime;
        eventDay = a.eventDay;
        this.location = a.location;
        ngay=a.ngay;
        thang=a.thang;
        nam=a.nam;
    }
    public Interval getCopy(){
        Interval interval = new Interval(id,idEvent,startTime,endTime,eventDay);
        interval.location = location;
        return interval;
    }
    public int getDayStandar(){
        int day = 0;
        switch (eventDay) {
            case 0 : day =2; break;
            case 1 : day =3; break;
            case 2 : day =4; break;
            case 3 : day =5; break;
            case 4 : day =6; break;
            case 5 : day =7; break;
            case 6 : day =1; break;
        }
        return day;
    }
    public void set(Interval a){
        this.id = a.id;
        this.idEvent = a.idEvent;
        this.startTime = a.startTime;
        this.endTime = a.endTime;
        this.eventDay = a.eventDay;
        this.location = a.location;
        ngay=a.ngay;
        thang=a.thang;
        nam=a.nam;
    }

    protected Interval(Parcel in) {
        id = in.readInt();
        idEvent =in.readInt();
        startTime = in.readDouble();
        endTime = in.readDouble();
        eventDay = in.readInt();
        location = in.readString();
        ngay=in.readInt();
        thang=in.readInt();
        nam=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idEvent);
        dest.writeDouble(startTime);
        dest.writeDouble(endTime);
        dest.writeInt(eventDay);
        dest.writeString(location);
        dest.writeInt(ngay);
        dest.writeInt(thang);
        dest.writeInt(nam);
    }

    public Calendar getDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,nam);
        calendar.set(Calendar.MONTH,thang);
        calendar.set(Calendar.DATE,ngay);
        return calendar;
    }
    public Calendar getCalendarStart(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,nam);
        calendar.set(Calendar.MONTH,thang);
        calendar.set(Calendar.DATE,ngay);
        calendar.set(Calendar.HOUR_OF_DAY,giobatdau());
        calendar.set(Calendar.MINUTE,phutbatdau());
        return calendar;
    }
    public Calendar getCalendarEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,nam);
        calendar.set(Calendar.MONTH,thang);
        calendar.set(Calendar.DATE,ngay);
        calendar.set(Calendar.HOUR_OF_DAY,gioketthuc());
        calendar.set(Calendar.MINUTE,phutketthuc());
        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Interval> CREATOR = new Creator<Interval>() {
        @Override
        public Interval createFromParcel(Parcel in) {
            return new Interval(in);
        }

        @Override
        public Interval[] newArray(int size) {
            return new Interval[size];
        }
    };


    public int lamtronso(Double a){
        if(a%1>=0.5)
            return (int) Math.ceil(a);
        else return (int) Math.floor(a);
    }

    public String timeString(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String s ="";

        double a = startTime%1;
        a=a*60;
        calendar.set(0,0,0,(int)Math.floor(startTime), lamtronso(a));
        String start = simpleDateFormat.format(calendar.getTime());

        double b = endTime%1;
        b=b*60;
        calendar.set(0,0,0,(int)Math.floor(endTime), lamtronso(b));
        String end = simpleDateFormat.format(calendar.getTime());
        s=start+" - "+end;
        return s;
    }
    public Calendar timeToCalendar(){
        Calendar calendar = Calendar.getInstance();
        double a = startTime%1;
        a=a*60;
        calendar.set(Calendar.HOUR_OF_DAY,(int)Math.floor(startTime));
        calendar.set(Calendar.MINUTE,lamtronso(a));
        calendar.set(Calendar.SECOND,00);

        return calendar;
    }
    public Calendar timeEndToCalendar(){
        Calendar calendar = Calendar.getInstance();
        double a = endTime%1;
        a=a*60;
        calendar.set(Calendar.HOUR_OF_DAY,(int)Math.floor(endTime));
        calendar.set(Calendar.MINUTE,lamtronso(a));
        calendar.set(Calendar.SECOND,00);
        return calendar;
    }

    public int giobatdau(){
        return (int)Math.floor(startTime);
    }
    public int phutbatdau(){
        double a = startTime%1;
        a=a*60;
        lamtronso(a);
        return (int) a;
    }
    public int gioketthuc(){
        return (int)Math.floor(endTime);
    }
    public int phutketthuc(){
        double a = endTime%1;
        a=a*60;
        lamtronso(a);
        return (int) a;
    }


    public String startString(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        double a = startTime%1;
        a=a*60;
        calendar.set(0,0,0,(int)Math.floor(startTime), lamtronso(a));

        String start = simpleDateFormat.format(calendar.getTime());

        return start;
    }
    public String endString(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String s ="";

        double b = endTime%1;
        b=b*60;
        calendar.set(0,0,0,(int)Math.floor(endTime), lamtronso(b));
        String end = simpleDateFormat.format(calendar.getTime());
        s=s+end;
        return s;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public int getEventDay() {
        return eventDay;
    }

    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }

    public String eventDay() {
        String s = eventDay+"";
        return  s;
    }
}
