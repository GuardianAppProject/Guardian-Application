package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private static int TIME_OUT = 2500; //Time to launch the another activity
    private static int TIME_OUT2 = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUp = (Button) findViewById(R.id.SignUpButt);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent i = new Intent(SignUp.this, SeatBelt.class);
                startActivity(i);
                finish();
            }
        });

        Button signIn = (Button) findViewById(R.id.signUpIn);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent i = new Intent(SignUp.this, SignIn.class);
                startActivity(i);
                finish();
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, TIME_OUT);


//        final pl.droidsonroids.gif.GifImageView gifImageView = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.logoGif);
//        final ImageView logoImage = (ImageView) findViewById(R.id.logo_SignUp);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gifImageView.setVisibility(View.GONE);
//                logoImage.setVisibility(View.VISIBLE);
//            }
//        }, TIME_OUT2);



    }

}
