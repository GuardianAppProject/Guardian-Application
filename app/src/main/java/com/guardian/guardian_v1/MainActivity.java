package com.guardian.guardian_v1;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


//import com.google.android.gms.maps.model.CameraPosition;
import com.guardian.guardian_v1.SleepSpeedManager.SleepManagerActivity;
import com.guardian.guardian_v1.SleepSpeedManager.SleepSpeedDetectorService;
import com.guardian.guardian_v1.SleepSpeedManager.UseMeNotification;
import com.guardian.guardian_v1.Transmission.AverageWorker;
import com.guardian.guardian_v1.Transmission.SingleUserDetailed;
import com.guardian.guardian_v1.Transmission.TokenChecker;
import com.onesignal.OneSignal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static String appVersion="0";

    public static String token;
    private static int TIME_OUT = 2500; //Time to launch the another activity
    private static final String ONESIGNAL_APP_ID = "52708f3f-d26f-4739-9b0e-97093714a222";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, SleepSpeedDetectorService.class));
//        } else {
//            startService(new Intent(this, SleepSpeedDetectorService.class));
//        }
        // version of apk --> Arman
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            appVersion = version;
            Log.d("version", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.appThemeColor));
        }

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        Button retryButton = findViewById(R.id.retryButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!GPSAndInternetChecker.check(MainActivity.this, height, width)) {
                    retryButton.setVisibility(View.VISIBLE);
                } else {
                    retryButton.setVisibility(View.INVISIBLE);
//                    startApp();
                }
            }
        });

//        if(!GPSAndInternetChecker.check(MainActivity.this, height, width)) {
//            retryButton.setVisibility(View.VISIBLE);
//        }
//
//        if(retryButton.getVisibility()==View.INVISIBLE) {
////           startApp();
//        }

        Date date = Calendar.getInstance().getTime();
        UseMeNotification.writeInfoToFile(this,date);
        Intent myIntent = new Intent(this ,UseMeNotification.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24*30 , pendingIntent);
    }

    private void startApp() {
        token = read();
        TokenChecker.beginCheck(read(),this);
        AverageWorker.beginCheck(read(),this);
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
//        StatusCalculator statusCalculator = new StatusCalculator();
//        try {
//            statusCalculator.weatherCalculator(42, 324, Weather.WeatherType.Ash);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String string = read();
        if(string.length()>=1){

        } else {
            Toast.makeText(this, "not", Toast.LENGTH_SHORT).show();
        }


        String str = readFile("dangermode.txt").toString();
        if(str.length()>=1){
            Main.dangerModeOn = Boolean.parseBoolean(str);
        } else {
            Main.dangerModeOn = true;
        }

//        Toast.makeText(this, readFile(), Toast.LENGTH_SHORT).show();
        if(isNumeric(readFile("settings.txt").toString())) {
            int sound_notif = Integer.parseInt(readFile("settings.txt").toString());
            Main.set_sound_repetition(sound_notif);
            Log.d("SOUND", "sound" +Main.get_sound_repetition());
        } else {
            Main.set_sound_repetition(20);
        }


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MainActivity.this, SignUp.class);
//                startActivity(i);
//                finish();
//            }
//        }, TIME_OUT);






        new CountDownTimer(3000, 3000) {

            @Override
            public void onTick(long millisUntilFinished) {
//                timer.setText(millisUntilFinished / 1000 + "s");
                //here you can have your logic to set text to edittext
                ImageView animateTextView = (ImageView) findViewById(R.id.Logo);


                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(animateTextView, "translationY",0f,-(height/3.72f)); //700 -- 3.2
                textViewAnimator.setDuration(3000);
                textViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                textViewAnimator.start();

//                Intent intent2 = new Intent();
//                startActivity(intent2);
//                overridePendingTransition(animation_in_goes_here,animation_out_goes_here);

            }


            public void onFinish() {
//                mTextField.setText("done!");

//                Intent i = new Intent(MainActivity.this, SignUp.class);
//                startActivity(i);
//                finish();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, TIME_OUT);

                    startActivity(new Intent(MainActivity.this, SignUp.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
            }

        }.start();

    }

    public StringBuilder readFile(String fileName) {
        StringBuilder stringBuffer = new StringBuilder("");
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuilder();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
//            Main.routeStyle = lines;
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return stringBuffer;
    }

    public String read(){
        //reading text from file
        String string = "";
        try {
            FileInputStream fileIn=openFileInput("tokenFile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[10000];

            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                string +=readstring;
            }
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resume");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Button retryButton = findViewById(R.id.retryButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!GPSAndInternetChecker.check(MainActivity.this, height, width)) {
                    retryButton.setVisibility(View.VISIBLE);
                } else {

                    retryButton.setVisibility(View.INVISIBLE);
                    startApp();
                }
            }
        });

        if(!GPSAndInternetChecker.check(MainActivity.this, height, width)) {
            retryButton.setVisibility(View.VISIBLE);
        }

        if(retryButton.getVisibility()==View.INVISIBLE) {
            startApp();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, SleepSpeedDetectorService.class));
//        }else{
//            startService(new Intent(this, SleepSpeedDetectorService.class));
//        }

        Date date = Calendar.getInstance().getTime();
        UseMeNotification.writeInfoToFile(this,date);
        Intent myIntent = new Intent(this ,UseMeNotification.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24*30 , pendingIntent);
    }

    private static boolean showGuide = false;

    public static void setShowGuide(boolean sg) {
        showGuide = sg;
    }

    public static boolean getShowGuide() {
        return showGuide;
    }
}
