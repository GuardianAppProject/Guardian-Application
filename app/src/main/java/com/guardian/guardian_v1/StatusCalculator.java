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
            sleepCoefficient = 1;
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
    
}
