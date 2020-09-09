package com.guardian.guardian_v1;

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
}
