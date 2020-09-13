package com.guardian.guardian_v1;

import java.util.ArrayList;

public class DriveAlertHandler {

    private String alertMessage;
    private int repetition;
    private FactorEnum factor;
    private boolean isTooImportant;
    private ArrayList<DriveAlertHandler> allAlerts;

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

    enum FactorEnum {
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
        ROAD_TYPE
    }

    // Initialization Block
    {
        allAlerts = new ArrayList<>();
    }


    public DriveAlertHandler(String alertMessage, int repetition, FactorEnum factor, boolean tooImportant) {
        setAlertMessage(alertMessage);
        setRepetition(repetition);
        setFactor(factor);
        setTooImportant(tooImportant);
        allAlerts.add(this);
    }

    private void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    private void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public void setFactor(FactorEnum factor) {
        this.factor = factor;
    }

    public void setTooImportant(boolean tooImportant) {
        isTooImportant = tooImportant;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    private void newCycle(){
        allAlerts.clear();

        if(sleep_restTime > 0) {
            sleep_restTime--;
        }

    }

    private String toShowAlert() {

        String toShowStr = "با دقت به رانندگی ادامه دهید.";

        for (DriveAlertHandler alert : allAlerts) {
            if(alert.isTooImportant) {
                toShowStr = alert.getAlertMessage();
                break;
            }
        }
        return toShowStr;
    }
}
