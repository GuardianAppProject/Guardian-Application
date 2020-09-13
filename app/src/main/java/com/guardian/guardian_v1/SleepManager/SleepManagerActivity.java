package com.guardian.guardian_v1.SleepManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;


import android.view.View;

import android.widget.TextView;

import com.bikcrum.circularrangeslider.CircularRangeSlider;
import com.guardian.guardian_v1.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SleepManagerActivity extends AppCompatActivity {

    TextView sleepTime;
    TextView wakeUpTime;
    TextView totalSleep;
    CircularRangeSlider clock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_manager);
        initiateViews();
        initiateClock();
    }

    public String getTotalSleepTime(Date wakeUpDate,Date sleepDate){
        int minutes;
        if(wakeUpDate.getDay()==sleepDate.getDay()){
            minutes = wakeUpDate.getHours()*60 + wakeUpDate.getMinutes() - sleepDate.getHours()*60 -sleepDate.getMinutes();
        }else{
            minutes = wakeUpDate.getHours()*60 + wakeUpDate.getMinutes()+ 24*60- sleepDate.getHours()*60 -sleepDate.getMinutes();
        }
        return minutes/60+"H:"+minutes%60+"M";
    }

    public ArrayList<Date> generateToDate(int timeStart, int timeEnd){
        ArrayList<Date> dates=new ArrayList<>();
        if(timeStart<timeEnd){
            System.out.println("111111");
            Date date = Calendar.getInstance().getTime();
            int minuteStart = timeStart % 60;
            int hourStart = timeStart / 60;
            int minuteFinish = timeEnd % 60;
            int hourFinish = timeEnd / 60;
            dates.add(new Date(date.getYear(),date.getMonth(),date.getDay(),hourStart,minuteStart));
            dates.add(new Date(date.getYear(),date.getMonth(),date.getDay(),hourFinish,minuteFinish));
            return dates;
        }else{
            System.out.println("22222222");
            Date date = Calendar.getInstance().getTime();
            int minuteStart = timeStart % 60;
            int hourStart = (timeStart/60) + 12;
            int minuteFinish = timeEnd % 60;
            int hourFinish = timeEnd / 60;
            dates.add(new Date(date.getYear(),date.getMonth(),date.getDay()-1,hourStart,minuteStart));
            dates.add(new Date(date.getYear(),date.getMonth(),date.getDay(),hourFinish,minuteFinish));
            return dates;
        }
    }

    public void initiateViews(){
        wakeUpTime = findViewById(R.id.wakeUpTime);
        sleepTime = findViewById(R.id.sleepTime);
        totalSleep = findViewById(R.id.totalSleep);
        clock = findViewById(R.id.clock);
    }

    public void initiateClock(){
        clock.setMax(720);
        clock.setProgress(2);
        clock.setLabelVisibility(1);
        clock.getBackground().setAlpha(45);
        clock.setOnRangeChangeListener(new CircularRangeSlider.OnRangeChangeListener() {
            @Override
            public void onRangePress(int i, int i1) {

            }

            @Override
            public void onRangeChange(int i, int i1) {
                ArrayList<Date> dates=generateToDate(clock.getStartIndex(),clock.getEndIndex());
                Date sleepTimeDate = dates.get(0);
                Date wakeUpTimeDate = dates.get(1);
                sleepTime.setText("sleep time=  "+sleepTimeDate.getHours()+"H:"+sleepTimeDate.getMinutes()+"M");
                wakeUpTime.setText("wake up time=  "+wakeUpTimeDate.getHours()+"H:"+wakeUpTimeDate.getMinutes()+"M");
                totalSleep.setText(getTotalSleepTime(wakeUpTimeDate,sleepTimeDate));
            }

            @Override
            public void onRangeRelease(int i, int i1) {

            }
        });
    }


    public void submit(View view) {
        //put your code here
    }

    public void recordAuto(View view) {
    }

}

