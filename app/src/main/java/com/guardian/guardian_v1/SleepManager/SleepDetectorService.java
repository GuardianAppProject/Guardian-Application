package com.guardian.guardian_v1.SleepManager;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.PendingIntent;
import android.app.Service;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.gson.Gson;
import android.content.Intent;


import android.os.IBinder;

import android.util.Log;


import androidx.annotation.Nullable;



import com.google.gson.reflect.TypeToken;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class SleepDetectorService extends Service {
    protected static final String TAG = "Activity";

    private static HashMap<Date,DetectedActivity> sleepData= new HashMap<>();
    private static ArrayList<Date> allDates = new ArrayList<>();



    private  ActivityRecognitionClient mActivityRecognitionClient;
    private boolean started = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println(started);
         if((mActivityRecognitionClient== null)&&(!started)){
            mActivityRecognitionClient = new ActivityRecognitionClient(this);
            mActivityRecognitionClient.requestActivityUpdates(1000 * 60 * 5,PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
            started = true;
         }

        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
            detectedActivitiesToJson(detectedActivities);
        }
        return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("bind");
        return null;
    }


    static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        if(detectedActivitiesList != null){
            deleteOldData();
            Date date = getDate(Calendar.getInstance().getTime());
            sleepData.put(date,detectedActivitiesList.get(0));
            allDates.add(date);
            System.out.println(sleepData);
        }
        return new Gson().toJson(detectedActivitiesList, type);
    }

    static void clear(){
        sleepData.clear();
        allDates.clear();
    }

    private static void deleteOldData(){
        if(allDates.isEmpty()) return;
        while((allDates.get(0).getDay()!=allDates.get(allDates.size()-1).getDay()) && (allDates.get(allDates.size()-1).getHours()+24-allDates.get(0).getHours()>24)){
            sleepData.remove(allDates.get(0));
            allDates.remove(0);
        }
    }

    public static boolean isSleepValid(Date sleepTime, Date awakeTime){
        Date date = getDate(sleepTime);
        int error = 0;
        while(date.after(awakeTime)  == false){
            if((sleepData.containsKey(date))&&(sleepData.get(date).getType()!=DetectedActivity.STILL)){
                error++;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE,5);
            date = calendar.getTime();
            calendar.clear();
        }
        return error <= 3;
    }

    public static ArrayList<Date> getSleepTime(){
        deleteOldData();
        ArrayList<Boolean> l = new ArrayList<>();
        for (Date date : allDates) {
            l.add(((sleepData.containsKey(date))&&(sleepData.get(date).getType()==DetectedActivity.STILL)));
        }
        int index = 0;
        int lenght = 0;
        int maxLenght = 0;
        for(int i=0; i<l.size() ; i++){
            if(l.get(i)){
                lenght ++;
                index = i;
            }else{
                if(lenght > maxLenght){
                    maxLenght = lenght;
                    index = i;
                }
                lenght = 0;
            }
        }
        ArrayList<Date> dates=new ArrayList<>();
        dates.add(allDates.get(index-lenght+1));
        dates.add(allDates.get(index));
        return dates;
    }

    private static Date getDate(Date date){
        return new Date(date.getYear(),date.getMonth(),date.getDate(),date.getHours(),(date.getMinutes()/5)*5,0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        System.out.println("task removed");
    }

    public static HashMap<Date, DetectedActivity> getSleepData() {
        return sleepData;
    }
}

