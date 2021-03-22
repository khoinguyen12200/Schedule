package com.example.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    EditText name, des;

    ConstraintLayout mainConstrain;
    Acti activity;
    String[] ngay;
    public static int MAXINTERVAL = 100;
    LinearLayout layoutTimeline;
    ScrollView scrollView;
    Interval[] listInterval = new Interval[100];

    int i=0;
    Interval intervalx;

    int idEvent=0;

    ArrayList intervals;
    String phutArray[] ;


    int NORMAL_MODE =0,VIBRATE_MODE =1,SILENT_MODE =2;

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
    int check =-1;

    CheckBox alarmSwitch;

    CheckBox notiSwitch;

    RadioButton noneRadio;
    RadioButton vibrateRadio;
    RadioButton silentRadio;
    int [] color_int;

    int step =1;
    TextView notiB ;
    TextView alarmB;

    TextView colorTV;
    LinearLayout back_alarm, back_noti;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getngay();

        getdata();


        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra("id",-1);

        for(int i=0;i<acti.size();i++){
            Acti ac = (Acti) acti.get(i);
            if(ac.id == id){
                activity = new Acti(ac);
                position =i;
            }
        }
        if(id==-1 || activity == null)
            finish();

        scrollView = (ScrollView) findViewById(R.id.scrollView3);
        name = (EditText) findViewById(R.id.name);
        des = (EditText) findViewById(R.id.des);
        colorTV = (TextView) findViewById(R.id.colorTV);
        colorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color_dialog(colorTV,activity);
            }
        });
        colorTV.setTextColor(activity.color);

        layoutTimeline = (LinearLayout) findViewById(R.id.layouttimeline);




        ImageView add = (ImageView) findViewById(R.id.add);



        alarmSwitch = (CheckBox) findViewById(R.id.alarm);

        notiSwitch = (CheckBox) findViewById(R.id.noti);

         notiB = (TextView) findViewById(R.id.notiBefore);
         alarmB = (TextView) findViewById(R.id.alarmBefore);
         back_alarm = findViewById(R.id.back_alarm);
         back_noti = findViewById(R.id.back_noti);





        noneRadio = (RadioButton) findViewById(R.id.normalRadio);
        vibrateRadio = (RadioButton) findViewById(R.id.vibrateRadio);
        silentRadio = (RadioButton) findViewById(R.id.silentRadio);

        scrollView.setSmoothScrollingEnabled(true);











        Log.d("davap", "onCreate: "+intervals.size());
        for(int i=0;i<intervals.size();i++){
            Interval interval1 = (Interval) intervals.get(i);
            Log.d("davap", interval1.toString());
            if(interval1.id==activity.id)
                intervalx = interval1;
        }


        Log.d("davap", "onCreate: ");
        if(activity.alarmBefore!=Acti.DONTHAVE){
            alarmSwitch.setChecked(true);
            setting_on_off(Acti.ALARM,true);
            setTimeView(Acti.ALARM,alarmB,activity.alarmBefore);

        }
        else{
            setTimeView(Acti.ALARM,alarmB,activity.alarmBefore);
            alarmSwitch.setChecked(false);
            setting_on_off(Acti.ALARM,false);
        }

        if(activity.notiBefore!=Acti.DONTHAVE){
            notiSwitch.setChecked(true);
            setting_on_off(Acti.NOTIFICATION,true);
            setTimeView(Acti.NOTIFICATION,notiB,activity.notiBefore);

        }
        else{
            setTimeView(Acti.NOTIFICATION,notiB,activity.notiBefore);
            notiSwitch.setChecked(false);
            setting_on_off(Acti.NOTIFICATION,false);
        }



        if(activity.mode == NORMAL_MODE)
            noneRadio.setChecked(true);
        else if(activity.mode == VIBRATE_MODE)
            vibrateRadio.setChecked(true);
        else if(activity.mode == SILENT_MODE )
            silentRadio.setChecked(true);



        name.setText(activity.name);
        des.setText(activity.describe);




        for( i=0;i<intervals.size();i++){
            Interval interval = new Interval((Interval) intervals.get(i));
            if(interval.id == activity.id){
                addTimeLine(layoutTimeline,interval);
            }
            if(idEvent<interval.idEvent)
                idEvent=interval.idEvent;
        }
        idEvent++;
        for( i=intervals.size()-1;i>=0;i--){
            Interval interval = new Interval((Interval) intervals.get(i));
            if(interval.id == activity.id){
                intervals.remove(i);
            }
        }



        back_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                before_dialog(Acti.ALARM);
            }
        });
        back_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                before_dialog(Acti.NOTIFICATION);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeLine(layoutTimeline);
            }
        });

        if(activity.weekly == Acti.ONCE){
            add.setVisibility(View.GONE);
            layoutTimeline.removeAllViews();
            addOncePicker(layoutTimeline);
        }

        color_int = getResources().getIntArray(R.array.color_list);








    }
    private void color_dialog(final TextView textView, final  Acti acti){

        final Dialog  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.color_picker_circle);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.nothing);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout back = (LinearLayout) dialog.findViewById(R.id.background);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout main = (LinearLayout) dialog.findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        final Button okButton = dialog.findViewById(R.id.ok);

        FlexboxLayout flexboxLayout = (FlexboxLayout) dialog.findViewById(R.id.flexbox);
        final int [] color_int = getResources().getIntArray(R.array.color_list);
        final ImageView[] oval = new ImageView[color_int.length];
        int item_perrow =5;
        int item =0;
        Resources r = getResources();
        int img = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,r.getDisplayMetrics());

        check=-1;
        while(item<color_int.length){

            for(int j=0;j<item_perrow;j++){
                if(item>=color_int.length)
                    break;
                oval[item] = new ImageView(this);
                GradientDrawable gdtv = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {color_int[item],color_int[item]});
                gdtv.setCornerRadii(new float[]{img/2, img/2, img/2, img/2, img/2, img/2, img/2, img/2});
                gdtv.setStroke(3,Color.BLACK);


                oval[item].setBackgroundColor(color_int[item]);
                oval[item].setMinimumWidth(img);
                oval[item].setMinimumHeight(img);

                oval[item].setBackground(gdtv);

                final int finalItem = item;
                oval[item].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(check!= finalItem){
                            if(check==-1){

                                oval[finalItem].setImageResource(R.drawable.ic_check_black_24dp);
                                check=finalItem;
                            }
                            else{

                                oval[check].setImageResource(R.drawable.nothing);
                                oval[finalItem].setImageResource(R.drawable.ic_check_black_24dp);
                                check=finalItem;
                            }
                        }
                    }
                });
                if(acti.color!=-1 && acti.color == color_int[item]){
                    oval[item].setImageResource(R.drawable.ic_check_black_24dp);
                    check=item;
                }
                Animation pop_up = AnimationUtils.loadAnimation(this,R.anim.popup2);
                pop_up.setStartOffset(item*30);
                oval[item].setAnimation(pop_up);

                LinearLayout paddinglayout = new LinearLayout(this);
                paddinglayout.setPadding(img/4,img/4,img/4,img/4);
                paddinglayout.addView(oval[item]);
                flexboxLayout.addView(paddinglayout);
                item++;
            }
        }


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(check!=-1){
                    acti.color = color_int[check];
                    textView.setTextColor(acti.color);
                }
            }
        });


        dialog.show();

    }

    private void setting_on_off(int type, boolean on){

        if(on){
            if(type==Acti.ALARM){
                if(activity.alarmBefore==-1) activity.alarmBefore=0;
                back_alarm.setSelected(true);
                alarmB.setSelected(true);
                alarmSwitch.setChecked(true);
                setTimeView(type,alarmB,activity.alarmBefore);
            }
            else{
                if(activity.notiBefore==-1) activity.notiBefore=0;
                back_noti.setSelected(true);
                notiB.setSelected(true);
                notiSwitch.setChecked(true);
                setTimeView(type,notiB,activity.notiBefore);
            }
        }
        else{
            if(type==Acti.ALARM){
                back_alarm.setSelected(false);
                alarmB.setSelected(false);
                activity.alarmBefore=-1;
                alarmSwitch.setChecked(false);
                setTimeView(type,alarmB,activity.alarmBefore);
            }
            else{
                back_noti.setSelected(false);
                notiB.setSelected(false);
                activity.notiBefore=-1;
                notiSwitch.setChecked(false);
                setTimeView(type,notiB,activity.notiBefore);
            }
        }

    }

    private void setTimeView(int type,TextView textView,  int phuttong){
        String s="";
        if(type == Acti.ALARM)
            s+= getResources().getString(R.string.alarm);
        else if(type == Acti.NOTIFICATION)
            s+= getResources().getString(R.string.noti);

        if(phuttong==-1){
            s+=" "+getResources().getString(R.string.not_set);
        }else{
            int gio = phuttong/60;
            int phut =phuttong%60;
            s+= " "+getResources().getString(R.string.before)+" "+gio+" "+getResources().getString(R.string.Hour)
                    +" "+phut+" "+getResources().getString(R.string.Minute);
        }
        textView.setText(s);


    }

    int current_minute;
    private  void before_dialog(final int type){
        final int [] minutes = getResources().getIntArray(R.array.minute_add);
        final int MAX_minute = 600;
        current_minute =0;

        final Dialog  dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.before_dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.nothing);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        ConstraintLayout back = (ConstraintLayout) dialog.findViewById(R.id.background);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LinearLayout main = (LinearLayout) dialog.findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView title = (TextView) dialog.findViewById(R.id.title);
        final TextView time = (TextView) dialog.findViewById(R.id.time);
        Button reset = (Button) dialog.findViewById(R.id.reset);
        Button set = (Button) dialog.findViewById(R.id.set);
        Button unset = (Button) dialog.findViewById(R.id.unset);
        FlexboxLayout flexbox = (FlexboxLayout) dialog.findViewById(R.id.flexbox);

        final Animation ping = AnimationUtils.loadAnimation(this,R.anim.up_down_lite);
        final Animation ping2 = AnimationUtils.loadAnimation(this,R.anim.down_up_lite);

        for(int i=0;i<minutes.length;i++){
            final Button button = new Button(this);
            button.setText("+ "+minutes[i]);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_minute+=minutes[finalI];
                    if(current_minute>MAX_minute)
                        current_minute=MAX_minute;
                    setTimeTextView(time,current_minute);
                    button.startAnimation(ping);
                    time.startAnimation(ping2);
                }
            });

            flexbox.addView(button);
        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_minute=0;
                setTimeTextView(time,current_minute);
                time.startAnimation(ping2);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });


        if(type == Acti.ALARM){

            title.setText(getResources().getString(R.string.alarm));
            current_minute = activity.alarmBefore;
            if(current_minute==-1)
                current_minute=0;
            setTimeTextView(time,current_minute);
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,true);
                    activity.alarmBefore = current_minute;
                    setTimeView(type,alarmB,current_minute);
                    setting_on_off(type,true);
                    dialog.dismiss();
                }
            });
            unset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,false);
                    dialog.dismiss();
                }
            });



        }else if(type == Acti.NOTIFICATION){
            title.setText(getResources().getString(R.string.noti));
            current_minute =activity.notiBefore;
            if(current_minute==-1)
                current_minute=0;

            setTimeTextView(time,current_minute);
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,true);
                    activity.notiBefore = current_minute;
                    setTimeView(type,notiB,current_minute);
                    setting_on_off(type,true);
                    dialog.dismiss();
                }
            });
            unset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,false);
                    dialog.dismiss();

                }
            });
        }

        dialog.show();

    }
    private  void setTimeTextView(TextView textView,int minute){
        int h = minute/60;
        int m = minute%60;
        String s = h+" "+getResources().getString(R.string.Hour)+" "+m+ " "+getResources().getString(R.string.Minute);
        textView.setText(s);

    }





    private  void addOncePicker(final  LinearLayout listTimeLine){
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_timeline_once, null);
        final TextView time1 = (TextView) v.findViewById(R.id.time1) ;
        final TextView time2 = (TextView) v.findViewById(R.id.time2);
        final TextView date = (TextView) v.findViewById(R.id.spinner);
        final EditText edit_location = (EditText) v.findViewById(R.id.edit_location);

        edit_location.setText(intervalx.location);
        CheckBox checkBoxdelete = (CheckBox) v.findViewById(R.id.checkboxdelete);
        if(activity.autoDelete == Acti.AUTO_DELETE)
            checkBoxdelete.setChecked(true);
        else
            checkBoxdelete.setChecked(false);

        checkBoxdelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activity.autoDelete = Acti.AUTO_DELETE;
                else
                    activity.autoDelete = Acti.NONE_AUTO_DELETE;
            }
        });

        time1.setText(intervalx.startString());

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time1,"s");
            }
        });
        time2.setText(intervalx.endString());
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time2,"e");
            }
        });
        edit_location.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                intervalx.location = edit_location.getText().toString().trim();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        Calendar calendar = intervalx.getDay();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.dateFotmat));
        String ngaythangnam = simpleDateFormat.format(calendar.getTime());
        date.setText(ngaythangnam);
        intervalx.ngay = calendar.get(Calendar.DATE);
        intervalx.thang = calendar.get(Calendar.MONTH);
        intervalx.nam = calendar.get(Calendar.YEAR);

        int eventd = calendar.get(Calendar.DAY_OF_WEEK);
        switch (eventd) {
            case 2 : intervalx.eventDay =0; break;
            case 3 : intervalx.eventDay =1; break;
            case 4 : intervalx.eventDay =2; break;
            case 5 : intervalx.eventDay =3; break;
            case 6 : intervalx.eventDay =4; break;
            case 7 : intervalx.eventDay =5; break;
            case 1 : intervalx.eventDay =6; break;
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate(date);
            }
        });

        listTimeLine.addView(v);
    }

    private  void timePicker(final TextView textView, final String se){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);

        final String[] s1 = textView.getText().toString().split(":");
        calendar.set(0,0,0,Integer.parseInt(s1[0]),Integer.parseInt(s1[1]));

        // TimePickerDialog có 5 tham số: (1: this, 2 new OnTimeSetListener, 3 hour, 4 minute, 5: true:24h || false:12h)
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(0,0,0,hourOfDay,minute,0); // set time cho calendar
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String gio = simpleDateFormat.format(calendar.getTime());
                textView.setText(gio);

                if(se.equals("s")){
                    String[] s = textView.getText().toString().split(":");
                    Double a = Double.valueOf(Integer.parseInt(s[0]) + (Float.parseFloat(s[1])/60));
                    intervalx.startTime=a;
                }
                else if(se.equals("e")){
                    String[] s = textView.getText().toString().split(":");
                    Double a = Double.valueOf(Integer.parseInt(s[0]) + (Float.parseFloat(s[1])/60));
                    intervalx.endTime=a;
                }

            }
        },Integer.parseInt(s1[0]),Integer.parseInt(s1[1]),true);

        timePickerDialog.show();
    }

    private void pickdate(final TextView textView){    // Hàm tạo date picker dialog

        final Calendar calendar = intervalx.getDay();
        int year =  calendar.get(calendar.YEAR); // lấy 3 tham số ngày tháng năm để xác định ngày mặc định trong picker
        int month = calendar.get(calendar.MONTH);
        int date = calendar.get(calendar.DATE);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.dateFotmat));

        // DatePickerDialog có 5 tham số (1: this, 2: new OnDateSetListener, 345: năm tháng ngày vừa lấy)
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                String ngaythangnam = simpleDateFormat.format(calendar.getTime());
                textView.setText(ngaythangnam);
                intervalx.ngay = calendar.get(Calendar.DATE);
                intervalx.thang = calendar.get(Calendar.MONTH);
                intervalx.nam = calendar.get(Calendar.YEAR);

                int eventd = calendar.get(Calendar.DAY_OF_WEEK);
                switch (eventd) {
                    case 2 : intervalx.eventDay =0; break;
                    case 3 : intervalx.eventDay =1; break;
                    case 4 : intervalx.eventDay =2; break;
                    case 5 : intervalx.eventDay =3; break;
                    case 6 : intervalx.eventDay =4; break;
                    case 7 : intervalx.eventDay =5; break;
                    case 1 : intervalx.eventDay =6; break;
                }
            }
        },year,month,date );
        datePickerDialog.show(); // Phải có show
    };

    public void save(){
        if(name.getText().toString().trim().equals(""))
            Toast.makeText(Main2Activity.this, "Name is empty", Toast.LENGTH_SHORT).show();
        else {
            activity.name = name.getText().toString().trim();
            activity.describe = des.getText().toString().trim();
            if(noneRadio.isChecked())
                activity.mode= NORMAL_MODE;
            if(vibrateRadio.isChecked())
                activity.mode = VIBRATE_MODE;
            if(silentRadio.isChecked())
                activity.mode = SILENT_MODE;
            if (check != -1)
                activity.color = color_int[check];



            if(!alarmSwitch.isChecked())
                activity.alarmBefore=-1;
            if(!notiSwitch.isChecked())
                activity.notiBefore=-1;


            if(activity.weekly != Acti.ONCE){
                for (int k = 0; k < intervals.size(); k++) {
                    Interval interval =(Interval) intervals.get(k);
                    if (interval.id == activity.id)
                    {
                        intervals.remove(k);
                    }
                }
                ArrayList intervals2 = intervals;

                for(int i=0;i<=numberofTimeline[0];i++)
                {
                    if(listInterval[i]!=null){

                        Interval a = new Interval(listInterval[i]);
                        if(a.startTime>a.endTime)
                        {
                            double temp = a.endTime;
                            a.setEndTime(a.startTime);
                            a.setStartTime(temp);
                            listInterval[i]=a;
                        }
                        intervals2.add(listInterval[i]);
                    }
                }// end for

                intervals=intervals2;
            }
            else
                intervals.add(intervalx);


            acti.remove(position);
            acti.add(position,activity);
            Log.d("onDismiss", ": "+activity.alarmBefore+" "+activity.notiBefore);
            savedata();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else
            finish();

        }/// endElse
    }

    //////////// end on create /////////////////////////////////////////////////////////////////////////////


    int[] numberofTimeline ={-1};

    private void XacNhanXoa() { //tạo hàm xác nhận có không
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.warning));

        alertDialog.setMessage(getString(R.string.delete_activity));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { // Tạo nút Có và hành động xóa phần tử trong array
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acti.remove(position);
                for(int i=0;i<intervals.size();i++){
                    Interval interval = (Interval) intervals.get(i);
                    if(interval.id == activity.id){
                        intervals.remove(i);
                        i=0;
                    }
                }
                savedata();
                finish();

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
    private  void addTimeLine(final LinearLayout listTimeLine){

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_timeline, null);

        final LinearLayout cardView = (LinearLayout) v.findViewById(R.id.itemtimeline);
        final TextView time1 = (TextView) v.findViewById(R.id.time1);
        final TextView time2 = (TextView) v.findViewById(R.id.time2);
        final Spinner date = (Spinner) v.findViewById(R.id.spinner);
        final EditText edit_location = (EditText) v.findViewById(R.id.edit_location);




        ImageView remove = (ImageView) v.findViewById(R.id.remove);


        if(numberofTimeline[0]==MAXINTERVAL)
        {
            Toast.makeText(this, "Out of memory", Toast.LENGTH_SHORT).show();
            return;
        }


        numberofTimeline[0]++;

        final int current = numberofTimeline[0];
        if(current==0)
            listInterval[current] = new Interval(activity.id,idEvent,9.0,10.0,0);
        else if(listInterval[current-1]==null)
            listInterval[current] = new Interval(activity.id,idEvent,9.0,10.0,0);
        else listInterval[current] = new Interval(listInterval[current-1].id,idEvent,listInterval[current-1].startTime,listInterval[current-1].endTime,0);



        edit_location.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                listInterval[current].location = edit_location.getText().toString().trim();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });


        time1.setText(listInterval[current].startString());
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time1,current,"s");
            }
        });

        time2.setText(listInterval[current].endString());
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time2,current,"e");
            }
        });

        //        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ngay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style,ngay)
        {

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(13);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        date.setAdapter(adapter);


        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listInterval[current].eventDay=i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final Animation pop_up_out = AnimationUtils.loadAnimation(this,R.anim.pop_up_out);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardView.startAnimation(pop_up_out);
                CountDownTimer countDownTimer = new CountDownTimer(800,500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        listTimeLine.removeView(cardView);
                        listInterval[current]=null;
                    }
                }.start();

            }
        });

        Animation pop_up = AnimationUtils.loadAnimation(this,R.anim.pop_up);
        cardView.setAnimation(pop_up);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Transition t = new Fade();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                t = new Slide();
            }
            TransitionManager.beginDelayedTransition(listTimeLine,t);
        }


        listTimeLine.addView(cardView);


        idEvent++;

    }
    private  void addTimeLine(final LinearLayout listTimeLine,Interval interval){

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_timeline, null);

        final LinearLayout cardView = (LinearLayout) v.findViewById(R.id.itemtimeline);
        final TextView time1 = (TextView) v.findViewById(R.id.time1);
        final TextView time2 = (TextView) v.findViewById(R.id.time2);
        final Spinner date = (Spinner) v.findViewById(R.id.spinner);
        ImageView remove = (ImageView) v.findViewById(R.id.remove);
        final EditText edit_location = (EditText) v.findViewById(R.id.edit_location);
        numberofTimeline[0]++;
        final int current = numberofTimeline[0];

        listInterval[current] = interval.getCopy();

        edit_location.setText(listInterval[current].location);
        edit_location.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                listInterval[current].location = edit_location.getText().toString().trim();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });



        time1.setText(listInterval[current].startString());

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time1,current,"s");
            }
        });


        time2.setText(listInterval[current].endString());

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time2,current,"e");
            }
        });


        //        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ngay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style,ngay)
        {

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(13);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        date.setAdapter(adapter);
        date.setSelection(listInterval[current].eventDay);


        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listInterval[current].eventDay=i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Animation pop_up_out = AnimationUtils.loadAnimation(this,R.anim.pop_up_out);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.startAnimation(pop_up_out);
                CountDownTimer countDownTimer = new CountDownTimer(800,100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        listTimeLine.removeView(cardView);
                        listInterval[current]=null;
                    }
                }.start();

            }
        });

        listTimeLine.addView(cardView);

        idEvent++;


    }

    private  void timePicker(final TextView textView, final int current, final String se){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);

        final String[] s1 = textView.getText().toString().split(":");
        calendar.set(0,0,0,Integer.parseInt(s1[0]),Integer.parseInt(s1[1]));

        // TimePickerDialog có 5 tham số: (1: this, 2 new OnTimeSetListener, 3 hour, 4 minute, 5: true:24h || false:12h)
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(0,0,0,hourOfDay,minute,0); // set time cho calendar
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String gio = simpleDateFormat.format(calendar.getTime());
                textView.setText(gio);

                Log.d("asdfasdfwerwer", "onTimeSet: "+current);

                if(se.equals("s")){
                    String[] s = textView.getText().toString().split(":");
                    Double a = Double.valueOf(Integer.parseInt(s[0]) + (Float.parseFloat(s[1])/60));
                    listInterval[current].startTime=a;
                }
                else if(se.equals("e")){
                    String[] s = textView.getText().toString().split(":");
                    Double a = Double.valueOf(Integer.parseInt(s[0]) + (Float.parseFloat(s[1])/60));
                    listInterval[current].endTime=a;
                }



            }
        },Integer.parseInt(s1[0]),Integer.parseInt(s1[1]),true);

        timePickerDialog.show();
    }
    public void add_actionbar_title(String s){
        androidx.appcompat.app.ActionBar abar = getSupportActionBar();

        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_actionbar, null);
        androidx.appcompat.app.ActionBar.LayoutParams params =new androidx.appcompat.app.ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.LEFT);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(s);
        abar.setCustomView(viewActionBar,params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("result",1);
        setResult(0,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = getIntent();
                intent.putExtra("result",1);
                setResult(0,intent);
                finish();
                return true;
            case R.id.delete:
                XacNhanXoa();
                break;
            case R.id.save:
                save();
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    TimeTable timeTable;
    ArrayList acti;
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
