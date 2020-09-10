package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.guardian_v1.Transmission.EditWorker;
import com.guardian.guardian_v1.Transmission.LogoutWorker;
import com.guardian.guardian_v1.Transmission.TokenChecker;

import org.w3c.dom.Text;

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
        updatePrivateData();

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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEdit();
            }
        });

        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                onClickLogout();
                Intent i = new Intent(MyAccount.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public String getToken() {
        String string = "";
        try {
            FileInputStream fileIn=openFileInput("tokenFile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[10000];

            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                string +=readstring;
            }
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;

    }

    private void onClickLogout(){
        LogoutWorker logoutWorker = new LogoutWorker(this);
        logoutWorker.execute("logout",getToken());
    }

    private void onClickEdit(){
        EditText editText = (EditText)findViewById(R.id.newPassword);
        EditWorker editWorker = new EditWorker(this);
        editWorker.execute("edit",getToken(),editText.getText().toString());
        Toast.makeText(this,getToken() + "   " + editText.getText().toString(),Toast.LENGTH_LONG).show();
    }

    private void updatePrivateData(){
        TextView username = (TextView) findViewById(R.id.username_accountField);
        TextView phoneNum = (TextView) findViewById(R.id.phoneNum_accountField);
        TextView userPass = (TextView) findViewById(R.id.password_accountField);

        username.setText(TokenChecker.getUsername());
        phoneNum.setText(TokenChecker.getPhoneNum());
        userPass.setText(TokenChecker.getUserPass());
    }
}