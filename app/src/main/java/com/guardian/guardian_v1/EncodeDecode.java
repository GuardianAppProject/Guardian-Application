package com.guardian.guardian_v1;

import com.guardian.guardian_v1.DriveStatus.RoadInformation;
import com.guardian.guardian_v1.DriveStatus.Shake;
import com.guardian.guardian_v1.DriveStatus.Weather;

public class EncodeDecode {

    public static double sleepEncode(double totalSleep) {
        return totalSleep;
    }

    public static String sleepDecode(double input) {
        int hour = (int)Math.round(input / 60);
        int minute = (int)Math.round(input % 60);

        return String.format("%dH:%dM",hour, minute);
    }

    public static double speedEncode(double userSpeed) {
        return userSpeed;
    }

    public static String speedDecode(double input) {

        int speed = (int)Math.round(input);
        return String.format("%d (Km/H)", speed);
    }

    public static double accelerationEncode(double userAcceleration) {
        return userAcceleration;
    }

    public static String accelerationDecode(double input) {

        return String.format("%.2f (m2/s)", input);
    }

    public static double vibrationEncode(Shake.ShakeSituation userShake) {
        int vibration = 0;
        if(userShake == Shake.ShakeSituation.noShake) {
            vibration = 0;
        } else if(userShake == Shake.ShakeSituation.lowShake) {
            vibration = 1;
        } else if(userShake == Shake.ShakeSituation.mediumShake) {
            vibration = 2;
        } else if(userShake == Shake.ShakeSituation.highShake) {
            vibration = 3;
        } else if(userShake == Shake.ShakeSituation.veryHighShake) {
            vibration = 4;
        }
        return vibration;
    }

    public static String vibrationDecode(double input) {

        input = (int)Math.round(input);
        String vibrationOutput = "بدون لرزش";
        if(input == 0) {
            vibrationOutput = "بدون لرزش";
        } else if(input == 1) {
            vibrationOutput = "لرزش کم";
        } else if(input == 2) {
            vibrationOutput = "لرزش متوسط";
        } else if(input == 3) {
            vibrationOutput = "لرزش زیاد";
        } else if(input == 4) {
            vibrationOutput = "لرزش بسیار زیاد";
        }
        return String.format("%s", vibrationOutput);
    }

    public static double timeEncode(double userHour, double userMinute) {
        double time = (userHour * 60) + userMinute;
        return time;
    }

    public static String timeDecode(double input) {
        int hour = (int)Math.round(input / 60);
        int start = hour - 1;
        int end = hour + 1;
        return String.format("%d:00 - %d:00", start, end);
    }

    public static double nearCitiesEncode(double userDistance) {

        double nearCities_factor = 1;
        if(userDistance <= 30) {
            nearCities_factor = 10;
        } else {
            nearCities_factor = 2.5;
        }
        return nearCities_factor;
    }

    public static String nearCitiesDecode(double input) {

        String nearCitiesOutput = "ایمن";
        if(input <= 3) {
            nearCitiesOutput = "بسیار ایمن";
        } else if(input <= 5) {
            nearCitiesOutput = "ایمن";
        } else {
            nearCitiesOutput = "ناایمن";
        }
        return String.format("%s", nearCitiesOutput);
    }

    public static double monthEncode(double userMonth) {

        return userMonth;
    }

    public static String monthDecode(double input) {

        input = Math.round(input);
        String monthOutput = "";
        if(input == 1) {
            monthOutput = "فروردین";
        } else if(input == 2) {
            monthOutput = "اردیبهشت";
        } else if(input == 3) {
            monthOutput = "خرداد";
        } else if(input == 4) {
            monthOutput = "تیر";
        } else if(input == 5) {
            monthOutput = "مرداد";
        } else if(input == 6) {
            monthOutput = "شهریور";
        } else if(input == 7) {
            monthOutput = "مهر";
        } else if(input == 8) {
            monthOutput = "آبان";
        } else if(input == 9) {
            monthOutput = "آذر";
        } else if(input == 10) {
            monthOutput = "دی";
        } else if(input == 11) {
            monthOutput = "بهمن";
        } else if(input == 12) {
            monthOutput = "اسفند";
        }
        return String.format("%s", monthOutput);
    }

    public static double weatherEncode(Weather.WeatherType weatherType) {

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
        return weather_factor;
    }

    public static String weatherDecode(double input) {

        String weather = "مناسب";
        if (input >= 92) {
            weather = "بسیار مناسب";
        } else if (input >= 85) {
            weather = "تقریبا مناسب";
        } else if (input >= 75) {
            weather = "مناسب";
        } else if (input >= 65) {
            weather = "نامناسب";
        } else if (input >= 55) {
            weather = "بسیار نامناسب";
        } else {
            weather = "خطرناک";
        }
        return String.format("%s", weather);
    }

    public static double withoutStopEncode(double userWithoutStop) {
        return userWithoutStop;
    }

    public static String withoutStopDecode(double input) {
        int hour = (int)Math.round(input / 60);
        int minute = (int)Math.round(input % 60);

        return String.format("%dH:%dM",hour, minute);
    }

//    public static double trafficEncode(double traffic) {
//        return ;
//    }
//
//    public static String trafficDecode(double input) {
//        return ;
//    }

    public static double roadTypeEncode(RoadInformation.HighwayTags roadType) {

        int roadType_factor = 100;
        if(roadType == RoadInformation.HighwayTags.motorway) {
            roadType_factor = 100;
        } else if(roadType == RoadInformation.HighwayTags.trunk) {
            roadType_factor = 100;
        } else if(roadType == RoadInformation.HighwayTags.primary) {
            roadType_factor = 90;
        } else if(roadType == RoadInformation.HighwayTags.secondary) {
            roadType_factor = 75;
        } else if(roadType == RoadInformation.HighwayTags.tertiary) {
            roadType_factor = 50;
        } else if(roadType == RoadInformation.HighwayTags.unclassified) {
            roadType_factor = 30;
        } else if(roadType == RoadInformation.HighwayTags.residential) {
            roadType_factor = 60;
        } else if(roadType == RoadInformation.HighwayTags.motorway_link) {
            roadType_factor = 90;
        } else if(roadType == RoadInformation.HighwayTags.trunk_link) {
            roadType_factor = 90;
        } else if(roadType == RoadInformation.HighwayTags.primary_link) {
            roadType_factor = 80;
        } else if(roadType == RoadInformation.HighwayTags.secondary_link) {
            roadType_factor = 65;
        } else if(roadType == RoadInformation.HighwayTags.tertiary_link) {
            roadType_factor = 40;
        } else if(roadType == RoadInformation.HighwayTags.road) {
            roadType_factor = 30;
        }
        return roadType_factor;
    }

    public static String roadTypeDecode(double input) {

        String roadTypeOutput = "ایمن";
       if(input >= 95) {
           roadTypeOutput = "کاملا ایمن";
       } else if(input >= 90) {
           roadTypeOutput = "تقریبا ایمن";
       } else if(input >= 75) {
           roadTypeOutput = "ایمن";
       } else if(input >= 50) {
           roadTypeOutput = "ناایمن";
       } else if(input >= 35) {
           roadTypeOutput = "بسیار ناایمن";
       } else {
           roadTypeOutput = "خطرناک";
       }

        return String.format("%s", roadTypeOutput);
    }
}
