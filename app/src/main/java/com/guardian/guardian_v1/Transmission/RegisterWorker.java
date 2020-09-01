package com.guardian.guardian_v1.Transmission;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.guardian.guardian_v1.SignUp;

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

public class RegisterWorker extends AsyncTask<String,Void,String> {
    Context context;
    private Toast toast;
    public RegisterWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String register_url = "http://www.guardianapp.ir/register747380.php" ;
        if(type.equals("register")){
            try {
                String username = strings[1];
                String password = strings[2];
                String phoneNum = strings[3];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                //khate zir bayad edit beshe ke etelaate ziadtar ro post kone baraye register kardan
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")
                        +"&"+URLEncoder.encode("number","UTF-8")+"="+URLEncoder.encode(phoneNum,"UTF-8");

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
                SignUp.setRegisterResult(result);
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
        toast.show();
        if(result.startsWith("Success")){
            //bayad baade register e moafagh login  e moafagh ham dashte bashim
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
