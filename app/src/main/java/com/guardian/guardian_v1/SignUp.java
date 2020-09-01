package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import static java.lang.Thread.sleep;

public class SignUp extends AppCompatActivity {

    private static int TIME_OUT = 2500; //Time to launch the another activity
    private static int TIME_OUT2 = 3000;

    LinearLayout signUpProgress;
    private boolean hidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText edittext = (EditText)findViewById(R.id.password);
        edittext.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        Button signUp = (Button) findViewById(R.id.SignUpButt);
        signUpProgress = findViewById(R.id.signUpProgress);

        ImageButton showPasswordBtn =  findViewById(R.id.passwordLock);
        showPasswordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(hidePassword) {
                    edittext.setTransformationMethod(new DoNothingTransformation());
                    showPasswordBtn.setBackgroundResource(R.drawable.padlock1);
                    hidePassword = false;
                } else {
                    edittext.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    showPasswordBtn.setBackgroundResource(R.drawable.padlock2);
                    hidePassword = true;
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //inja bayad bebinim register mishe ya na
                registerResult = "asd";
                onSignUpClick(v);
                try {
                    signUpProgress.setVisibility(View.VISIBLE);
                    sleep(150);
                    signUpProgress.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!isRegisterResultValid()) return;

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

    protected void onSignUpClick(View v){
        EditText enteredUN = (EditText) findViewById(R.id.username);
        EditText enteredPW = (EditText) findViewById(R.id.password);
        EditText enteredNum = (EditText) findViewById(R.id.phoneNum);

        String username = enteredUN.getText().toString();
        String password = enteredPW.getText().toString();
        String phone = enteredNum.getText().toString();

        RegisterWorker registerWorker = new RegisterWorker(this);
        registerWorker.execute("register",username,password,phone);
        try {
            signUpProgress.setVisibility(View.VISIBLE);
            sleep(300);
            signUpProgress.setVisibility(View.INVISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String registerResult = "asd";

    protected static void setRegisterResult(String result){
        registerResult  = result;
    }

    private boolean isRegisterResultValid(){
        return registerResult.contains("register complete");
    }

}



