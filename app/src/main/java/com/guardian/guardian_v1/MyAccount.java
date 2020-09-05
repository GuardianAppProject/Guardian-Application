package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.guardian.guardian_v1.Transmission.EditWorker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccount.this, SelectNavigation.class);
                startActivity(i);
                finish();

            }
        });
        Context thisCtx = this;
        Button editButton = (Button) findViewById(R.id.editButton);
        EditText editText = (EditText)findViewById(R.id.newPassword);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditWorker editWorker = new EditWorker(thisCtx);
                editWorker.execute("edit",getToken(),editText.getText().toString());
            }
        });
    }

    public String getToken() {
        StringBuilder stringBuffer = new StringBuilder("");
        try {
            FileInputStream fileInputStream = openFileInput("guardian_token.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines);
            }
        } catch (FileNotFoundException exp) {
            System.out.println("==========||==========");
            exp.printStackTrace();
        } catch (IOException exp) {
            System.out.println("==========||==========22222222");
            exp.printStackTrace();
        }
        return stringBuffer.toString();
    }
}