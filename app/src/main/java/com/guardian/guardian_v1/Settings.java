package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

//import com.mapbox.mapboxsdk.maps.Style;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Settings extends AppCompatActivity {

    SeekBar seekBar;
    TextView textView;
    int min = 0, max = 100, current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textView = findViewById(R.id.textView10);
        seekBar = findViewById(R.id.seekBar);
        int rep = Main.get_sound_repetition();
        if(rep <= 1) {
            current = 100;
            textView.setText("همیشه");
        } else if(rep <= 2) {
            current = 85;
            textView.setText("هر دقیقه");
        } else if(rep <= 4) {
            current = 75;
            textView.setText("هر ۲ دقیقه یکبار");
        } else if(rep <= 6) {
            current = 65;
            textView.setText("هر ۳ دقیقه یکبار");
        } else if(rep <= 10) {
            current = 55;
            textView.setText("هر ۵ دقیقه یکبار");
        } else if(rep <= 20) {
            current = 45;
            textView.setText("هر ۱۰ دقیقه یکبار");
        } else if(rep <= 40) {
            current = 35;
            textView.setText("هر ۲۰ دقیقه یکبار");
        } else if(rep <= 60) {
            current = 25;
            textView.setText("هر نیم ساعت یکبار");
        } else if(rep <= 120) {
            current = 15;
            textView.setText("هر یک ساعت یکبار");
        } else {
            current = 5;
            textView.setText("هیچ وقت");
        }
        seekBar.setProgress(current);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current = progress + min;
                if(current <= 10) {
                    textView.setText("هیچ وقت");
                    Main.set_sound_repetition(Integer.MAX_VALUE);
                } else if(current <= 20) {
                    textView.setText("هر یک ساعت یکبار");
                    Main.set_sound_repetition(120);
                } else if(current <= 30) {
                    textView.setText("هر نیم ساعت یکبار");
                    Main.set_sound_repetition(60);
                } else if(current <= 40) {
                    textView.setText("هر ۲۰ دقیقه یکبار");
                    Main.set_sound_repetition(40);
                } else if(current <= 50) {
                    textView.setText("هر ۱۰ دقیقه یکبار");
                    Main.set_sound_repetition(20);
                } else if(current <= 60) {
                    textView.setText("هر ۵ دقیقه یکبار");
                    Main.set_sound_repetition(10);
                } else if(current <= 70) {
                    textView.setText("هر ۳ دقیقه یکبار");
                    Main.set_sound_repetition(6);
                } else if(current <= 80) {
                    textView.setText("هر ۲ دقیقه یکبار");
                    Main.set_sound_repetition(4);
                } else if(current <= 90) {
                    textView.setText("هر دقیقه");
                    Main.set_sound_repetition(2);
                } else if(current <= 100) {
                    textView.setText("همیشه");
                    Main.set_sound_repetition(0);
                }
//                textView.setText("" + current);
                writeFile(String.valueOf(Main.get_sound_repetition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        androidx.appcompat.widget.SwitchCompat dangerModeSwitch = findViewById(R.id.dangerModeSwitch);

        if(Main.dangerModeOn){
            dangerModeSwitch.setChecked(true);
        } else {
            dangerModeSwitch.setChecked(false);
        }

        dangerModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Main.dangerModeOn = true;
                    writeFile2(String.valueOf(true));
                } else {
                    Main.dangerModeOn = false;
                    writeFile2(String.valueOf(false));
                }
            }
        });


        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, Main.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void writeFile(String textToSave) {
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

    public void writeFile2(String textToSave) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("dangermode.txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Settings.this, Main.class);
        startActivity(i);
        finish();
    }

    public void clickOnBtn(View v) {
        Intent i = new Intent(Settings.this, Main.class);
        startActivity(i);
        finish();
    }
}