package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

//        Button backButton = (Button) findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Info.this, Main.class);
//                startActivity(i);
//                finish();
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Info.this, Main.class);
        startActivity(i);
        finish();
    }

    public void clickOnBtn(View v){
        Intent i = new Intent(Info.this, Main.class);
        startActivity(i);
        finish();
    }
}