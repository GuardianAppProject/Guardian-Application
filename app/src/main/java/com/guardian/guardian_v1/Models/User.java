package com.guardian.guardian_v1.Models;

import android.util.Log;

import com.guardian.guardian_v1.EncodeDecode;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    // Singleton
    private static User single_instance = null;

    // private constructor restricted to this class itself
    private User() {}

    // static method to create instance of Singleton class
    public static User getInstance() {
        if (single_instance == null)
            single_instance = new User();

        return single_instance;
    }


    private String phoneNumberText;
    private String usernameText;
    private String safety;
    private String textView1;
    private String textView2;
    private String textView3;
    private String textView4;
    private String textView5;
    private String textView6;
    private String textView7;
    private String textView8;
    private String textView9;
    private String textView10;
    private String textView11;

    public void updateUserData(String[] data){


        //inja ans.split dare az onvar miad, chizmiz haro tike tike mikonim set mikonim roye textbox haye xml
        getInstance().phoneNumberText = data[2];
        getInstance().usernameText = data[3];
        String safety = data[4];
        if(isNumeric(safety)) {
            getInstance().textView1 = (Math.round(Double.parseDouble(safety) * 100.0) / 100.0) + "%";
            getInstance().safety = EncodeDecode.calculateStatusAlgorithm(Math.round(Double.parseDouble(safety)));
        } else {
            getInstance().textView1 = safety;
        }
        Log.d("safetyy", safety);
        String speed = data[5];
        if(isNumeric(speed)) {
            getInstance().textView2 = EncodeDecode.speedDecode(Double.parseDouble(speed));
        } else {
            getInstance().textView2 = speed;
        }
        String nonstop = data[6];
        if(isNumeric(nonstop)) {
            getInstance().textView3 = EncodeDecode.withoutStopDecode(Double.parseDouble(nonstop));
        } else {
            getInstance().textView3 = nonstop;
        }
        String vibration = data[7];
        if(isNumeric(vibration)) {
            getInstance().textView4 = EncodeDecode.vibrationDecode(Double.parseDouble(vibration));
        } else {
            getInstance().textView4 = vibration;
        }
        String sleep = data[8];
        if(isNumeric(sleep)) {
            getInstance().textView5 = EncodeDecode.sleepDecode(Double.parseDouble(sleep));
        } else {
            getInstance().textView5 = sleep;
        }
        String acceleration = data[9];
        if(isNumeric(acceleration)) {
            getInstance().textView6 = EncodeDecode.sleepDecode(Double.parseDouble(acceleration));
        } else {
            getInstance().textView6 = acceleration;
        }
        String time = data[10];
        if(isNumeric(time)) {
            getInstance().textView7 = EncodeDecode.timeDecode(Double.parseDouble(time));
        } else {
            getInstance().textView7 = time;
        }
        String danger_zone = data[11];
        if(isNumeric(danger_zone)) {
            getInstance().textView8 = EncodeDecode.nearCitiesDecode(Double.parseDouble(danger_zone));
        } else {
            getInstance().textView8 = danger_zone;
        }
        String weather = data[12];
        if(isNumeric(weather)) {
            getInstance().textView9 = EncodeDecode.weatherDecode(Double.parseDouble(weather));
        } else {
            getInstance().textView9 = weather;
        }
        String road_type = data[13];
        if(isNumeric(road_type)) {
            getInstance().textView10 = EncodeDecode.roadTypeDecode(Double.parseDouble(road_type));
        } else {
            getInstance().textView10 = road_type;
        }
        String traffic = data[14];
        if(isNumeric(traffic)) {
            getInstance().textView11 = EncodeDecode.monthDecode(Double.parseDouble(traffic));
        } else {
            getInstance().textView11 = traffic;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String getPhoneNumberText() {
        return phoneNumberText;
    }

    public String getUsernameText() {
        return usernameText;
    }

    public String getSafety() {
        return safety;
    }

    public String getTextView1() {
        return textView1;
    }

    public String getTextView2() {
        return textView2;
    }

    public String getTextView3() {
        return textView3;
    }

    public String getTextView4() {
        return textView4;
    }

    public String getTextView5() {
        return textView5;
    }

    public String getTextView6() {
        return textView6;
    }

    public String getTextView7() {
        return textView7;
    }

    public String getTextView8() {
        return textView8;
    }

    public String getTextView9() {
        return textView9;
    }

    public String getTextView10() {
        return textView10;
    }

    public String getTextView11() {
        return textView11;
    }
}
