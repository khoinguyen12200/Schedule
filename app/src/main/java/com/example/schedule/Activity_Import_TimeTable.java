package com.example.schedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;

public class Activity_Import_TimeTable extends AppCompatActivity {

    Button nhan;
    WebView webView;
    int [] ngay = {2,3,4,5,6,7,8};
    InTime[] mangTietHoc;

    int fristTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_time_table);
        add_actionbar_title(getResources().getString(R.string.import_timetable));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getdata();


        String tiethoc = getResources().getString(R.string.TKB_TietHoc);
        String [] tiet = tiethoc.split(" ");
        mangTietHoc = new InTime[tiet.length];
        for(int i=0;i<tiet.length;i++){
            String[] t = tiet[i].split("-");

            int gio0 = Integer.parseInt(t[0].split(":")[0]);
            int phut0 = Integer.parseInt(t[0].split(":")[1]);

            int gio1 = Integer.parseInt(t[1].split(":")[0]);
            int phut1 = Integer.parseInt(t[1].split(":")[1]);

            mangTietHoc[i] = new InTime(new Time(gio0,phut0), new Time(gio1,phut1));

        }

        webView = (WebView) findViewById(R.id.webview);
        nhan = (Button) findViewById(R.id.nhan);



        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(getResources().getString(R.string.URL_CTU));
        webView.getSettings().setBuiltInZoomControls(true);
        nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laytatcamonhoc();
            }
        });




        fristTime = 0;



    }

    public void them(){



        for(int i=0;i<TenMon.size();i++){


            Log.d("asdfwwooooooo", "1");
            final int [] color_int = getResources().getIntArray(R.array.color_list);
            int random = new Random().nextInt(color_int.length);
            int color = color_int[random];

            Log.d("asdfwwooooooo", color+"");
            int id = haveMaMon(TenMon.get(i));
            Log.d("asdfwwooooooo", id+"id");
            if(id==-1){
                id = acti.size();
                Acti activity = new Acti(id,TenMon.get(i),MaMon.get(i)+"/"+MaNhom.get(i), color);
                acti.add(activity);
                Interval interval = new Interval(id,intervals.size(),getStart(TietHoc.get(i)),getEnd(TietHoc.get(i)),NgayHoc.get(i)-2);
                interval.location = PhongHoc.get(i);

                Log.d("asdfwwooooooo", "size"+intervals.size());
                intervals.add(interval);

            }else{
                Interval interval = new Interval(id,intervals.size(),getStart(TietHoc.get(i)),getEnd(TietHoc.get(i)),NgayHoc.get(i)-2);
                interval.location = PhongHoc.get(i);
                intervals.add(interval);
            }
        }
        savedata();
        finish();

    }

    public float getStart(String s){
        String time = s.replaceAll("\\*","");
        time = time.substring(0,1);
        int tietbatdau = Integer.parseInt(time);
        return mangTietHoc[tietbatdau-1].start.tofloat();
    }
    public float getEnd(String s){
        String time = s.replaceAll("\\*","");
        time = time.substring(time.length()-1);
        int tietketthuc = Integer.parseInt(time);
        return mangTietHoc[tietketthuc-1].end.tofloat();
    }



    public class InTime{
        Time start,end;

        public InTime(Time start, Time end) {
            this.start = start;
            this.end = end;
        }
        public String toString(){
            return start.tofloat()+"-"+end.tofloat();
        }
    }
    public class Time {
        int hour,minute;

        public Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }
        public float tofloat(){
            float a = hour+((float)minute/60);
            return a;
        }
    }

    public int haveMaMon(String tenMon){

        for(int i=0;i<acti.size();i++){
            Acti a = (Acti) acti.get(i);
            if(tenMon.equals(a.name)){
                return i;
            }
            if(i==acti.size()-1)
                return -1;
        }
        return -1;

    }

    ArrayList<String> MaMon,MaNhom,TenMon,TietHoc,PhongHoc;
    ArrayList<Integer> NgayHoc;

    public void laytatcamonhoc(){
        MaMon = new ArrayList<String>();
        MaNhom = new ArrayList<String>();
        TenMon = new ArrayList<String>();
        TietHoc = new ArrayList<String>();
        PhongHoc = new ArrayList<String>();
        NgayHoc = new ArrayList<Integer>();

        gets(0,2);


    }

    String [] hoc_array ={"cell_mamonhoc_","cell_manhom_","cell_tenmonhoc_","cell_tiethoc_","cell_phong_"};
    String string;
    public String gets( final int loai_hoc, final int thu){

        Log.d("loaihoac", hoc_array[loai_hoc]+thu);
        String s1= hoc_array[loai_hoc]+thu;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(
                    "(function() { return (document.getElementById('"+s1+"').innerHTML); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            if(html.equals("null")){
                                sourceReceived(getResources().getString(R.string.data_error));
                                return;
                            }

                            String temp = html.replaceAll("\"","");
                            String [] a = temp.split("\\\\u003Cbr>");
                            for(int i=0;i<a.length;i++){
                                if(!a[i].equals("&nbsp;")){
                                    switch (loai_hoc){
                                        case 0 :
                                            MaMon.add(a[i]);
                                            NgayHoc.add(thu);
                                            break;
                                        case 1 : MaNhom.add(a[i]); break;
                                        case 2 : TenMon.add(a[i]); break;
                                        case 3 : TietHoc.add(a[i]); break;
                                        case 4 : PhongHoc.add(a[i]); break;

                                    }
                                }

                            }
                            Log.d("loaihoac", a[0]);


                            if(thu<8)
                                gets(loai_hoc,thu+1);
                            else{
                                if(loai_hoc<hoc_array.length-1)
                                    gets(loai_hoc+1,2);
                                else{
                                    show();
                                    String s="";
                                    for(int i=0;i<NgayHoc.size();i++){
                                        s+= NgayHoc.get(i)+" ";

                                    }
                                    Log.d("TAGngayhoc", s);
                                }
                            }

                        }
                    }
            );
        }

        return string;
    }
    private void Xacnhanthem(String s) { //tạo hàm xác nhận có không
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.import_timetable));

        alertDialog.setMessage(s);
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { // Tạo nút Có và hành động xóa phần tử trong array
            @Override
            public void onClick(DialogInterface dialog, int which) {
                them();
                savedata();
                getdata();


            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() { // Tạo nút không
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    public void show(){
        String s="";

        if(MaMon !=null){
            for(int i=0;i<MaMon.size();i++){
                s+= MaMon.get(i) + " " +MaNhom.get(i) + " " +TenMon.get(i) + " " +TietHoc.get(i).replaceAll("\\*","") + " " +PhongHoc.get(i) + "\n";
            }
            Xacnhanthem(s);
        }
    }


    public class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);

//            if (url.startsWith("source://")) {
//                try {
//                    String html = URLDecoder.decode(url, "UTF-8").substring(9);
//                    sourceReceived(html);
//                } catch (UnsupportedEncodingException e) {
//                    Log.d("example", "failed to decode source", e);
//                }
//                webView.loadUrl(url);
//                webView.getSettings().setJavaScriptEnabled(false);
//                return true;
//            }
            // For all other links, let the WebView do it's normal thing
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if(fristTime<4){
                if(url.equals(getResources().getString(R.string.URL_CTU)) && fristTime==0){
                    webView.loadUrl(getResources().getString(R.string.URL_HTQL));
                    fristTime++;
                }

                if(url.equals(getResources().getString(R.string.URL_Sinhvien)) && fristTime==1){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(getResources().getString(R.string.URL_DKHP));
                            fristTime++;
                        }
                    }, 500);
                }

                if(url.equals(getResources().getString(R.string.URL_DKHP)) && fristTime==2){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(getResources().getString(R.string.URL_TKB));
                            fristTime++;
                        }
                    }, 500);
                }
                if(url.equals(getResources().getString(R.string.URL_TKB)) && fristTime==3){
                    laytatcamonhoc();
                    fristTime++;

                }


            }




            super.onPageFinished(view, url);
        }
    }
    private void sourceReceived(String html) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(html);
        builder.setTitle("View Source");
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
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