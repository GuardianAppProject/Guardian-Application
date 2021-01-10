package com.guardian.guardian_v1;


import android.content.Context;
import android.util.Log;

import com.guardian.guardian_v1.DriveStatus.GPSTracker;
import com.guardian.guardian_v1.DriveStatus.PersianCalender;
import com.guardian.guardian_v1.DriveStatus.RoadInformation;
import com.guardian.guardian_v1.DriveStatus.Shake;
import com.guardian.guardian_v1.DriveStatus.Time;
import com.guardian.guardian_v1.DriveStatus.Weather;
import com.guardian.guardian_v1.Transmission.DataSender;

import java.io.IOException;
import java.util.ArrayList;

//import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class StatusCalculator {

    //Morteza
    int speedLimit = 0;
    int lanes = 0;
    boolean oneway = false;
    double latitude, longtitude, distance;
    String city;
    RoadInformation.HighwayTags highwayType = RoadInformation.HighwayTags.road;
    public static double staticUserSpeed;
    public static long totalTime;
    public static long totalDrive;
    public static long nonStop;
    public static double acceleration;
    public static Shake.ShakeSituation vibration;

    private int cycle = 0;
    private ArrayList<Double> sleep_data = new ArrayList<>();
    private ArrayList<Double> speed_data = new ArrayList<>();
    private ArrayList<Double> acceleration_data = new ArrayList<>();
    private ArrayList<Double> vibration_data = new ArrayList<>();
    private ArrayList<Double> time_data = new ArrayList<>();
    private ArrayList<Double> nearCities_data = new ArrayList<>();
    private ArrayList<Double> month_data = new ArrayList<>();
    private ArrayList<Double> weather_data = new ArrayList<>();
    private ArrayList<Double> withoutStop_data = new ArrayList<>();
    private ArrayList<Double> traffic_data = new ArrayList<>();
    private ArrayList<Double> roadType_data = new ArrayList<>();

    ArrayList<String> alerts;

    private Time timeObj;
    private PersianCalender.SolarCalendar solarCalendar = new PersianCalender.SolarCalendar();

    Context context;

    public void setContext(Context context) {
        this.context = context;
    }



    public StatusCalculator(Context context) {
        timeObj = new Time();
        setContext(context);
    }


    public double sleepCalculator(double userSleep, double userAwake) {
        double sleep_factor = 0;
        if(userSleep < 180){
            sleep_factor = 0;
        } else if(userSleep < 240) {
            sleep_factor = (sleep_factor - 3) * 0.25;
        } else if(userSleep < 300) {
            sleep_factor = 15 + (sleep_factor - 4) * 0.25;
        } else if(userSleep < 360) {
            sleep_factor = 30 + (sleep_factor - 5) * 0.33;
        } else if(userSleep < 420) {
            sleep_factor = 50 + (sleep_factor - 6) * 0.33;
        } else if(userSleep < 480) {
            sleep_factor = 70 + (sleep_factor - 7) * 0.25;
        } else if(userSleep < 540) {
            sleep_factor = 85 + (sleep_factor - 8) * 0.25;
        } else {
            sleep_factor = 100;
        }

        double sleepCoefficient = 1;
        if(userAwake < 360) {
            //
        } else if(userAwake < 420) {
            sleepCoefficient = 0.9;
        }else if(userAwake < 480) {
            sleepCoefficient = 0.85;
        } else if(userAwake < 600) {
            sleepCoefficient = 0.8;
        } else if(userAwake < 720) {
            sleepCoefficient = 0.7;
        } else if(userAwake < 840) {
            sleepCoefficient = 0.6;
        } else if(userAwake < 960) {
            sleepCoefficient = 0.5;
        } else if(userAwake < 1080) {
            sleepCoefficient = 0.4;
        } else if(userAwake < 1200) {
            sleepCoefficient = 0.2;
        } else {
            sleepCoefficient = 0;
        }

        sleep_factor *= sleepCoefficient;

        if(sleep_factor <= 0) {
            sleep_factor = 0;
        }

        if(sleep_factor >= 100) {
            sleep_factor = 100;
        }

        return sleep_factor;
    }

    public double speedCalculator(double userSpeed, double speedLimit, Weather.WeatherType weatherType) {

        if(weatherType==Weather.WeatherType.Thunderstorm) {
            speedLimit -= 10;
        } else if(weatherType==Weather.WeatherType.Drizzle) {
            speedLimit -= 4;
        } else if(weatherType==Weather.WeatherType.Rain) {
            speedLimit -= 7;
        } else if(weatherType==Weather.WeatherType.Snow) {
            speedLimit -= 18;
        } else if(weatherType==Weather.WeatherType.Clear) {
            //
        } else if(weatherType==Weather.WeatherType.Clouds) {
            //
        } else if(weatherType==Weather.WeatherType.Mist) {
            speedLimit -= 12;
        } else if(weatherType==Weather.WeatherType.Smoke) {
            speedLimit -= 13;
        } else if(weatherType==Weather.WeatherType.Haze) {
            speedLimit -= 4;
        } else if(weatherType==Weather.WeatherType.Dust) {
            speedLimit -= 10;
        } else if(weatherType==Weather.WeatherType.Fog) {
            speedLimit -= 6;
        } else if(weatherType==Weather.WeatherType.Sand) {
            speedLimit -= 8;
        } else if(weatherType==Weather.WeatherType.Ash) {
            speedLimit -= 5;
        } else if(weatherType==Weather.WeatherType.Squall) {
            speedLimit -= 14;
        } else if(weatherType==Weather.WeatherType.Tornado) {
            speedLimit -= 7;
        }

        double speed_factor = 0;

        double speedCoefficient = 1;
        if(speedLimit <= 30) {
            speedCoefficient = 1.05;
        } else if(speedLimit <= 50) {
            speedCoefficient = 1.1;
        } else if(speedLimit <= 60) {
            speedCoefficient = 1.2;
        } else if(speedLimit <= 80) {
            speedCoefficient = 1.3;
        } else if(speedLimit <= 100) {
            speedCoefficient = 1.4;
        } else if(speedLimit <= 110) {
            speedCoefficient = 1.5;
        } else  {
            speedCoefficient = 1.85;
        }

        if((userSpeed - speedLimit) < -10) {
            speed_factor = 100;
        } else if((userSpeed - speedLimit) < -5) {
            speed_factor = 92;
        } else if((userSpeed - speedLimit) <= 0) {
            speed_factor = 82;
        } else if((userSpeed - speedLimit) < 5) {
            double s = speedCoefficient * (userSpeed - speedLimit);
            speed_factor = 82 - s;
        } else if((userSpeed - speedLimit) < 10) {
            double s = speedCoefficient * (userSpeed - speedLimit) * 1.3;
            speed_factor = 82 - s;
        } else if((userSpeed - speedLimit) < 20) {
            double s = speedCoefficient * (userSpeed - speedLimit) * 1.8;
            speed_factor = 82 - s;
        } else if((userSpeed - speedLimit) < 30) {
            double s = speedCoefficient * (userSpeed - speedLimit) * 2.5;
            speed_factor = 82 - s;
        }

        if(speedLimit >= 110 && userSpeed >= 15) {
            if(userSpeed <= 30) {
                speed_factor = 70;
            } else if(userSpeed <= 40) {
                speed_factor = 77;
            }
        }

        if(speed_factor <= 0) {
            speed_factor = 0;
        }

        if(speed_factor >= 100) {
            speed_factor = 100;
        }

        return speed_factor;
    }

    public double accelerationCalculator(double userAcceleration, Weather.WeatherType weatherType) {

        double standardAcceleration = 7.5;

        if(weatherType==Weather.WeatherType.Thunderstorm) {
            standardAcceleration -= 1.45;
        } else if(weatherType==Weather.WeatherType.Drizzle) {
            standardAcceleration -= 0.3;
        } else if(weatherType==Weather.WeatherType.Rain) {
            standardAcceleration -= 1;
        } else if(weatherType==Weather.WeatherType.Snow) {
            standardAcceleration -= 2.2;
        } else if(weatherType==Weather.WeatherType.Clear) {
            //
        } else if(weatherType==Weather.WeatherType.Clouds) {
            //
        } else if(weatherType==Weather.WeatherType.Mist) {
            standardAcceleration -= 0.5;
        } else if(weatherType==Weather.WeatherType.Smoke) {
            standardAcceleration -= 0.9;
        } else if(weatherType==Weather.WeatherType.Haze) {
            standardAcceleration -= 0.3;
        } else if(weatherType==Weather.WeatherType.Dust) {
            standardAcceleration -= 0.6;
        } else if(weatherType==Weather.WeatherType.Fog) {
            standardAcceleration -= 0.2;
        } else if(weatherType==Weather.WeatherType.Sand) {
            standardAcceleration -= 0.4;
        } else if(weatherType==Weather.WeatherType.Ash) {
            standardAcceleration -= 0.25;
        } else if(weatherType==Weather.WeatherType.Squall) {
            standardAcceleration -= 1;
        } else if(weatherType==Weather.WeatherType.Tornado) {
            standardAcceleration -= 1;
        }

        double accelerationCoefficient = 0.8;
        if(userAcceleration < 0) {
            accelerationCoefficient = 1.3;
        } else {
            //
        }

        double acceleration_factor = 0;

        if(Math.abs(userAcceleration) < (standardAcceleration - 1.8)) {
            acceleration_factor = 100;
        } else if(Math.abs(userAcceleration) < (standardAcceleration - 1.2)) {
            acceleration_factor = 95;
        } else if(Math.abs(userAcceleration) < (standardAcceleration - 0.6)) {
            acceleration_factor = 90;
        } else if(Math.abs(userAcceleration) < (standardAcceleration)) {
            acceleration_factor = 85;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 1)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 6;
            acceleration_factor = 85 - a;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 2)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 7;
            acceleration_factor = 85 - a;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 4)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 8;
            acceleration_factor = 85 - a;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 6)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 8.1;
            acceleration_factor = 85 - a;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 8)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 9;
            acceleration_factor = 85 - a;
        } else if(Math.abs(userAcceleration) < (standardAcceleration + 10)) {
            double a = (Math.abs(userAcceleration) - standardAcceleration) * accelerationCoefficient * 10;
            acceleration_factor = 85 - a;
        } else {
            acceleration_factor = 0;
        }

        if(acceleration_factor <= 0) {
            acceleration_factor = 0;
        }

        if(acceleration_factor >= 100) {
            acceleration_factor = 100;
        }

        return acceleration_factor;
    }


    public static void setVibration(Shake.ShakeSituation vibration) {
        StatusCalculator.vibration = vibration;
    }

    public double vibrationCalculator(Shake.ShakeSituation userVibration) {

        double vibration_factor = 0;
        if(userVibration== Shake.ShakeSituation.noShake) {
            vibration_factor = 100;
        } else if(userVibration== Shake.ShakeSituation.lowShake) {
            vibration_factor = 80;
        } else if(userVibration== Shake.ShakeSituation.mediumShake) {
            vibration_factor = 60;
        } else if(userVibration== Shake.ShakeSituation.highShake) {
            vibration_factor = 45;
        } else if(userVibration== Shake.ShakeSituation.veryHighShake) {
            vibration_factor = 30;
        }

        if(vibration_factor <= 0) {
            vibration_factor = 0;
        }

        if(vibration_factor >= 100) {
            vibration_factor = 100;
        }

        return vibration_factor;
    }

    public double timeCalculator(double userTimeHOUR, double userTimeMINUTE, double sunrise, double sunset) {

        double userTime = (userTimeHOUR * 60) + userTimeMINUTE;
        double time_factor = 0;

        if(userTime <= 60) {           // 0  -  1
            time_factor = 45;
        } else if(userTime <= 120) {   // 1  -  2
            time_factor = 42;
        } else if(userTime <= 180) {   // 2  -  3
            time_factor = 40;
        } else if(userTime <= 240) {   // 3  -  4
            time_factor = 42;
        } else if(userTime <= 300) {   // 4  -  5
            time_factor = 70;
        } else if(userTime <= 360) {   // 5  -  6
            time_factor = 85;
        } else if(userTime <= 420) {   // 6  -  7
            time_factor = 90;
        } else if(userTime <= 480) {   // 7  -  8
            time_factor = 90;
        } else if(userTime <= 540) {   // 8  -  9
            time_factor = 100;
        } else if(userTime <= 600) {   // 9  -  10
            time_factor = 95;
        } else if(userTime <= 660) {   // 10  -  11
            time_factor = 88;
        } else if(userTime <= 720) {   // 11  -  12
            time_factor = 75;
        }else if(userTime <= 780) {    // 12  -  13
            time_factor = 50;
        } else if(userTime <= 840) {   // 13  -  14
            time_factor = 35;
        } else if(userTime <= 900) {   // 14  -  15
            time_factor = 30;
        } else if(userTime <= 960) {   // 15  -  16
            time_factor = 30;
        } else if(userTime <= 1020) {  // 16  -  17
            time_factor = 45;
        } else if(userTime <= 1080) {  // 17  -  18
            time_factor = 50;
        } else if(userTime <= 1140) {  // 18  -  19
            time_factor = 55;
        } else if(userTime <= 1200) {  // 19  -  20
            time_factor = 55;
        } else if(userTime <= 1260) {  // 20  -  21
            time_factor = 70;
        } else if(userTime <= 1320) {  // 21  -  22
            time_factor = 85;
        } else if(userTime <= 1380) {  // 22  -  23
            time_factor = 80;
        } else if(userTime <= 1440) {  // 23  -  24
            time_factor = 75;
        }

        if(time_factor <= 0) {
            time_factor= 0;
        }

        if(time_factor >= 100) {
            time_factor = 100;
        }

        return time_factor;
    }

    public double nearCitiesCalculator(double userWithNearCityDistance) {

        double nearCities_factor = 0;

        if(userWithNearCityDistance <= 5) {
            nearCities_factor = 30;
        } else if(userWithNearCityDistance <= 10) {
            nearCities_factor = 25 - (( 5 - (userWithNearCityDistance - 5)));
        } else if(userWithNearCityDistance <= 30) {
            nearCities_factor = 50 - (( 20 - (userWithNearCityDistance - 10)) * 1.25);
        } else if(userWithNearCityDistance <= 40) {
            nearCities_factor = 55 - (( 10 - (userWithNearCityDistance - 40)) * 0.5);
        } else if(userWithNearCityDistance <= 50) {
            nearCities_factor = 60;
        } else if(userWithNearCityDistance <= 55) {
            nearCities_factor = 70;
        } else if(userWithNearCityDistance <= 60) {
            nearCities_factor = 85;
        } else {
            nearCities_factor = 100;
        }

        if(nearCities_factor <= 0) {
            nearCities_factor = 0;
        }

        if(nearCities_factor >= 100) {
            nearCities_factor = 100;
        }

        return nearCities_factor;
    }

    public double monthCalculator(int userMonth) {

        double month_factor = 0;

        if(userMonth == 1) {
            month_factor = 55;
        } else if(userMonth == 2) {
            month_factor = 80;
        } else if(userMonth == 3) {
            month_factor = 90;
        } else if(userMonth == 4) {
            month_factor = 60;
        } else if(userMonth == 5) {
            month_factor = 65;
        } else if(userMonth == 6) {
            month_factor = 50;
        } else if(userMonth == 7) {
            month_factor = 90;
        } else if(userMonth == 8) {
            month_factor = 65;
        } else if(userMonth == 9) {
            month_factor = 85;
        } else if(userMonth == 10) {
            month_factor = 85;
        } else if(userMonth == 11) {
            month_factor = 80;
        } else if(userMonth == 12) {
            month_factor = 70;
        }

        if(month_factor <= 0) {
            month_factor = 0;
        }

        if(month_factor >= 100) {
            month_factor = 100;
        }

        return month_factor;
    }

    public double weatherCalculator(double userTemperature, double userWindSpeed, Weather.WeatherType weatherType) {

        double weather_factor = 0;
        if(weatherType==Weather.WeatherType.Thunderstorm) {
            weather_factor = 60;
        } else if(weatherType==Weather.WeatherType.Drizzle) {
            weather_factor = 88;
        } else if(weatherType==Weather.WeatherType.Rain) {
            weather_factor = 78;
        } else if(weatherType==Weather.WeatherType.Snow) {
            weather_factor = 50;
        } else if(weatherType==Weather.WeatherType.Clear) {
            weather_factor = 96;
        } else if(weatherType==Weather.WeatherType.Clouds) {
            weather_factor = 100;
        } else if(weatherType==Weather.WeatherType.Mist) {
            weather_factor = 70;
        } else if(weatherType==Weather.WeatherType.Smoke) {
            weather_factor = 60;
        } else if(weatherType==Weather.WeatherType.Haze) {
            weather_factor = 80;
        } else if(weatherType==Weather.WeatherType.Dust) {
            weather_factor = 75;
        } else if(weatherType==Weather.WeatherType.Fog) {
            weather_factor = 82;
        } else if(weatherType==Weather.WeatherType.Sand) {
            weather_factor = 75;
        } else if(weatherType==Weather.WeatherType.Ash) {
            weather_factor = 80;
        } else if(weatherType==Weather.WeatherType.Squall) {
            weather_factor = 58;
        } else if(weatherType==Weather.WeatherType.Tornado) {
            weather_factor = 58;
        }

        if(userTemperature < -20) {
            weather_factor -= 25;
        } else if(userTemperature < -10) {
            weather_factor -= 10;
        } else if(userTemperature < 0) {
            weather_factor -= 5;
        } else if(userTemperature < 10) {
            weather_factor -= 2;
        } else if(userTemperature < 25) {
            //
        } else if(userTemperature < 30) {
            weather_factor -= 4;
        } else if(userTemperature < 40) {
            weather_factor -= 8;
        } else if(userTemperature < 50) {
            weather_factor -= 18;
        } else if(userTemperature < 60) {
            weather_factor -= 25;
        } else {
            weather_factor -= 34;
        }

        if(weather_factor <= 0) {
            weather_factor = 0;
        }

        if(weather_factor >= 100) {
            weather_factor = 100;
        }

        return weather_factor;
    }

    public double withoutStopDrivingCalculator(double userWithoutStopDriving, double userTotalDriving, double userTotalRest, double userTimeHOUR, double userTimeMINUTE) {


        Log.d("wstop", "userWithoutStopDriving" + userWithoutStopDriving + "userTotalDriving" + userTotalDriving + "userTotalRest" + userTotalRest
        + "usermin" + userTimeMINUTE + "userho" + userTimeHOUR);
        double userTime = (userTimeHOUR * 60) + userTimeMINUTE;

        double standardRestTime = 0;
        if(userTime <= 60) {           // 0  -  1
            standardRestTime = 140;
        } else if(userTime <= 120) {   // 1  -  2
            standardRestTime = 130;
        } else if(userTime <= 180) {   // 2  -  3
            standardRestTime = 120;
        } else if(userTime <= 240) {   // 3  -  4
            standardRestTime = 100;
        } else if(userTime <= 300) {   // 4  -  5
            standardRestTime = 105;
        } else if(userTime <= 360) {   // 5  -  6
            standardRestTime = 130;
        } else if(userTime <= 420) {   // 6  -  7
            standardRestTime = 150;
        } else if(userTime <= 480) {   // 7  -  8
            standardRestTime = 150;
        } else if(userTime <= 540) {   // 8  -  9
            standardRestTime = 150;
        } else if(userTime <= 600) {   // 9  -  10
            standardRestTime = 150;
        } else if(userTime <= 660) {   // 10  -  11
            standardRestTime = 150;
        } else if(userTime <= 720) {   // 11  -  12
            standardRestTime = 150;
        }else if(userTime <= 780) {    // 12  -  13
            standardRestTime = 145;
        } else if(userTime <= 840) {   // 13  -  14
            standardRestTime = 110;
        } else if(userTime <= 900) {   // 14  -  15
            standardRestTime = 110;
        } else if(userTime <= 960) {   // 15  -  16
            standardRestTime = 110;
        } else if(userTime <= 1020) {  // 16  -  17
            standardRestTime = 95;
        } else if(userTime <= 1080) {  // 17  -  18
            standardRestTime = 95;
        } else if(userTime <= 1140) {  // 18  -  19
            standardRestTime = 100;
        } else if(userTime <= 1200) {  // 19  -  20
            standardRestTime = 100;
        } else if(userTime <= 1260) {  // 20  -  21
            standardRestTime = 130;
        } else if(userTime <= 1320) {  // 21  -  22
            standardRestTime = 140;
        } else if(userTime <= 1380) {  // 22  -  23
            standardRestTime = 140;
        } else if(userTime <= 1440) {  // 23  -  24
            standardRestTime = 135;
        }


        double rest_factor = 0;
        if(userWithoutStopDriving <= standardRestTime - 30) {
            rest_factor = 100;
        } else if(userWithoutStopDriving <= standardRestTime - 20) {
            rest_factor = 98;
        } else if(userWithoutStopDriving <= standardRestTime - 10) {
            rest_factor = 95;
        } else if(userWithoutStopDriving <= standardRestTime) {
            rest_factor = 93;
        } else if(userWithoutStopDriving <= standardRestTime + 10) {
            rest_factor = 88;
        } else if(userWithoutStopDriving <= standardRestTime + 20) {
            rest_factor = 80;
        } else if(userWithoutStopDriving <= standardRestTime + 30) {
            rest_factor = 75;
        } else if(userWithoutStopDriving <= standardRestTime + 40) {
            rest_factor = 68;
        } else if(userWithoutStopDriving <= standardRestTime + 50) {
            rest_factor = 60;
        } else if(userWithoutStopDriving <= standardRestTime + 60) {
            rest_factor = 50;
        } else if(userWithoutStopDriving <= standardRestTime + 70) {
            rest_factor = 45;
        } else if(userWithoutStopDriving <= standardRestTime + 80) {
            rest_factor = 40;
        } else if(userWithoutStopDriving <= standardRestTime + 90) {
            rest_factor = 30;
        } else if(userWithoutStopDriving <= standardRestTime + 100) {
            rest_factor = 22;
        } else if(userWithoutStopDriving <= standardRestTime + 120) {
            rest_factor = 15;
        } else if(userWithoutStopDriving <= standardRestTime + 130) {
            rest_factor = 10;
        }  else {
            rest_factor = 0;
        }

        Log.d("with" , "" + rest_factor);
        if(userTotalDriving <= 300) { // 6
            rest_factor *= 1;
        } else if(userTotalDriving <= 480) { // 8
            rest_factor *= 0.95;
        } else if(userTotalDriving <= 600) { // 10
            rest_factor *= 0.9;
        } else if(userTotalDriving <= 720) { // 12
            rest_factor *= 0.85;
        } else if(userTotalDriving <= 900) { // 15
            rest_factor *= 0.8;
        } else if(userTotalDriving <= 1080) { // 18
            rest_factor *= 0.7;
        } else if(userTotalDriving <= 1260) { // 21
            rest_factor *= 0.6;
        } else if(userTotalDriving <= 1440) { // 24
            rest_factor *= 0.5;
        } else {
            rest_factor *= 0.4;
        }


        if(userTotalDriving >= 1 && userTotalRest >= 1) {
            if((userTotalDriving/userTotalRest) <= 3) {
                rest_factor *= 1.4;
            } else if((userTotalDriving/userTotalRest) <= 5) {
                rest_factor *= 1.2;
            } else if((userTotalDriving/userTotalRest) <= 8) {
                rest_factor *= 1.1;
            }  else if((userTotalDriving/userTotalRest) <= 10) {
                //
            } else if((userTotalDriving/userTotalRest) <= 13) {
                rest_factor *= 0.9;
            } else if((userTotalDriving/userTotalRest) <= 15) {
                rest_factor *= 0.8;
            } else if((userTotalDriving/userTotalRest) <= 17) {
                rest_factor *= 0.7;
            } else if((userTotalDriving/userTotalRest) <= 19) {
                rest_factor *= 0.6;
            } else {
                rest_factor *= 0.5;
            }
        }

        if(rest_factor <= 0) {
            rest_factor = 0;
        }

        if(rest_factor >= 100) {
            rest_factor = 100;
        }

        return rest_factor;
    }

    public double trafficCalculator(double userTraffic) {

        double traffic_factor = 0;

        return traffic_factor;
    }

    public double roadTypeCalculator(RoadInformation.HighwayTags highwayTags, double userRoadLanes, boolean oneWay) {

        double roadType_factor = 0;
        if(highwayTags == RoadInformation.HighwayTags.motorway) {
            roadType_factor = 100;
        } else if(highwayTags == RoadInformation.HighwayTags.trunk) {
            roadType_factor = 100;
        } else if(highwayTags == RoadInformation.HighwayTags.primary) {
            roadType_factor = 90;
        } else if(highwayTags == RoadInformation.HighwayTags.secondary) {
            roadType_factor = 75;
        } else if(highwayTags == RoadInformation.HighwayTags.tertiary) {
            roadType_factor = 50;
        } else if(highwayTags == RoadInformation.HighwayTags.unclassified) {
            roadType_factor = 30;
        } else if(highwayTags == RoadInformation.HighwayTags.residential) {
            roadType_factor = 60;
        } else if(highwayTags == RoadInformation.HighwayTags.motorway_link) {
            roadType_factor = 90;
        } else if(highwayTags == RoadInformation.HighwayTags.trunk_link) {
            roadType_factor = 90;
        } else if(highwayTags == RoadInformation.HighwayTags.primary_link) {
            roadType_factor = 80;
        } else if(highwayTags == RoadInformation.HighwayTags.secondary_link) {
            roadType_factor = 65;
        } else if(highwayTags == RoadInformation.HighwayTags.tertiary_link) {
            roadType_factor = 40;
        } else if(highwayTags == RoadInformation.HighwayTags.road) {
            roadType_factor = 30;
        }

        if(!oneWay) {
            roadType_factor *= 0.8;
        }

        double roadTypeCoefficient = 1;
        if(userRoadLanes >= 5) {
           //
        } else if(userRoadLanes >= 4) {
            roadTypeCoefficient = 0.98;
        } else if(userRoadLanes >= 3) {
            roadTypeCoefficient = 0.9;
        } else if(userRoadLanes >= 2) {
            roadTypeCoefficient = 0.85;
        } else {
            roadTypeCoefficient = 0.75;
        }
        roadType_factor *= roadTypeCoefficient;

        return roadType_factor;
    }

    double weather_factor = 0;
    Weather.WeatherType weatherType;
    public void setWeather_factor() {
        this.weather_factor = weather_factor;
        Thread weatherThread = new Thread() {
            @Override
            public void run() {
                try {
                    Weather weather = Weather.getCurrentLocationWeather(context.getApplicationContext());
                    weather_factor = weatherCalculator(weather.getTemperature(),weather.getWindSpeed(),weather.getWeatherType()) * 2;
                    weatherType = weather.getWeatherType();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        weatherThread.start();
    }

    public double calculatePercentageAlgorithm() {

        Log.d("vib", "" + vibration);
        //Morteza for location
        GPSTracker gps = new GPSTracker(context.getApplicationContext());
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longtitude = gps.getLongtitude();
            city = gps.getNearestCIty();
            distance = gps.getNearestDistance();
        }
        else {
            gps.ShowGPSAlertDialog();
        }
        //end Morteza for location

        //Morteza calling roadInformation class
        RoadInformation roadInformation = new RoadInformation();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String xml = roadInformation.getXmlFromUrl(latitude, latitude + 0.0002, longtitude, longtitude + 0.0002);
                speedLimit = roadInformation.GetSpeedLimit(xml);
                lanes = roadInformation.GetLanes(xml);
                oneway = roadInformation.IsOneway(xml);
                highwayType = roadInformation.RoadType(xml);
            }
        });
        thread.start();
        //Morteza calling roadInformation class

        setWeather_factor();
        Log.d("acceleration is", "" + acceleration);
        double sleep_factor = 300; //sleepCalculator() * 3;
        double time_factor = timeCalculator(timeObj.getTimeHOUR(), timeObj.getTimeMINUTE(), 0, 0) * 3;
        double speed_factor = speedCalculator(staticUserSpeed, speedLimit, weatherType) * 3;
        double withoutStopDriving_factor = withoutStopDrivingCalculator(nonStop, totalDrive, totalTime, timeObj.getTimeHOUR(), timeObj.getTimeMINUTE()) * 3;
        double nearCities_factor = nearCitiesCalculator(distance) * 2;
        double vibration_factor = vibrationCalculator(vibration) * 2;
        double acceleration_factor = accelerationCalculator(acceleration, weatherType) * 2.5;
        double month_factor = monthCalculator(solarCalendar.month) * 0.8;
        double traffic_factor = 0; // trafficCalculator() * 1;
        double roadType_factor = roadTypeCalculator(highwayType, lanes, oneway) * 1;
        Log.d("drive status", "sleep: " + sleep_factor + " + time: " + time_factor +
        "+ speed: " + speed_factor + "+ wihtoutstop: " + withoutStopDriving_factor + " + nearcity: " + nearCities_factor + "+ vibration: "
                        + vibration_factor +  " + wheather" + weather_factor + " + accelration: " + acceleration_factor + "+ month: " + month_factor + " + roadtype: " + roadType_factor);
        sleepAlert(sleep_factor/3);
        speedAlert(speed_factor/3);
        timeAlert(time_factor/3);
        withoutStopAlert(withoutStopDriving_factor/3);
        weatherAlert(weather_factor);
        nearCitiesAlert(nearCities_factor/2);
        vibrationAlert(vibration_factor/2);
        accelerationAlert(acceleration_factor/2.5);
        monthAlert(month_factor/0.8);
        trafficAlert(traffic_factor);
        roadTypeAlert(roadType_factor);

        double average = 0;
        
        double sleep_raw = 0; //EncodeDecode.sleepEncode();
        double speed_raw = EncodeDecode.speedEncode(staticUserSpeed);
        double time_raw = EncodeDecode.timeEncode(timeObj.getTimeHOUR(), timeObj.getTimeMINUTE());
        double acceleration_raw = EncodeDecode.accelerationEncode(acceleration);
        double withoutStop_raw = EncodeDecode.withoutStopEncode(nonStop);
        double weather_raw = EncodeDecode.weatherEncode(weatherType);
        double nearCities_raw = EncodeDecode.nearCitiesEncode(distance);
        double vibration_raw = EncodeDecode.vibrationEncode(vibration);
        double month_raw = EncodeDecode.monthEncode(solarCalendar.month);
        double traffic_raw = 0;
        double roadType_raw = EncodeDecode.roadTypeEncode(highwayType);

        average = (sleep_factor + time_factor
                + speed_factor + withoutStopDriving_factor
                + weather_factor + nearCities_factor
                + vibration_factor + acceleration_factor
                + month_factor + traffic_factor + roadType_factor) / 21.3;

        if(cycle == 10) {
            double sleep_save = calculateAverage(sleep_data);
            double speed_save = calculateAverage(speed_data);
            double time_save = calculateAverage(time_data);
            double withoutStop_save = calculateAverage(withoutStop_data);
            double weather_save = calculateAverage(weather_data);
            double nearCities_save = calculateAverage(nearCities_data);
            double vibration_save = calculateAverage(vibration_data);
            double acceleration_save = calculateAverage(acceleration_data);
            double month_save = calculateAverage(month_data);
            double traffic_save = calculateAverage(traffic_data);
            double roadType_save = calculateAverage(roadType_data);

            // Should save data
            DataSender sender = new DataSender();
            sender.execute(
                    SignIn.getToken(),Double.toString(sleep_save),Double.toString(time_save),Double.toString(speed_save)
                    ,Double.toString(withoutStop_save),Double.toString(roadType_save),Double.toString(traffic_save),Double.toString(weather_save),Double.toString(nearCities_save)
                    ,Double.toString(vibration_save),Double.toString(acceleration_save),Double.toString(month_save),Double.toString(average)
            );

            sleep_data.clear();
            speed_data.clear();
            time_data.clear();
            withoutStop_data.clear();
            weather_data.clear();
            nearCities_data.clear();
            vibration_data.clear();
            acceleration_data.clear();
            month_data.clear();
            traffic_data.clear();
            roadType_data.clear();
            cycle = 0;
        }
        cycle ++;
        sleep_data.add(sleep_raw);
        speed_data.add(speed_raw);
        time_data.add(time_raw);
        withoutStop_data.add(withoutStop_raw);
        weather_data.add(weather_raw);
        nearCities_data.add(nearCities_raw);
        vibration_data.add(vibration_raw);
        acceleration_data.add(acceleration_raw);
        month_data.add(month_raw);
        traffic_data.add(traffic_raw);
        roadType_data.add(roadType_raw);

        return average;
    }

    public void restNeeded(double percentage) {

    }

    public void nearestRestComplex(double dist, String restComplexName) {
        // Morteza
    }

    public boolean checkIfStopped() {
        //Morteza
        return false;
    }

    public void theTotalRest(double restTime) {
        // Morteza
    }

    private double calculateAverage(ArrayList<Double> array) {
        double ave = 0;
        for (Double item : array) {
            ave += item;
        }
        ave /= array.size();
        return ave;
    }

    public String calculateStatusAlgorithm(double percentage) {
        String status = "";
        if(percentage >= 90) {
            status = "بسیار ایمن";
        } else if(percentage >= 70) {
            status = "ایمن";
        } else if(percentage >= 55) {
            status = "نیازمند دقت";
        } else if(percentage >= 48) {
            status = "نیازمند دقت بالا";
        } else if(percentage >= 40) {
            status = "ناایمن";
        } else if(percentage >= 30) {
            status = "ایمنی بسیار پایین";
        } else {
            status = "شرایط نامناسب رانندگی";
        }

        return status;
    }

    public int calculateBackgroundAlgorithm(double percentage) {
        int background = 0;
        if(percentage >= 90) {
            background = 1;
        } else if(percentage >= 70) {
            background = 2;
        } else if(percentage >= 55) {
            background = 3;
        } else if(percentage >= 48) {
            background = 4;
        } else if(percentage >= 40) {
            background = 5;
        } else if(percentage >= 30) {
            background = 6;
        } else {
            background = 7;
        }

        return background;
    }

    public void AlertAlgorithm() {

    }

    public String sleepAlert(double sleep_factor) {
        String sleep_alert = "";
        if(sleep_factor <= 30) {
            sleep_alert = "شرایط خواب شما برای رانندگی اصلا مناسب نیست!";
            DriveAlertHandler.sleep_func(sleep_alert, 4, true, DriveAlertHandler.Type.SLEEP);
        } else if(sleep_factor <= 50) {
            sleep_alert = "خواب شما به میزان کافی نبوده است.";
            DriveAlertHandler.sleep_func(sleep_alert, 2, false, DriveAlertHandler.Type.SLEEP);
        } else if(sleep_factor <= 65) {
            sleep_alert = "بهتر است در اولین فرصت کمی استراحت کنید.";
            DriveAlertHandler.sleep_func(sleep_alert, 1, false, DriveAlertHandler.Type.SLEEP);
        }
        return sleep_alert;
    }

    public String speedAlert(double speed_factor) {

        String speed_alert = "";
        if(speed_factor <= 30) {
            speed_alert = "سرعت شما به شدت خطرناک است!";
            DriveAlertHandler.speed_func(speed_alert, 4, true, DriveAlertHandler.Type.SPEED);
        } else if(speed_factor <= 50) {
            speed_alert = "سرعت شما بسیار زیاد است.";
            DriveAlertHandler.speed_func(speed_alert, 2, false, DriveAlertHandler.Type.SPEED);
        } else if(speed_factor <= 65) {
            speed_alert = "لطفا کمی از سرعت خود بکاهید.";
            DriveAlertHandler.speed_func(speed_alert, 1, false, DriveAlertHandler.Type.SPEED);
        }
        return speed_alert;
    }

    public String accelerationAlert(double acceleration_factor) {

        String acceleration_alert = "";
        if(acceleration_factor <= 40) {
            acceleration_alert = "شتاب خودروی شما خطرناک است!";
            DriveAlertHandler.acceleration_func(acceleration_alert, 4, true, DriveAlertHandler.Type.ACCELERATION);
        } else if(acceleration_factor <= 60) {
            acceleration_alert = "شتاب خودرویتان را متعادل تر کنید.";
            DriveAlertHandler.acceleration_func(acceleration_alert, 2, false, DriveAlertHandler.Type.ACCELERATION);
        } else if(acceleration_factor <= 72) {
            acceleration_alert = "کمی در افزایش و کاهش سرعت دقت کنید.";
            DriveAlertHandler.acceleration_func(acceleration_alert, 1, false, DriveAlertHandler.Type.ACCELERATION);
        }
        return acceleration_alert;
    }

    public String vibrationAlert(double vibration_factor) {

        String vibration_alert = "";
        if(vibration_factor <= 42) {
            vibration_alert = "لرزش خودروی شما به شدت زیاد است!";
            DriveAlertHandler.vibration_func(vibration_alert, 4, true, DriveAlertHandler.Type.VIBRATION);
        } else if(vibration_factor <= 55) {
            vibration_alert = "لرزش خودروی شما بیش از حد است؛ دقت کنید.";
            DriveAlertHandler.vibration_func(vibration_alert, 2, false, DriveAlertHandler.Type.VIBRATION);
        } else if(vibration_factor <= 65) {
            vibration_alert = "لرزش خودروی شما زیاد است؛ کمی دقت کنید.";
            DriveAlertHandler.vibration_func(vibration_alert, 1, false, DriveAlertHandler.Type.VIBRATION);
        }
        return vibration_alert;
    }

    public String timeAlert(double time_factor) {

        String time_alert = "";
        if(time_factor <= 40) {
            time_alert = "ساعت اوج تصادف؛ بسیار مراقب باشید!";
            DriveAlertHandler.time_func(time_alert, 4, true, DriveAlertHandler.Type.TIME);
        } else if(time_factor <= 55) {
            time_alert = "این ساعت خطرناک است؛ با دقت ادامه دهید.";
            DriveAlertHandler.time_func(time_alert, 2, false, DriveAlertHandler.Type.TIME);
        } else if(time_factor <= 68) {
            time_alert = "این زمان رانندگی پرخطر است؛ کمی بیشتر دقت کنید.";
            DriveAlertHandler.time_func(time_alert, 1, false, DriveAlertHandler.Type.TIME);
        }
        return time_alert;
    }

    public String nearCitiesAlert(double nearCities_factor) {

        String nearCities_alert = "";
        if(nearCities_factor <= 42) {
            nearCities_alert = "منطقه ی بسیار حادثه خیز؛ با هوشیاری رانندگی کنید!";
            DriveAlertHandler.nearCities_func(nearCities_alert, 4, true, DriveAlertHandler.Type.NEAR_CITIES);
        }else if(nearCities_factor <= 55) {
            nearCities_alert = "منطقه ی پرتصادف؛ با دقت بیشتری رانندگی کنید.";
            DriveAlertHandler.nearCities_func(nearCities_alert, 2, false, DriveAlertHandler.Type.NEAR_CITIES);
        } else if(nearCities_factor <= 70) {
            nearCities_alert = "منطقه ی حادثه خیز؛ دقت کنید.";
            DriveAlertHandler.nearCities_func(nearCities_alert, 1, false, DriveAlertHandler.Type.NEAR_CITIES);
        }
        return nearCities_alert;
    }

    public String monthAlert(double month_factor) {

        String month_alert = "";
        if(month_factor <= 58) {
            month_alert = "زمان اوج تصادف؛ بیشتر مراقب باشید!";
            DriveAlertHandler.month_func(month_alert, 4, true, DriveAlertHandler.Type.MONTH);
        } else if(month_factor <= 66) {
            month_alert = "زمان حادثه خیز؛ در رانندگی دقت بیشتری کنید.";
            DriveAlertHandler.month_func(month_alert, 2, false, DriveAlertHandler.Type.MONTH);
        } else if(month_factor <= 75) {
            month_alert = "زمان حادثه خیز؛ مراقب باشید.";
            DriveAlertHandler.month_func(month_alert, 1, false, DriveAlertHandler.Type.MONTH);
        }
        return month_alert;
    }

    public String weatherAlert(double weather_factor) {

        String weather_alert = "";
        if(weather_factor <= 42) {
            weather_alert = "هوا بسیار بد است؛ بسیار مراقب باشید!";
            DriveAlertHandler.weather_func(weather_alert, 4, true, DriveAlertHandler.Type.WEATHER);
        } else if(weather_factor <= 55) {
            weather_alert = "آب و هوا بد است؛ در رانندگی بیشتر دقت کنید.";
            DriveAlertHandler.weather_func(weather_alert, 2, false, DriveAlertHandler.Type.WEATHER);
        } else if(weather_factor <= 68) {
            weather_alert = "آب و هوا کمی نامناسب است؛ مراقب باشید.";
            DriveAlertHandler.weather_func(weather_alert, 1, false, DriveAlertHandler.Type.WEATHER);
        }
        return weather_alert;
    }

    public String withoutStopAlert(double withoutStop_factor) {

        String withoutStop_alert = "";
        if(withoutStop_factor <= 30) {
            withoutStop_alert = "زمان بسیار طولانی رانندگی کرده اید؛ استراحت کنید!";
            DriveAlertHandler.withoutStop_func(withoutStop_alert, 4, true, DriveAlertHandler.Type.WITHOUT_STOP);
        } else if(withoutStop_factor <= 50) {
            withoutStop_alert = "زمان طولانی رانندگی کرده اید؛ لطفا کمی استراحت کنید.";
            DriveAlertHandler.withoutStop_func(withoutStop_alert, 2, false, DriveAlertHandler.Type.WITHOUT_STOP);
        } else if(withoutStop_factor <= 65) {
            withoutStop_alert = "مدت زمان زیادی رانندگی کرده اید؛ کمی استراحت کنید.";
            DriveAlertHandler.withoutStop_func(withoutStop_alert, 1, false, DriveAlertHandler.Type.WITHOUT_STOP);
        }
        return withoutStop_alert;
    }

    public String trafficAlert(double traffic_factor) {

        String traffic_alert = "";
        if(traffic_factor <= 30) {
            traffic_alert = "ترافیک خیلی زیاد است؛ با دقت ادامه دهید!";
            DriveAlertHandler.traffic_func(traffic_alert, 4, true, DriveAlertHandler.Type.TRAFFIC);
        } else if(traffic_factor <= 50) {
            traffic_alert = "ترافیک مسیر سنگین است؛ بسیار دقت کنید.";
            DriveAlertHandler.traffic_func(traffic_alert, 2, false, DriveAlertHandler.Type.TRAFFIC);
        } else if(traffic_factor <= 65) {
            traffic_alert = "مسیر ترافیک کمی دارد؛ لطفا دقت کنید.";
            DriveAlertHandler.traffic_func(traffic_alert, 1, false, DriveAlertHandler.Type.TRAFFIC);
        }
        return traffic_alert;
    }

    public String roadTypeAlert(double roadType_factor) {

        String roadType_alert = "";
        if(roadType_factor <= 40) {
            roadType_alert = "جاده بسیار خطرناک است؛ بسیار دقت کنید!";
            DriveAlertHandler.roadType_func(roadType_alert, 4, true, DriveAlertHandler.Type.ROAD_TYPE);
        } else if(roadType_factor <= 50) {
            roadType_alert = "جاده بسیار نامناسب است؛ با دقت بیشتری ادامه دهید.";
            DriveAlertHandler.roadType_func(roadType_alert, 2, false, DriveAlertHandler.Type.ROAD_TYPE);
        } else if(roadType_factor <= 65) {
            roadType_alert = "جاده مناسب نیست؛ دقت کنید.";
            DriveAlertHandler.roadType_func(roadType_alert, 1, false, DriveAlertHandler.Type.ROAD_TYPE);
        }
        return roadType_alert;
    }

}
