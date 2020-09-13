package com.guardian.guardian_v1;

import java.util.ArrayList;
import java.util.HashMap;

public class DriveAlertHandler {

    private String alertMessage;
    private int repetition;
    private boolean isTooImportant;
    private Type type;

    private static DriveAlertHandler currentAlert = new DriveAlertHandler("", 1, false, Type.NONE);

    // number of cycles
    private static final int sleep_timeGap = 14;
    private static final int speed_timeGap = 1;
    private static final int acceleration_timeGap = 1;
    private static final int vibration_timeGap = 1;
    private static final int time_timeGap = 14;
    private static final int nearCities_timeGap = 4;
    private static final int month_timeGap = 60;
    private static final int weather_timeGap = 10;
    private static final int withoutStop_timeGap = 10;
    private static final int traffic_timeGap = 5;
    private static final int roadType_timeGap = 10;

    //
    private static int sleep_restTime = 0;
    private static int speed_restTime = 0;
    private static int acceleration_restTime = 0;
    private static int vibration_restTime = 0;
    private static int time_restTime = 0;
    private static int nearCities_restTime = 0;
    private static int month_restTime = 0;
    private static int weather_restTime = 0;
    private static int withoutStop_restTime = 0;
    private static int traffic_restTime = 0;
    private static int roadType_restTime = 0;

    //
    public static DriveAlertHandler sleep_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler speed_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler acceleration_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler vibration_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler time_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler nearCities_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler month_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler weather_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler withoutStop_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler traffic_alert = new DriveAlertHandler("", 1, false, Type.NONE);
    public static DriveAlertHandler roadType_alert = new DriveAlertHandler("", 1, false, Type.NONE);


    public enum Type {
        SLEEP,
        SPEED,
        ACCELERATION,
        VIBRATION,
        TIME,
        NEAR_CITIES,
        MONTH,
        WEATHER,
        WITHOUT_STOP,
        TRAFFIC,
        ROAD_TYPE,
        NONE
    }

    public DriveAlertHandler(String alertMessage, int repetition, boolean tooImportant, Type type) {
        setAlertMessage(alertMessage);
        setRepetition(repetition);
        setTooImportant(tooImportant);
        setType(type);
    }

    private void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    private void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public void setTooImportant(boolean tooImportant) {
        isTooImportant = tooImportant;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public static void passCycle(){
       if(currentAlert.repetition > 0) {
           currentAlert.repetition --;
       }

        if(sleep_restTime > 0) {
             sleep_restTime --;
         }
        if(speed_restTime > 0) {
            speed_restTime --;
        }
        if(acceleration_restTime > 0) {
            acceleration_restTime --;
        }
        if(vibration_restTime > 0) {
            vibration_restTime --;
        }
        if(time_restTime > 0) {
            time_restTime --;
        }
        if(nearCities_restTime > 0) {
            nearCities_restTime --;
        }
        if(month_restTime > 0) {
            month_restTime --;
        }
        if(weather_restTime > 0) {
            weather_restTime --;
        }
        if(withoutStop_restTime > 0) {
            withoutStop_restTime --;
        }
        if(roadType_restTime > 0) {
            roadType_restTime --;
        }
        if(traffic_restTime > 0) {
            traffic_restTime --;
        }
    }

    public static String toShowAlert() {

        String toShowStr = "با دقت به رانندگی ادامه دهید.";


        // zire 30 ha
        if(speed_alert.isTooImportant) {
            toShowStr = speed_alert.getAlertMessage();
            speed_restTime = speed_timeGap;
            currentAlert = sleep_alert;
            return toShowStr;
        } else if(sleep_alert.isTooImportant) {
            toShowStr = sleep_alert.getAlertMessage();
            sleep_restTime = sleep_timeGap;
            currentAlert = speed_alert;
            return toShowStr;
        } else if(withoutStop_alert.isTooImportant) {
            toShowStr = withoutStop_alert.getAlertMessage();
            withoutStop_restTime = withoutStop_timeGap;
            currentAlert = withoutStop_alert;
            return toShowStr;
        } else if(acceleration_alert.isTooImportant) {
            toShowStr = acceleration_alert.getAlertMessage();
            acceleration_restTime = acceleration_timeGap;
            currentAlert = acceleration_alert;
            return toShowStr;
        } else if(vibration_alert.isTooImportant) {
            toShowStr = vibration_alert.getAlertMessage();
            vibration_restTime = vibration_timeGap;
            currentAlert = vibration_alert;
            return toShowStr;
        } else if(time_alert.isTooImportant) {
            toShowStr = time_alert.getAlertMessage();
            time_restTime = time_timeGap;
            currentAlert = time_alert;
            return toShowStr;
        } else if(nearCities_alert.isTooImportant) {
            toShowStr = nearCities_alert.getAlertMessage();
            nearCities_restTime = nearCities_timeGap;
            currentAlert = nearCities_alert;
            return toShowStr;
        }  else if(weather_alert.isTooImportant) {
            toShowStr = weather_alert.getAlertMessage();
            weather_restTime = weather_timeGap;
            currentAlert = weather_alert;
            return toShowStr;
        } else if(roadType_alert.isTooImportant) {
            toShowStr = roadType_alert.getAlertMessage();
            roadType_restTime = roadType_timeGap;
            currentAlert = roadType_alert;
            return toShowStr;
        } else if(month_alert.isTooImportant) {
            toShowStr = month_alert.getAlertMessage();
            month_restTime = month_timeGap;
            currentAlert = month_alert;
            return toShowStr;
        } else if(traffic_alert.isTooImportant) {
            toShowStr = traffic_alert.getAlertMessage();
            traffic_restTime = traffic_timeGap;
            currentAlert = traffic_alert;
            return toShowStr;
        }


        if(currentAlert.repetition > 0) {
            toShowStr = currentAlert.getAlertMessage();
            return toShowStr;
        } else {

            HashMap<DriveAlertHandler, Integer> toShowAlertsList = new HashMap<>();
            if(!speed_alert.getAlertMessage().equalsIgnoreCase("")) {
               toShowAlertsList.put(speed_alert, speed_restTime);
            } else if(!sleep_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(sleep_alert, sleep_restTime);
            } else if(!withoutStop_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(withoutStop_alert, withoutStop_restTime);
            } else if(!acceleration_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(acceleration_alert, acceleration_restTime);
            } else if(!vibration_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(vibration_alert, vibration_restTime);
            } else if(!time_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(time_alert, time_restTime);
            } else if(!nearCities_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(nearCities_alert, nearCities_restTime);
            } else if(!weather_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(weather_alert, weather_restTime);
            } else if(!roadType_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(roadType_alert, roadType_restTime);
            } else if(!month_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(month_alert, month_restTime);
            } else if(!traffic_alert.getAlertMessage().equalsIgnoreCase("")) {
                toShowAlertsList.put(traffic_alert, traffic_restTime);
            }

            int min = -1000;
            for (DriveAlertHandler driveAlertHandler : toShowAlertsList.keySet()) {
                if(toShowAlertsList.get(driveAlertHandler) < min) {
                    currentAlert = driveAlertHandler;
                    min = toShowAlertsList.get(driveAlertHandler);
                }
            }
            if(toShowAlertsList.size()==0) {
                toShowStr = "";
                return toShowStr;
            }

            toShowStr = currentAlert.getAlertMessage();
            if(currentAlert.type == Type.SLEEP) {
                sleep_restTime = sleep_timeGap;
            } else if(currentAlert.type == Type.SPEED) {
                speed_restTime = speed_timeGap;
            } else if(currentAlert.type == Type.ACCELERATION) {
                acceleration_restTime = acceleration_timeGap;
            } else if(currentAlert.type == Type.VIBRATION) {
                vibration_restTime = vibration_timeGap;
            } else if(currentAlert.type == Type.TIME) {
                time_restTime = time_timeGap;
            } else if(currentAlert.type == Type.NEAR_CITIES) {
                nearCities_restTime = nearCities_timeGap;
            } else if(currentAlert.type == Type.MONTH) {
                month_restTime = month_timeGap;
            } else if(currentAlert.type == Type.WEATHER) {
                weather_restTime = weather_timeGap;
            } else if(currentAlert.type == Type.WITHOUT_STOP) {
                withoutStop_restTime = withoutStop_timeGap;
            } else if(currentAlert.type == Type.ROAD_TYPE) {
                roadType_restTime = roadType_timeGap;
            } else if(currentAlert.type == Type.TRAFFIC) {
                traffic_restTime = traffic_timeGap;
            }

            return toShowStr;
        }
    }
}
