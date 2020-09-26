package com.guardian.guardian_v1.SleepSpeedManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bikcrum.circularrangeslider.CircularRangeSlider;
import com.google.gson.Gson;
import com.guardian.guardian_v1.R;
import com.guardian.guardian_v1.SeatBelt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
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
        if(isThereSleepDataFile(this)  == false){
            makeSleepDataFile(this);
        }else{
            System.out.println("hastesh");
        }
        initiateViews();
        initiateClock();
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
                changeTexts();
            }

            @Override
            public void onRangeRelease(int i, int i1) {

            }
        });
    }


    public void submit(View view) {
        if ((sleepTimeDate == null) || (wakeUpTimeDate == null)) {
            Toast.makeText(this, "لطفا میزان خواب را وارد کنید!", Toast.LENGTH_LONG).show();
            return;
        }
        if (SleepSpeedDetectorService.isSleepValid(sleepTimeDate, wakeUpTimeDate) == false) {
            Toast.makeText(this, "این میزان خواب برای شما معتبر نیست!", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.sleep_sureness_alert, null);

        Button yesButton = dialogView.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               writeInfoToFile(SleepManagerActivity.this,sleepTimeDate,wakeUpTimeDate);
               Intent intent = new Intent(SleepManagerActivity.this, SeatBelt.class);
               startActivity(intent);
               finish();
            }
        });
       builder.setNeutralButton("خروج", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(dialogView);
        builder.show();
    }

    public void changeTexts() {
        sleepTime.setText("ساعت خواب:  " + sleepTimeDate.getHours() + "H:" + sleepTimeDate.getMinutes() + "M");
        wakeUpTime.setText("ساعت بیدارشدن:  " + wakeUpTimeDate.getHours() + "H:" + wakeUpTimeDate.getMinutes() + "M");
        totalSleep.setText(getTotalSleepTime(wakeUpTimeDate, sleepTimeDate));
    }

    public void recordAuto(View view) {
        ArrayList<Date> dates = SleepSpeedDetectorService.getSleepTime();
        if (dates.get(0).equals(dates.get(1))) {
            Toast.makeText(this, "اطلاعات کافی موجود نیست!", Toast.LENGTH_SHORT).show();
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

    public static boolean isThereSleepDataFile(Context context){
        for (String s : context.fileList()) {
            if(s.equals("SleepData")) return true;
        }
        return false;
    }

    public static void makeSleepDataFile(Context context){
        File file = new File(context.getFilesDir(), "SleepData");
    }

    public static boolean writeInfoToFile(Context context, Date sleepTimeDate, Date wakeUpTimeDate){
        try {
            makeSleepDataFile(context);
            SleepData sleepData = new SleepData(sleepTimeDate,wakeUpTimeDate,Calendar.getInstance().getTime());
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("SleepData", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(sleepData));
            outputStreamWriter.close();
            System.out.println("done");
            isSleepDataRecordedToday(context);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static SleepData readDataFromFile(Context context){
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("SleepData");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (IOException e) {
            return null;
        }
        Gson gson = new Gson();
        return   gson.fromJson(ret,SleepData.class);
    }

    public static boolean isSleepDataRecordedToday(Context context){
        if(isThereSleepDataFile(context)  == false) makeSleepDataFile(context);
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("SleepData");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
          System.out.println("login activity File not found: " + e.toString());
          return false;
        } catch (IOException e) {
            System.out. println("login activity Can not read file: " + e.toString());
            return false;
        }
        System.out.println(ret);
        Gson gson = new Gson();
        SleepData sleepData =  gson.fromJson(ret,SleepData.class);
        Date now = Calendar.getInstance().getTime();
        System.out.println(sleepData.getCurrentTime().getHours());
        System.out.println(now.getHours());
        if(sleepData.getCurrentTime().getDate()-now.getDate()>1) return false;
        return !((sleepData.getCurrentTime().getDate()!=now.getDate()) && (now.getHours()>10));
    }

    public static void deleteSleepData(Context context){
       makeSleepDataFile(context);
    }

}