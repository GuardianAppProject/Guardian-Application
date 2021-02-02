package com.guardian.guardian_v1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.guardian.guardian_v1.SleepSpeedManager.SleepSpeedDetectorService;
import com.guardian.guardian_v1.Transmission.AverageWorker;
import com.guardian.guardian_v1.Transmission.TokenChecker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private static int TIME_OUT = 2500; //Time to launch the another activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, SleepSpeedDetectorService.class));
        }else{
            startService(new Intent(this, SleepSpeedDetectorService.class));
        }

    }

    private void startApp() {
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


        String str = readFile2().toString();
        if(str.length()>=1){
            Main.dangerModeOn = Boolean.parseBoolean(str);
        } else {
            Main.dangerModeOn = true;
        }

//        Toast.makeText(this, readFile(), Toast.LENGTH_SHORT).show();
        if(isNumeric(readFile().toString())) {
            int sound_notif = Integer.parseInt(readFile().toString());
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

            }

        }.start();

    }

    public StringBuilder readFile() {
        StringBuilder stringBuffer = new StringBuilder("");
        try {
            FileInputStream fileInputStream = openFileInput("settings.txt");
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

    public StringBuilder readFile2() {
        StringBuilder stringBuffer = new StringBuilder("");
        try {
            FileInputStream fileInputStream = openFileInput("dangermode.txt");
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

}
