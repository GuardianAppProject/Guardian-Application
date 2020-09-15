package com.guardian.guardian_v1;

import java.util.ArrayList;
import java.lang.reflect.Type;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.gson.Gson;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.content.res.Resources;


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



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mActivityRecognitionClient== null){
            mActivityRecognitionClient = new ActivityRecognitionClient(this);
            mActivityRecognitionClient.requestActivityUpdates(1000,PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        }
        speedCheckerService = this;

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


    static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.ON_BICYCLE:
                return "bicycle";
            case DetectedActivity.ON_FOOT:
                return "foot";
            case DetectedActivity.RUNNING:
                return "running";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.WALKING:
                return "walking";
            case DetectedActivity.IN_VEHICLE:
                return "vehicle";
            default:
                return "unknown";
        }
    }
    static final int[] POSSIBLE_ACTIVITIES = {

            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };

    static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        if ((detectedActivitiesList.size()>=1)&&(detectedActivitiesList.get(0).getType() == DetectedActivity.IN_VEHICLE) && (detectedActivitiesList.get(0).getConfidence()) >= 60){
            speedCheckerService.makeNotification();
            try {
                Thread.sleep(1000*60*20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new Gson().toJson(detectedActivitiesList, type);
    }

    static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
        Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        if (detectedActivities == null) {
            detectedActivities = new ArrayList<>();
        }
        return detectedActivities;
    }

    public void makeNotification(){
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.blue)
                .setContentTitle("Driving detected")
                .setContentText("would you like to start Guardian?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

}

//to start service startForeground(intent)