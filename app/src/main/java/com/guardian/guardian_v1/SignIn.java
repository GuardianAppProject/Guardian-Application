package com.guardian.guardian_v1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.guardian_v1.PasswordManager.AsteriskPasswordTransformationMethod;
import com.guardian.guardian_v1.PasswordManager.DoNothingTransformationMethod;
import com.guardian.guardian_v1.Transmission.LoginWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.Thread.sleep;

public class SignIn extends Activity {

    LinearLayout signInProgress;
    private boolean hidePassword = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appThemeColor));


        EditText edittext = (EditText)findViewById(R.id.password);
        edittext.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        ImageButton showPasswordBtn =  findViewById(R.id.passwordLock);
        showPasswordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(hidePassword) {
                    edittext.setTransformationMethod(new DoNothingTransformationMethod());
                    showPasswordBtn.setBackgroundResource(R.drawable.padlock1);
                    hidePassword = false;
                } else {
                    edittext.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                    showPasswordBtn.setBackgroundResource(R.drawable.padlock2);
                    hidePassword = true;
                }

            }
        });


        Button signIn = (Button) findViewById(R.id.SignInButt);
        signInProgress = findViewById(R.id.signInProgress);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loginResult = "asd";
                onSignInClick(v);
                try {
                    signInProgress.setVisibility(View.VISIBLE);
                    sleep(250);
                    signInProgress.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!isLoginResultValid()) return;
//                saveToken(loginResult.substring(25));
                write(loginResult.substring(37));
                Toast.makeText(SignIn.this, loginResult.substring(37), Toast.LENGTH_SHORT).show();
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

        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    signIn.performClick();
                }
                return false;
            }
        });
    }

    public void write(String toWrite) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("tokenFile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(toWrite);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        token = toWrite;
    }

    public String read(){
        //reading text from file
        String string = "";
        try {
            FileInputStream fileIn= openFileInput("tokenFile.txt");
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

    public void saveToken(String textToSave) {
        File dir = new File(this.getFilesDir(), "guardian_token.txt");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput("guardian_token.txt", MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    protected void onSignInClick(View v){
        EditText enteredUN = (EditText) findViewById(R.id.username);
        EditText enteredPW = (EditText) findViewById(R.id.password);
        String username = enteredUN.getText().toString();
        String password = enteredPW.getText().toString();
        LoginWorker loginWorker = new LoginWorker(this);
        loginWorker.execute("login",username,password);
        try {
            signInProgress.setVisibility(View.VISIBLE);
            sleep(500);
            signInProgress.setVisibility(View.INVISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String loginResult = "asd";

    public static void setLoginResult(String result){
        loginResult  = result;
    }

    private boolean isLoginResultValid(){
        return loginResult.contains("login complete");
    }
    private static String token;
    protected static String getToken(){
        return token;
    }

    public static void setToken(String token) {
        SignIn.token = token;
    }
}
