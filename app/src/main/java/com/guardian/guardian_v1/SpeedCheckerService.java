package com.guardian.guardian_v1;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;


import com.google.android.gms.location.ActivityRecognitionClient;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.gson.reflect.TypeToken;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class SpeedCheckerService extends Service {
    private final String CHANNEL_ID = "guardian";

    protected static final String TAG = "Activity";
    private static SpeedCheckerService speedCheckerService;
    private  ActivityRecognitionClient mActivityRecognitionClient;
    private boolean started = false;
    Date lastNotification;


    @Override
    public void onCreate() {
        createNotificationChannel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void makeNotification(){
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("رانندگی شناسایی شد")
                .setContentText("ایا مایل به باز کردن گاردین هستید؟")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_white)
                .setColor(Color.parseColor("#00ff00"))
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(70, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "guardian";
            String description = "alerting user";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if((mActivityRecognitionClient== null)&&(!started)){
            mActivityRecognitionClient = new ActivityRecognitionClient(this);
            mActivityRecognitionClient.requestActivityUpdates(1000 * 60 * 2,PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
            started = true;
        }

        speedCheckerService = this;

        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
            detectedActivitiesToJson(detectedActivities);
        }
        return START_STICKY;
    }

    String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        System.out.println(detectedActivitiesList.toString());
        if ((detectedActivitiesList.size()>=1)&&(detectedActivitiesList.get(0).getType() == DetectedActivity.IN_VEHICLE) && (detectedActivitiesList.get(0).getConfidence()) >= 60){
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(2);
            for (ActivityManager.RunningTaskInfo aTask : alltasks) {
                System.out.println(aTask.baseActivity.getClassName());
                if(aTask.baseActivity.getClassName().equals("com.guardian.guardian_v1.MainActivity"))
                return null;
            }
            if(lastNotification!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastNotification);
                calendar.add(Calendar.MINUTE,1);
                Date newDate = calendar.getTime();
                calendar.clear();
                if(newDate.after(lastNotification) == false)
                return null;
            }
            speedCheckerService.makeNotification();
            lastNotification = Calendar.getInstance().getTime();
        }
        return new Gson().toJson(detectedActivitiesList, type);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        System.out.println("remove");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destroy");
    }
}

//to start service startForeground(intent)