package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static java.lang.Thread.sleep;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button signIn = (Button) findViewById(R.id.SignInButt);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                onSignInClick(v);
                //if(!isLoginResultValid()) return;
                //Intent i = new Intent(SignIn.this, SeatBelt.class);
                //startActivity(i);
                //finish();
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
        String username = "username az koja begiram?";
        String password = "va password";
        LoginWorker loginWorker = new LoginWorker(this);
        loginWorker.execute("login",username,password);
        System.out.println("============================================================");
        try {
            sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String loginResult;

    protected static void setLoginResult(String result){
        loginResult  = result;
    }

    private boolean isLoginResultValid(){
        return loginResult.contains("login complete");
    }
}
