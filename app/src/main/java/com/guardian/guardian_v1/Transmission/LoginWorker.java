package com.guardian.guardian_v1.Transmission;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.guardian_v1.MainActivity;
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

public class LoginWorker extends AsyncTask<String,Void,String> {
    Context context;
    private Toast toast;
    public LoginWorker(Context ctx){
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

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")
                        +"&"+URLEncoder.encode("version","UTF-8")+"="+URLEncoder.encode(MainActivity.appVersion,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result="";
                String line="";

                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                System.err.println("===========//================");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                SignIn.setLoginResult(result);
                System.err.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
//        toast.show();

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
