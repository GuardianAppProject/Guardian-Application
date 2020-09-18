package com.guardian.guardian_v1.Transmission;

import android.content.Context;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class TokenChecker extends AsyncTask<String,Void,String> {
    Context context;
    private Toast toast;
    private static String ans="asd";

    public TokenChecker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String login_url = "https://www.guardianapp.ir/check_token_validity.php";
        if (type.equals("check")) {
            try {
                String token = strings[1];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8") ;

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.err.println(result);
                ans = result;
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
        toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public static void beginCheck(String token,Context ctx){
        TokenChecker checker = new TokenChecker(ctx);
        checker.execute("check",token);
    }

    public static boolean tokenIsValid(){
        return ans.startsWith("Connected - True");
    }

    public static String getUsername(){
        if(tokenIsValid())
            return ans.substring(17).split(" ")[1];
        return "asd";
    }

    public static String getPhoneNum(){
        if(tokenIsValid())
            return ans.substring(17).split(" ")[0];
        return "asd";
    }

    public static String getUserPass(){
        if(tokenIsValid())
            return ans.substring(17).split(" ")[2];
        return "asd";
    }
}
