package com.example.schedule;


import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Acti implements Parcelable {
    public int id;
    public String name;
    public String describe;
    public int color;
    public String note;
    public int alarmBefore=-1,notiBefore=-1;
    public int mode =0;
    public int weekly = WEEKLY;
    public int autoDelete = NONE_AUTO_DELETE;

    public static int WEEKLY =1;
    public static int ONCE =0;
    public static int AUTO_DELETE =1;
    public static int NONE_AUTO_DELETE =0;
    public  static  int ALARM =1;
    public  static  int NOTIFICATION =2;
    public  static  int DONTHAVE =-1;
    public static  int HAVE_MODE = 3;



    @NonNull
    @Override
    public String toString() {
        if(weekly==WEEKLY){
            return "Weekly !~ id = "+id+"; name="+name+"; des="+describe+"; noti="+notiBefore+"; alarm="+alarmBefore ;
        }else{
            return "Just Once !~ id = "+id+"; name="+name+"; des="+describe+"; noti="+notiBefore+"; alarm="+alarmBefore ;
        }

    }
    public int getHourAlarm(){
        return alarmBefore/60;
    }
    public int getMinuteAlarm(){
        return alarmBefore%60;
    }
    public int getHourNoti(){
        return notiBefore/60;
    }
    public int getMinuteNoti(){
        return notiBefore%60;
    }


    public Acti(int id, String name, String describe, int color) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.color = color;
        this.note = "";
        alarmBefore=-1;
        notiBefore=-1;
        mode =0;
        weekly = WEEKLY;
        autoDelete = NONE_AUTO_DELETE;

    }
    public Acti() {
        this.id = 0;
        this.name = "name";
        this.describe = "describe";
        this.color = Color.BLACK;
        this.note = "";
        alarmBefore=-1;
        notiBefore=-1;
        mode =0;
        weekly = WEEKLY;
        autoDelete = NONE_AUTO_DELETE;
    }

    public Acti( Acti a) {
        id = a.id;
        name = a.name;
        describe = a.describe;
        color = a.color;
        note = a.note;
        alarmBefore=a.alarmBefore;
        notiBefore=a.notiBefore;
        mode=a.mode;
        weekly = a.weekly;
        autoDelete = a.autoDelete;

    }

    protected Acti(Parcel in) {
        id = in.readInt();
        name = in.readString();
        describe = in.readString();
        color = in.readInt();
        note = in.readString();
        alarmBefore = in.readInt();
        notiBefore=in.readInt();
        mode = in.readInt();
        weekly = in.readInt();
        autoDelete = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(describe);
        dest.writeInt(color);
        dest.writeString(note);
        dest.writeInt(alarmBefore);
        dest.writeInt(notiBefore);
        dest.writeInt(mode);
        dest.writeInt(weekly);
        dest.writeInt(autoDelete);
    }

    public static final Creator<Acti> CREATOR = new Creator<Acti>() {
        @Override
        public Acti createFromParcel(Parcel in) {
            return new Acti(in);
        }

        @Override
        public Acti[] newArray(int size) {
            return new Acti[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
