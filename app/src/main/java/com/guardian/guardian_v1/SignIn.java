package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.guardian.guardian_v1.Transmission.LoginWorker;

import static java.lang.Thread.sleep;

public class SignIn extends AppCompatActivity {

    LinearLayout signInProgress;
    private boolean hidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText edittext = (EditText)findViewById(R.id.password);
        edittext.setTransformationMethod(new AsteriskPasswordTransformationMethod());

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


        Button signIn = (Button) findViewById(R.id.SignInButt);
        signInProgress = findViewById(R.id.signInProgress);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loginResult = "asd";
                onSignInClick(v);
                try {
                    signInProgress.setVisibility(View.VISIBLE);
                    sleep(150);
                    signInProgress.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!isLoginResultValid()) return;
                TokenManager.getInstance().saveToken(loginResult.substring(25));
                Intent i = new Intent(SignIn.this, SeatBelt.class);
                startActivity(i);
                finish();
            }
        });

        Button signUp = (Button) findViewById(R.id.signInUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
                finish();

            }
        });
    }

    protected void onSignInClick(View v){
        EditText enteredUN = (EditText) findViewById(R.id.username);
        EditText enteredPW = (EditText) findViewById(R.id.password);
        String username = enteredUN.getText().toString();
        String password = enteredPW.getText().toString();
        LoginWorker loginWorker = new LoginWorker(this);
        loginWorker.execute("login",username,password);
        try {
            signInProgress.setVisibility(View.VISIBLE);
            sleep(500);
            signInProgress.setVisibility(View.INVISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String loginResult = "asd";

    public static void setLoginResult(String result){
        loginResult  = result;
    }

    private boolean isLoginResultValid(){
        return loginResult.contains("login complete");
    }
}
