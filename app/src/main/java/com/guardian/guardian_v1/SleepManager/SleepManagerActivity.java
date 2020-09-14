package com.guardian.guardian_v1.SleepManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.bikcrum.circularrangeslider.CircularRangeSlider;
import com.guardian.guardian_v1.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SleepManagerActivity extends AppCompatActivity {

    TextView sleepTime;
    TextView wakeUpTime;
    TextView totalSleep;
    CircularRangeSlider clock;

    Date sleepTimeDate;
    Date wakeUpTimeDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_manager);
        initiateViews();
        initiateClock();
        startForegroundService(new Intent(this, SleepDetectorService.class));

    }

    public String getTotalSleepTime(Date wakeUpDate, Date sleepDate) {
        int minutes;
        if (wakeUpDate.getDay() == sleepDate.getDay()) {
            minutes = wakeUpDate.getHours() * 60 + wakeUpDate.getMinutes() - sleepDate.getHours() * 60 - sleepDate.getMinutes();
        } else {
            minutes = wakeUpDate.getHours() * 60 + wakeUpDate.getMinutes() + 24 * 60 - sleepDate.getHours() * 60 - sleepDate.getMinutes();
        }
        return minutes / 60 + "H:" + minutes % 60 + "M";
    }

    public ArrayList<Date> generateToDate(int timeStart, int timeEnd) {
        ArrayList<Date> dates = new ArrayList<>();
        if (timeStart < timeEnd) {
            Date date = Calendar.getInstance().getTime();
            int minuteStart = timeStart % 60;
            int hourStart = timeStart / 60;
            int minuteFinish = timeEnd % 60;
            int hourFinish = timeEnd / 60;
            dates.add(new Date(date.getYear(), date.getMonth(), date.getDate(), hourStart, minuteStart, 0));
            dates.add(new Date(date.getYear(), date.getMonth(), date.getDate(), hourFinish, minuteFinish, 0));
        } else {
            Date date = Calendar.getInstance().getTime();
            int minuteStart = timeStart % 60;
            int hourStart = (timeStart / 60) + 12;
            int minuteFinish = timeEnd % 60;
            int hourFinish = timeEnd / 60;
            dates.add(new Date(date.getYear(), date.getMonth(), date.getDate() - 1, hourStart, minuteStart, 0));
            dates.add(new Date(date.getYear(), date.getMonth(), date.getDate(), hourFinish, minuteFinish, 0));
        }
        return dates;
    }

    public void initiateViews() {
        wakeUpTime = findViewById(R.id.wakeUpTime);
        sleepTime = findViewById(R.id.sleepTime);
        totalSleep = findViewById(R.id.totalSleep);
        clock = findViewById(R.id.clock);
    }

    public void initiateClock() {
        clock.setMax(720);
        clock.setProgress(2);
        String[] labels = new String[720];
        Arrays.fill(labels, "");
        labels[0] = "12";
        labels[60] = "1";
        labels[120] = "2";
        labels[180] = "3";
        labels[240] = "4";
        labels[300] = "5";
        labels[360] = "6";
        labels[420] = "7";
        labels[480] = "8";
        labels[540] = "9";
        labels[600] = "10";
        labels[660] = "11";
        clock.setlabels(labels);
        clock.setOnRangeChangeListener(new CircularRangeSlider.OnRangeChangeListener() {
            @Override
            public void onRangePress(int i, int i1) {

            }

            @Override
            public void onRangeChange(int i, int i1) {
                ArrayList<Date> dates = generateToDate(clock.getStartIndex(), clock.getEndIndex());
                wakeUpTimeDate = dates.get(1);
                sleepTimeDate = dates.get(0);
                System.out.println(SleepDetectorService.getSleepData().toString());
                changeTexts();
            }

            @Override
            public void onRangeRelease(int i, int i1) {

            }
        });
    }


    public void submit(View view) {
        if ((sleepTimeDate == null) || (wakeUpTimeDate == null)) {
            Toast.makeText(this, "please enter a sleep time", Toast.LENGTH_LONG).show();
            return;
        }
        if (SleepDetectorService.isSleepValid(sleepTimeDate, wakeUpTimeDate) == false) {
            Toast.makeText(this, "please enter a valid sleep time", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("submit")
                .setMessage("Are you sure you want to submit your data")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //put your code here
                    }
                })
                .setNegativeButton("cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void changeTexts() {
        sleepTime.setText("sleep time=  " + sleepTimeDate.getHours() + "H:" + sleepTimeDate.getMinutes() + "M");
        wakeUpTime.setText("wake up time=  " + wakeUpTimeDate.getHours() + "H:" + wakeUpTimeDate.getMinutes() + "M");
        totalSleep.setText(getTotalSleepTime(wakeUpTimeDate, sleepTimeDate));
    }

    public void recordAuto(View view) {
        ArrayList<Date> dates = SleepDetectorService.getSleepTime();
        if (dates.get(0).equals(dates.get(1))) {
            Toast.makeText(this, "not enough data", Toast.LENGTH_SHORT).show();
            return;
        }
        setSleepTimeDate(dates.get(0));
        setWakeUpTimeDate(dates.get(1));
        changeTexts();
    }

    public void setSleepTimeDate(Date date) {
        int minutes = 0;
        if (date.getHours() >= 12)
            minutes = (date.getHours() - 12) * 60 + date.getMinutes();
        else
            minutes = date.getHours() * 60 + date.getMinutes();
        clock.setStartIndex(minutes);
        sleepTimeDate = date;
    }

    public void setWakeUpTimeDate(Date date) {
        int minutes = 0;
        if (date.getHours() >= 12)
            minutes = (date.getHours() - 12) * 60 + date.getMinutes();
        else
            minutes = date.getHours() * 60 + date.getMinutes();
        clock.setEndIndex(minutes);
        wakeUpTimeDate = date;
    }

}

