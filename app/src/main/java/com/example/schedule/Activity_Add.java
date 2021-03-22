package com.example.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
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
import android.view.animation.Transformation;
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

public class Activity_Add extends AppCompatActivity {
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

    CheckBox alarmSwitch ;
    CheckBox notiSwitch;

    int id;
    int idActi;

    Acti oneActi;
    LinearLayout listTimeLine;
    LinearLayout addlinear ;

    TextView title;

    EditText editName ;
    EditText editDes;
    TextView colorTV;


    RadioButton noneRadio ;
    RadioButton vibrateRadio ;
    RadioButton silentRadio ;

    LinearLayout linearsound ;
    ImageView showimage ;
    ImageView addtimeline ;

    RadioButton weekly;
    RadioButton once ;

    Button importButton;


    public static int MAXINTERVAL = 100;
    int numberofTimeline = -1;
    Interval[] listInterval;
    ScrollView scrollViewdialog;
    boolean timeline1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getngay();
        getdata();
        add_actionbar_title(getResources().getString(R.string.new_event));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);


        colorTV = (TextView) findViewById(R.id.colorTV);
        id = acti.size();
        idActi=id;

        oneActi = new Acti();
        oneActi.id = idActi;
        notiB = (TextView) findViewById(R.id.notiBefore);
        alarmB = (TextView) findViewById(R.id.alarmBefore);
        alarmSwitch = (CheckBox) findViewById(R.id.alarm);
        notiSwitch = (CheckBox) findViewById(R.id.noti);

        back_alarm = (LinearLayout) findViewById(R.id.back_alarm);
        back_noti = (LinearLayout) findViewById(R.id.back_noti);
        weekly = (RadioButton) findViewById(R.id.weekly);
        once = (RadioButton) findViewById(R.id.once);


        listTimeLine = (LinearLayout) findViewById(R.id.listtimeline);


        colorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color_dialog(colorTV,oneActi);
            }
        });
        setting_on_off(Acti.ALARM,false);
        setting_on_off(Acti.NOTIFICATION,false);

        importButton = (Button) findViewById(R.id.importbutton);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Add.this, Activity_Import_TimeTable.class);
                startActivityForResult(intent,0);
            }
        });





        weekly.setChecked(true);

        weekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    listTimeLine.removeAllViews();
                    addtimeline.setVisibility(View.VISIBLE);
                    addTimeLine(listTimeLine);
                }

            }
        });
        once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    listTimeLine.removeAllViews();
                    listInterval = new Interval[100];
                    numberofTimeline=-1;
                    addtimeline.setVisibility(View.GONE);

                    interval_ADD = new Interval();
                    interval_ADD.id = idActi;
                    interval_ADD.idEvent=intervals.size();
                    addOncePicker(listTimeLine,oneActi,interval_ADD);
                }
            }
        });

        showimage = (ImageView) findViewById(R.id.showsound);
        linearsound = (LinearLayout) findViewById(R.id.linearsound);

        ConstraintLayout soundconstrain =(ConstraintLayout) findViewById(R.id.soundconstrain);
        soundconstrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearsound.getVisibility() == View.GONE){
                    expand(linearsound);
                    showimage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
                else{
                    collapse(linearsound);
                    showimage.setImageResource(R.drawable. ic_baseline_keyboard_arrow_down_24);
                }
            }
        });
        scrollViewdialog = (ScrollView) findViewById(R.id.scrollView3);

        timeline1=true;



        noneRadio = (RadioButton) findViewById(R.id.normalRadio);
        silentRadio = (RadioButton) findViewById(R.id.silentRadio);
        vibrateRadio = (RadioButton) findViewById(R.id.vibrateRadio);
        noneRadio.setChecked(true);



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



        addtimeline = (ImageView) findViewById(R.id.addTimeline);
        listInterval = new Interval[100];
        numberofTimeline=-1;
        addTimeLine(listTimeLine);
        addtimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeLine(listTimeLine);
            }
        });



        editName = (EditText) findViewById(R.id.editTextName);
        editDes = (EditText) findViewById(R.id.editTextDes);



    }

    private void add_click(){
        if(editName.getText().toString().trim().equals("")){
            Toast.makeText(Activity_Add.this, getResources().getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
        }
        else if(check==-1)
            Toast.makeText(Activity_Add.this, getResources().getString(R.string.empty_color), Toast.LENGTH_SHORT).show();

        else if(!editName.getText().toString().trim().equals("") && check!=-1){
            oneActi.name = editName.getText().toString().trim();
            oneActi.describe = editDes.getText().toString().trim();

            if(weekly.isChecked())
                oneActi.weekly = Acti.WEEKLY;
            else
                oneActi.weekly = Acti.ONCE;

            if(!alarmSwitch.isChecked())
                oneActi.alarmBefore=Acti.DONTHAVE;
            if(!notiSwitch.isChecked())
                oneActi.notiBefore=Acti.DONTHAVE;

            if(noneRadio.isChecked())
                oneActi.mode = Interval.NORMAL;
            else if(vibrateRadio.isChecked())
                oneActi.mode= Interval.VIBRATE;
            else if(silentRadio.isChecked())
                oneActi.mode= Interval.SILENT;


            acti.add(new Acti(oneActi));

            if(weekly.isChecked()){
                ArrayList intervals2 = intervals;

                for(int i=0;i<=numberofTimeline;i++)
                {
                    if(listInterval[i]!=null){
                        Interval a = listInterval[i];
                        if(a.startTime>a.endTime)
                        {
                            double temp = a.endTime;
                            a.setEndTime(a.startTime);
                            a.setStartTime(temp);
                            listInterval[i]=a;
                        }

                        intervals2.add(listInterval[i]);
                    }

                }
                intervals=intervals2;
            }else{
                if(interval_ADD.startTime>interval_ADD.endTime)
                {
                    double temp = interval_ADD.endTime;
                    interval_ADD.setEndTime(interval_ADD.startTime);
                    interval_ADD.setStartTime(temp);
                }
                intervals.add(interval_ADD);
            }

            idActi++;

            savedata();
            finish();
        }



    }
    int idEvent;
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


        if(type == Acti.ALARM){

            title.setText(getResources().getString(R.string.alarm));
            current_minute = oneActi.alarmBefore;
            if(current_minute==-1)
                current_minute=0;
            setTimeTextView(time,current_minute);



            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,true);
                    oneActi.alarmBefore = current_minute;
                    setTimeView(type,alarmB,oneActi.alarmBefore);
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
            current_minute =oneActi.notiBefore;
            if(current_minute==-1)
                current_minute=0;
            setTimeTextView(time,current_minute);


            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_on_off(type,true);
                    oneActi.notiBefore = current_minute;
                    setTimeView(type,notiB,current_minute);
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


    private void setting_on_off(int type, boolean on){

        if(on){
            if(type==Acti.ALARM){
                if(oneActi.alarmBefore==-1) oneActi.alarmBefore=0;
                back_alarm.setSelected(true);
                alarmB.setSelected(true);
                alarmSwitch.setChecked(true);
                setTimeView(type,alarmB,oneActi.alarmBefore);
            }
            else{
                if(oneActi.notiBefore==-1) oneActi.notiBefore=0;
                back_noti.setSelected(true);
                notiB.setSelected(true);
                notiSwitch.setChecked(true);
                setTimeView(type,notiB, oneActi.notiBefore);
            }
        }
        else{
            if(type==Acti.ALARM){
                back_alarm.setSelected(false);
                alarmB.setSelected(false);
                oneActi.alarmBefore=-1;
                alarmSwitch.setChecked(false);
                setTimeView(type,alarmB,oneActi.alarmBefore);
            }
            else{
                back_noti.setSelected(false);
                notiB.setSelected(false);
                oneActi.notiBefore=-1;
                notiSwitch.setChecked(false);
                setTimeView(type,notiB, oneActi.notiBefore);
            }
        }

    }


    private  void addOncePicker(final  LinearLayout listTimeLine, final Acti acti, final Interval interval){
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_timeline_once, null);
        final TextView time1 = (TextView) v.findViewById(R.id.time1) ;
        final TextView time2 = (TextView) v.findViewById(R.id.time2);
        final TextView date = (TextView) v.findViewById(R.id.spinner);
        final EditText edit_location = (EditText) v.findViewById(R.id.edit_location);
        CheckBox checkBoxdelete = (CheckBox) v.findViewById(R.id.checkboxdelete);
        acti.autoDelete = Acti.NONE_AUTO_DELETE;
        checkBoxdelete.setChecked(false);
        checkBoxdelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    acti.autoDelete = Acti.AUTO_DELETE;
                else
                    acti.autoDelete = Acti.NONE_AUTO_DELETE;
            }
        });


        time1.setText(interval.startString());

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time1,interval,"s");
            }
        });
        time2.setText(interval.endString());
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker(time2,interval,"e");
            }
        });
        edit_location.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                interval.location = edit_location.getText().toString().trim();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.dateFotmat));
        String ngaythangnam = simpleDateFormat.format(calendar.getTime());
        date.setText(ngaythangnam);
        interval.ngay = calendar.get(Calendar.DATE);
        interval.thang = calendar.get(Calendar.MONTH);
        interval.nam = calendar.get(Calendar.YEAR);

        int eventd = calendar.get(Calendar.DAY_OF_WEEK);
        switch (eventd) {
            case 2 : interval.eventDay =0; break;
            case 3 : interval.eventDay =1; break;
            case 4 : interval.eventDay =2; break;
            case 5 : interval.eventDay =3; break;
            case 6 : interval.eventDay =4; break;
            case 7 : interval.eventDay =5; break;
            case 1 : interval.eventDay =6; break;
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate(date,interval);
            }
        });

        Animation pop_up = AnimationUtils.loadAnimation(this,R.anim.pop_up);
        v.setAnimation(pop_up);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Transition t = new Fade();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                t = new Slide();
            }
            TransitionManager.beginDelayedTransition(listTimeLine,t);
        }

        listTimeLine.addView(v);
    }

    private  void timePicker(final TextView textView, final Interval interval, final String se){
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
                    interval.startTime=a;
                }
                else if(se.equals("e")){
                    String[] s = textView.getText().toString().split(":");
                    Double a = Double.valueOf(Integer.parseInt(s[0]) + (Float.parseFloat(s[1])/60));
                    interval.endTime=a;
                }

            }
        },Integer.parseInt(s1[0]),Integer.parseInt(s1[1]),true);

        timePickerDialog.show();
    }

    private void pickdate(final TextView textView, final Interval interval){    // Hàm tạo date picker dialog

        final Calendar calendar = Calendar.getInstance();
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
                interval.ngay = calendar.get(Calendar.DATE);
                interval.thang = calendar.get(Calendar.MONTH);
                interval.nam = calendar.get(Calendar.YEAR);

                int eventd = calendar.get(Calendar.DAY_OF_WEEK);
                switch (eventd) {
                    case 2 : interval.eventDay =0; break;
                    case 3 : interval.eventDay =1; break;
                    case 4 : interval.eventDay =2; break;
                    case 5 : interval.eventDay =3; break;
                    case 6 : interval.eventDay =4; break;
                    case 7 : interval.eventDay =5; break;
                    case 1 : interval.eventDay =6; break;
                }
            }
        },year,month,date );
        datePickerDialog.show(); // Phải có show
    };

    private  void addTimeLine(final LinearLayout listTimeLine){

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_timeline, null);

        int id = idActi;
        if(numberofTimeline==MAXINTERVAL-2)
        {
            Toast.makeText(this, "Out of memory", Toast.LENGTH_SHORT).show();
            return;
        }
        final LinearLayout cardView = (LinearLayout) v.findViewById(R.id.itemtimeline);
        final TextView time1 = (TextView) v.findViewById(R.id.time1) ;
        final TextView time2 = (TextView) v.findViewById(R.id.time2);
        final Spinner date = (Spinner) v.findViewById(R.id.spinner);
        final EditText edit_location = (EditText) v.findViewById(R.id.edit_location);




        ImageView remove = (ImageView) v.findViewById(R.id.remove);



        numberofTimeline++;

        final int current = numberofTimeline;
        if(current==0)
            listInterval[current] = new Interval(id,idEvent,9.0,10.0,0);
        else if(listInterval[current-1]==null)
            listInterval[current] = new Interval(id,idEvent,9.0,10.0,0);
        else {
            listInterval[current] = new Interval(listInterval[current-1].id,idEvent,listInterval[current-1].startTime,listInterval[current-1].endTime,0);
            if(listInterval[current-1].eventDay!=6)
                listInterval[current].eventDay=listInterval[current-1].eventDay+1;

        }

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style,ngay)
        {

            public View getView(int position, View convertView, ViewGroup parent) {

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                t = new Slide();
            }
            TransitionManager.beginDelayedTransition(listTimeLine,t);
        }


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
    int check;
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
                gdtv.setStroke(3, Color.BLACK);


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

                                oval[finalItem].setImageResource(R.drawable.ic_color_lens_black_24dp);
                                check=finalItem;
                            }
                            else{

                                oval[check].setImageResource(R.drawable.nothing);
                                oval[finalItem].setImageResource(R.drawable.ic_color_lens_black_24dp);
                                check=finalItem;
                            }
                        }
                    }
                });
                if(acti.color!=-1 && acti.color == color_int[item]){
                    oval[item].setImageResource(R.drawable.ic_color_lens_black_24dp);
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
    Interval interval_ADD ;
    TextView notiB ;
    TextView alarmB;
    LinearLayout back_alarm, back_noti;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        finish();
        super.onActivityResult(requestCode, resultCode, data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
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
            case R.id.add:
                add_click();
                break;

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