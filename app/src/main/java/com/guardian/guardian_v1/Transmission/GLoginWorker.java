package com.guardian.guardian_v1.Transmission;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.guardian_v1.R;
import com.guardian.guardian_v1.SignIn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GLoginWorker extends AsyncTask<String,Void,String> {
    Context context;
    private Toast toast;
    public GLoginWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String login_url = "https://www.guardianapp.ir/login555555.php" ;
        if(type.equals("login")){
            try {
                String username = strings[1];
                String password = strings[2];

                //1- post user/pass data

                //2- string result javabie ke be ma mide (= token) va bayad pass dade beshe
                String result = "";

                
                SignIn.setLoginResult(result);
                System.err.println(result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
        toast = Toast.makeText(context,result,Toast.LENGTH_LONG);
        toast.show();

        TextView messageText = ((Activity)context).findViewById(R.id.messageTextSignIn);
        if(result.contains("login complete")) {
            messageText.setText("لطفا چند لحظه منتظر بمانید.");
            messageText.setTextColor(context.getResources().getColor(R.color.colorPositiveError));
        } else if(result.contains("invalid characters were detected")) {
            messageText.setText("لطفا از کاراکتر های غیر مجاز مثل '*' و 'فاصله' استفاده نکنید.");
            messageText.setTextColor(context.getResources().getColor(R.color.colorNegativeError));
        } else if(result.contains("login failed")) {
            messageText.setText("نام کاربری یا رمز عبور صحیح نمی باشد.");
            messageText.setTextColor(context.getResources().getColor(R.color.colorNegativeError));
        } else {
            messageText.setText("سرور پاسخگو نمی باشد؛ لطفا چند دقیقه دیگر تلاش کنید.");
            messageText.setTextColor(context.getResources().getColor(R.color.colorNegativeError));
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
