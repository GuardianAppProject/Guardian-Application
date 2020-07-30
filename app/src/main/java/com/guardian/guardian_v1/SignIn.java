package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button signUp = (Button) findViewById(R.id.SignInButt);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent i = new Intent(SignIn.this, SeatBelt.class);
                startActivity(i);
                finish();
            }
        });

        Button signIn = (Button) findViewById(R.id.signInUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                /*Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
                finish();*/
                onSignInClick(v);
            }
        });
    }

    protected void onSignInClick(View v){
        Intent i = new Intent(SignIn.this, SignUp.class);
        startActivity(i);
        finish();
    }
}
