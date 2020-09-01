package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.mapbox.mapboxsdk.maps.Style;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Switch mapStyleSwitch = findViewById(R.id.mapStyleSwitch);
        if(Main.routeStyle.equals(Style.DARK)){
            mapStyleSwitch.setChecked(true);
        } else {
            mapStyleSwitch.setChecked(false);
        }


        mapStyleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Main.routeStyle = Style.DARK;
                    writeFile(Style.DARK);
                } else {
                    Main.routeStyle = Style.LIGHT;
                    writeFile(Style.LIGHT);
                }
            }
        });


        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Setting.this, SelectNavigation.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void writeFile(String textToSave) {
        File dir = new File(this.getFilesDir(), "settings.txt");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput("settings.txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}