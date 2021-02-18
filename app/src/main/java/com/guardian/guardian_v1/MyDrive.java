package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.guardian.guardian_v1.Models.User;

public class MyDrive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drive);

        TextView usernameText = findViewById(R.id.usernameTextView);
        TextView phoneNumberText = findViewById(R.id.phoneNumberTextView);
        TextView safetyStatus = findViewById(R.id.safetyStatus);
        TextView textView1 = findViewById(R.id.text1);
        TextView textView2 = findViewById(R.id.text2);
        TextView textView3 = findViewById(R.id.text3);
        TextView textView4 = findViewById(R.id.text4);
        TextView textView5 = findViewById(R.id.text5);
        TextView textView6 = findViewById(R.id.text6);
        TextView textView7 = findViewById(R.id.text7);
        TextView textView8 = findViewById(R.id.text8);
        TextView textView9 = findViewById(R.id.text9);
        TextView textView10 = findViewById(R.id.text10);
        TextView textView11 = findViewById(R.id.text11);

        usernameText.setText(User.getInstance().getUsernameText());
        phoneNumberText.setText(User.getInstance().getPhoneNumberText());
        safetyStatus.setText(User.getInstance().getSafety());
        textView1.setText(User.getInstance().getTextView1());
        textView2.setText(User.getInstance().getTextView2());
        textView3.setText(User.getInstance().getTextView3());
        textView4.setText(User.getInstance().getTextView4());
        textView5.setText(User.getInstance().getTextView5());
        textView6.setText(User.getInstance().getTextView6());
        textView7.setText(User.getInstance().getTextView7());
        textView8.setText(User.getInstance().getTextView8());
        textView9.setText(User.getInstance().getTextView9());
        textView10.setText(User.getInstance().getTextView10());
        textView11.setText(User.getInstance().getTextView11());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyDrive.this, Main.class);
        startActivity(i);
        finish();
    }

    public void clickOnBtn(View v){
        Intent i = new Intent(MyDrive.this, Main.class);
        startActivity(i);
        finish();
    }

}