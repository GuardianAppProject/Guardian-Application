package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import static java.lang.Thread.sleep;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        updatePrivateData();

//        Button backButton = (Button) findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MyAccount.this, Main.class);
//                startActivity(i);
//                finish();
//
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.appThemeColor));
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.logout_alert, null);

        Button logoutYes = view.findViewById(R.id.logoutYesButton);
        logoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutWorker logoutWorker = new LogoutWorker(MyAccount.this);
                logoutWorker.execute("logout",getToken());

                Intent i = new Intent(MyAccount.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        Button logoutNo = view.findViewById(R.id.logoutNoBtn);
        logoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(view);
        builder.show();


    }

    private void onClickEdit(){
        EditText editText = (EditText)findViewById(R.id.newPassword);
        EditWorker editWorker = new EditWorker(this);
        editWorker.execute("edit",getToken(),editText.getText().toString());
        Toast.makeText(this,getToken() + "   " + editText.getText().toString(),Toast.LENGTH_LONG);//.show();
        TokenChecker.beginCheck(read(),this);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updatePrivateData();
    }

    private void updatePrivateData(){
        TextView username = (TextView) findViewById(R.id.username_accountField);
        TextView phoneNum = (TextView) findViewById(R.id.phoneNum_accountField);
        TextView userPass = (TextView) findViewById(R.id.password_accountField);

        username.setText(TokenChecker.getUsername());
        phoneNum.setText(TokenChecker.getPhoneNum());
        userPass.setText(TokenChecker.getUserPass());
    }

    public String read(){
        //reading text from file
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyAccount.this, Main.class);
        startActivity(i);
        finish();
    }

    public void clickOnBtn(View v) {
        Intent i = new Intent(MyAccount.this, Main.class);
        startActivity(i);
        finish();
    }
}