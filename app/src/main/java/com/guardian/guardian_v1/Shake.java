package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Shake extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelometerSensor;
    private boolean isAccelometerSensorAvailible, several = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ, xDifference, yDifference, zDifference;
    private float shakeThrehold = 5f;
    public enum shakeSituation { noShake, lowShake, mediumShake, highShake, veryHighShake}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelometerSensorAvailible = true;
        }
        else {
            Log.d("xAccelometer", "Accelometer is not availible");
            isAccelometerSensorAvailible = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("xAccelometer", sensorEvent.values[0]+ "m/s2");
        Log.d("yAccelometer", sensorEvent.values[1]+ "m/s2");
        Log.d("zAccelometer", sensorEvent.values[2]+ "m/s2");

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(several) {

            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            if((xDifference > 7f && yDifference > 7f)
                    || (xDifference > 7f && zDifference > 7f)
                    || (yDifference > 7f && zDifference > 7f)) {
                Log.d("shake situation", shakeSituation.veryHighShake.toString());
            }
            else if ((xDifference > 6f && yDifference > 6f)
                    || (xDifference > 6f && zDifference > 6f)
                    || (yDifference > 6f && zDifference > 6f)) {
                Log.d("shake situation", shakeSituation.highShake.toString());
            }
            else if ((xDifference > 5f && yDifference > 5f)
                    || (xDifference > 5f && zDifference > 5f)
                    || (yDifference > 5f && zDifference > 5f)) {
                Log.d("shake situation", shakeSituation.mediumShake.toString());
            }
            else if ((xDifference > 4f && yDifference > 4f)
                    || (xDifference > 4f && zDifference > 4f)
                    || (yDifference > 4f && zDifference > 4f)) {
                Log.d("shake situation", shakeSituation.lowShake.toString());
            }
            else {
                Log.d("shake situation", shakeSituation.noShake.toString());
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        several = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isAccelometerSensorAvailible) {
            sensorManager.registerListener(this, accelometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isAccelometerSensorAvailible) {
            sensorManager.unregisterListener(this);
        }
    }

    public float getX() {
        return xDifference;
    }

    public float getY() {
        return yDifference;
    }

    public float getZ() {
        return zDifference;
    }
}
