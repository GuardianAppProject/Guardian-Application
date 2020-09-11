package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.guardian.guardian_v1.DriveStatus.Weather;
import com.guardian.guardian_v1.Transmission.TokenChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static int TIME_OUT = 2500; //Time to launch the another activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TokenChecker.beginCheck(read(),this);
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

//        Toast.makeText(this, readFile(), Toast.LENGTH_SHORT).show();
        String mapStyle = readFile().toString();
        if(mapStyle!=null){
            Main.routeStyle = mapStyle;
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

                ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(animateTextView, "translationY",0f,-(height/3.2f)); //700
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
}
