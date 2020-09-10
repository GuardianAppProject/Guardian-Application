package com.guardian.guardian_v1;


import com.guardian.guardian_v1.DriveStatus.Shake;
import com.guardian.guardian_v1.DriveStatus.Weather;

public class StatusCalculator {

    private double sleepCalculator(double userSleep, double userAwake) {
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
        return sleep_factor;
    }

    private double speedCalculator(double userSpeed, double speedLimit, Weather.WeatherType weatherType) {

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

        if(speed_factor <= 0) {
            speed_factor = 0;
        }

        if(speedLimit >= 110 && userSpeed >= 15) {
            if(userSpeed <= 30) {
                speed_factor = 70;
            } else if(userSpeed <= 40) {
                speed_factor = 77;
            }
        }
        return speed_factor;
    }

    private double accelerationCalculator(double userAcceleration, Weather.WeatherType weatherType) {

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

        return acceleration_factor;
    }

    private double vibrationCalculator(Shake.ShakeSituation userVibration) {

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

        return vibration_factor;
    }

    private double timeCalculator(double userTimeHOUR, double userTimeMINUTE, String sunrise, String sunset) {

        double userTime = (userTimeHOUR * 60) + userTimeMINUTE;
        double time_factor = 0;

        if(userTime <= 60) {           // 0  -  1

        } else if(userTime <= 120) {   // 1  -  2

        } else if(userTime <= 180) {   // 2  -  3

        } else if(userTime <= 240) {   // 3  -  4

        } else if(userTime <= 300) {   // 4  -  5

        } else if(userTime <= 360) {   // 5  -  6

        } else if(userTime <= 420) {   // 6  -  7

        } else if(userTime <= 480) {   // 7  -  8

        } else if(userTime <= 540) {   // 8  -  9

        } else if(userTime <= 600) {   // 9  -  10

        } else if(userTime <= 660) {   // 10  -  11

        } else if(userTime <= 720) {   // 11  -  12

        }else if(userTime <= 780) {    // 12  -  13

        } else if(userTime <= 840) {   // 13  -  14

        } else if(userTime <= 900) {   // 14  -  15

        } else if(userTime <= 960) {   // 15  -  16

        } else if(userTime <= 1020) {  // 16  -  17

        } else if(userTime <= 1080) {  // 17  -  18

        } else if(userTime <= 1140) {  // 18  -  19

        } else if(userTime <= 1200) {  // 19  -  20

        } else if(userTime <= 1260) {  // 20  -  21

        } else if(userTime <= 1320) {  // 21  -  22

        } else if(userTime <= 1380) {  // 22  -  23

        } else if(userTime <= 1440) {  // 23  -  24

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

        return month_factor;
    }
}
