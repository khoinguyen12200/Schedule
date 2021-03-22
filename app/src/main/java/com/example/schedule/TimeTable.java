package com.example.schedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TimeTable  implements Parcelable{
    public ArrayList<Acti> actis;
    public ArrayList<Interval> intervals;

    public int startTime, endTime, startDay, endDay;
    public float wid,hei;

    public TimeTable() {
        this.actis = new ArrayList<Acti>();
        this.intervals = new ArrayList<Interval>();
        this.startTime = 6;
        this.endTime = 15;
        this.startDay = 0;
        this.endDay = 6;
        this.wid = 200;
        this.hei = 100;
    }

    public TimeTable(ArrayList<Acti> actis, ArrayList<Interval> intervals, int startTime, int endTime, int startDay, int endDay, float wid, float hei) {
        this.actis = actis;
        this.intervals = intervals;

        this.startTime = startTime;
        this.endTime = endTime;
        this.startDay = startDay;
        this.endDay = endDay;
        this.wid = wid;
        this.hei = hei;
    }

    protected TimeTable(Parcel in) {
        actis = in.createTypedArrayList(Acti.CREATOR);
        intervals = in.createTypedArrayList(Interval.CREATOR);

        startTime = in.readInt();
        endTime = in.readInt();
        startDay = in.readInt();
        endDay = in.readInt();
        wid = in.readFloat();
        hei = in.readFloat();
    }

    public static final Creator<TimeTable> CREATOR = new Creator<TimeTable>() {
        @Override
        public TimeTable createFromParcel(Parcel in) {
            return new TimeTable(in);
        }

        @Override
        public TimeTable[] newArray(int size) {
            return new TimeTable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(actis);
        dest.writeTypedList(intervals);

        dest.writeInt(startTime);
        dest.writeInt(endTime);
        dest.writeInt(startDay);
        dest.writeInt(endDay);
        dest.writeFloat(wid);
        dest.writeFloat(hei);
    }


//    protected TimeTable(Parcel in) {
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<TimeTable> CREATOR = new Creator<TimeTable>() {
//        @Override
//        public TimeTable createFromParcel(Parcel in) {
//            return new TimeTable(in);
//        }
//
//        @Override
//        public TimeTable[] newArray(int size) {
//            return new TimeTable[size];
//        }
//    };
}
