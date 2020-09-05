package com.guardian.guardian_v1;

import java.util.ArrayList;
import java.lang.reflect.Type;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.gson.Gson;
import android.content.Intent;

import android.os.IBinder;
import android.content.res.Resources;


import androidx.annotation.Nullable;


import com.google.gson.reflect.TypeToken;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class SpeedCheckerService extends Service {
    protected static final String TAG = "Activity";
    private static SpeedCheckerService speedCheckerService;
    private ActivityRecognitionClient mActivityRecognitionClient;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        makeNotification();
        if (mActivityRecognitionClient == null) {
            mActivityRecognitionClient = new ActivityRecognitionClient(this);
            mActivityRecognitionClient.requestActivityUpdates(1000, PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
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
        switch (detectedActivityType) {
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
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {
        }.getType();
        System.out.println(detectedActivitiesList.toString());
        if ((detectedActivitiesList.size() >= 1) && (detectedActivitiesList.get(0).getType() == DetectedActivity.STILL) && (detectedActivitiesList.get(0).getConfidence()) >= 60) {
            System.out.println("re dge haha ijfejie");
            speedCheckerService.makeNotification();
        }
        return new Gson().toJson(detectedActivitiesList, type);
    }

    static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
        Type listType = new TypeToken<ArrayList<DetectedActivity>>() {
        }.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        if (detectedActivities == null) {
            detectedActivities = new ArrayList<>();
        }
        return detectedActivities;
    }

    public void makeNotification() {

    }
}
