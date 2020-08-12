package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                loginResult = "asd";
                onSignInClick(v);
                try {
                    sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!isLoginResultValid()) return;
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
            sleep(300);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String loginResult = "asd";

    protected static void setLoginResult(String result){
        loginResult  = result;
    }

    private boolean isLoginResultValid(){
        return loginResult.contains("login complete");
    }
}
