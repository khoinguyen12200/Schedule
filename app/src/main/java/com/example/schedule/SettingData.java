package com.example.schedule;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class SettingData implements Parcelable{
    public String audioAlarmName="baothuc1";
    public Boolean alarmHaveVibration=false;
    public String audioNotiName="none";
    public Boolean notiHaveVibration=false;
    public int itemType =1;
    public int textSize =11;
    public int backBarColor = Color.WHITE;
    public int textBarColor = Color.BLACK;
    public int cellColor=0;
    public int RBT = 0; // remove blank time
    public int showName = SHOW;
    public int showTime = SHOW;
    public int showLocation = SHOW;
    public int showDescribe = SHOW;

    public static int HIDE = 0;
    public static int SHOW = 1;
    public static int DAY_IS_COLUMN = 1;
    public static int DAY_IS_ROW = 0;

    public static String CHANNEL_NOTIFICATION = "CHANNEL_NOTIFICATION";
    public static String CHANNEL_ALARM= "CHANNEL_ALARM";

    public SettingData() {
        this.audioAlarmName = "baothuc1";
        this.alarmHaveVibration = false;
        this.audioNotiName = "none";
        this.notiHaveVibration = false;
        itemType =1;
        textSize =11;
        backBarColor = Color.WHITE;
        textBarColor = Color.BLACK;
        cellColor = 0;
        RBT=0;
        showName=SHOW;
        showTime=SHOW;
        showLocation=SHOW;
        showDescribe=SHOW;

    }
    public SettingData( SettingData a) {
        this.audioAlarmName = a.audioAlarmName;
        this.alarmHaveVibration = a.alarmHaveVibration;
        this.audioNotiName = a.audioNotiName;
        this.notiHaveVibration = a.notiHaveVibration;
        itemType =a.itemType;
        textSize = a.textSize;
        backBarColor = a.backBarColor;
        textBarColor = a.textBarColor;
        cellColor = a.cellColor;
        RBT = a.RBT;
        showName=a.showName;
        showTime=a.showTime;
        showLocation=a.showLocation;
        showDescribe=a.showDescribe;

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audioAlarmName);
        dest.writeByte((byte) (alarmHaveVibration == null ? 0 : alarmHaveVibration ? 1 : 2));
        dest.writeString(audioNotiName);
        dest.writeByte((byte) (notiHaveVibration == null ? 0 : notiHaveVibration ? 1 : 2));
        dest.writeInt(itemType);
        dest.writeInt(textSize);
        dest.writeInt(backBarColor);
        dest.writeInt(textBarColor);
        dest.writeInt(cellColor);
        dest.writeInt(RBT);
        dest.writeInt(showName);
        dest.writeInt(showTime);
        dest.writeInt(showLocation);
        dest.writeInt(showDescribe);

    }

    protected SettingData(Parcel in) {
        audioAlarmName = in.readString();
        byte tmpAlarmHaveVibration = in.readByte();
        alarmHaveVibration = tmpAlarmHaveVibration == 0 ? null : tmpAlarmHaveVibration == 1;
        audioNotiName = in.readString();
        byte tmpNotiHaveVibration = in.readByte();
        notiHaveVibration = tmpNotiHaveVibration == 0 ? null : tmpNotiHaveVibration == 1;
        itemType = in.readInt();
        textSize = in.readInt();
        backBarColor = in.readInt();
        textBarColor = in.readInt();
        cellColor = in.readInt();
        RBT = in.readInt();
        showName=in.readInt();
        showTime=in.readInt();
        showLocation=in.readInt();
        showDescribe=in.readInt();


    }

    public static final Creator<SettingData> CREATOR = new Creator<SettingData>() {
        @Override
        public SettingData createFromParcel(Parcel in) {
            return new SettingData(in);
        }

        @Override
        public SettingData[] newArray(int size) {
            return new SettingData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
