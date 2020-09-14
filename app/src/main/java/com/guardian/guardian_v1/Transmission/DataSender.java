package com.guardian.guardian_v1.Transmission;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.guardian.guardian_v1.R;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataSender extends AsyncTask<String, Void, String> {
    Context context;
    private Toast toast;

    public DataSender(Context ctx) {
        context = ctx;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... strings) {
        String register_url = "http://www.guardianapp.ir/insert_driver_data_asd.php";

        try {
            String token = strings[0];
            String sleep = strings[1];
            String time_d = strings[2];
            String speed = strings[3];
            String trip_d = strings[4];
            String road = strings[5];
            String traffic = strings[6];
            String weather = strings[7];
            String rad_30 = strings[8];
            String vibration = strings[9];
            String acceleration = strings[10];
            String month = strings[11];
            String average = strings[12];
            String reg_date = LocalDateTime.now().toString();

            URL url = new URL(register_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
                    + "&" + URLEncoder.encode("sleep_amount", "UTF-8") + "=" + URLEncoder.encode(sleep, "UTF-8")
                    + "&" + URLEncoder.encode("time_data", "UTF-8") + "=" + URLEncoder.encode(time_d, "UTF-8")
                    + "&" + URLEncoder.encode("car_speed", "UTF-8") + "=" + URLEncoder.encode(speed, "UTF-8")
                    + "&" + URLEncoder.encode("trip_duration", "UTF-8") + "=" + URLEncoder.encode(trip_d, "UTF-8")
                    + "&" + URLEncoder.encode("road_type", "UTF-8") + "=" + URLEncoder.encode(road, "UTF-8")
                    + "&" + URLEncoder.encode("traffic", "UTF-8") + "=" + URLEncoder.encode(traffic, "UTF-8")
                    + "&" + URLEncoder.encode("weather", "UTF-8") + "=" + URLEncoder.encode(weather, "UTF-8")
                    + "&" + URLEncoder.encode("radius_30km", "UTF-8") + "=" + URLEncoder.encode(rad_30, "UTF-8")
                    + "&" + URLEncoder.encode("car_vibration", "UTF-8") + "=" + URLEncoder.encode(vibration, "UTF-8")
                    + "&" + URLEncoder.encode("accelerometer", "UTF-8") + "=" + URLEncoder.encode(acceleration, "UTF-8")
                    + "&" + URLEncoder.encode("month_data", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")
                    + "&" + URLEncoder.encode("average", "UTF-8") + "=" + URLEncoder.encode(average, "UTF-8")
                    + "&" + URLEncoder.encode("reg_date", "UTF-8") + "=" + URLEncoder.encode(reg_date, "UTF-8")
                    ;

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
            SignUp.setRegisterResult(result);
            System.err.println(result);

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
