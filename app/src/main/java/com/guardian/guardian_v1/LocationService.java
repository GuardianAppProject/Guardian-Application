package com.guardian.guardian_v1;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

    public class LocationService extends Service implements
            LocationListener,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

        ArrayList<Double> speedArray = new ArrayList<>();
        ArrayList<Long> timeArray = new ArrayList<>();
        private static final long INTERVAL = 1000 * 2;
        private static final long FASTEST_INTERVAL = 1000 * 1;
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        Location mCurrentLocation, lStart, lEnd;
        static double distance = 0;
        double speed;
        int counter;
        private boolean first = true;
        private long firstTime;
        private long stopTime = 0;
        private double minspeed , maxspeed;
        private boolean firstArray = true;
        private final IBinder mBinder = new com.guardian.guardian_v1.LocationService.LocalBinder();
        long endingTime;
        boolean firstSpeedTime = true;
        long saveSpeedTime;

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            return mBinder;
        }

        @SuppressLint("RestrictedApi")
        protected void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public void onConnected(Bundle bundle) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } catch (SecurityException e) {
            }
        }


        protected void stopLocationUpdates() {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            distance = 0;
        }


        @Override
        public void onConnectionSuspended(int i) {

        }


        @Override
        public void onLocationChanged(Location location) {
            MainActivitySpeedometer.locate.dismiss();
            mCurrentLocation = location;
            if (lStart == null) {
                lStart = mCurrentLocation;
                lEnd = mCurrentLocation;
            } else
                lEnd = mCurrentLocation;

            //Calling the method below updates the  live values of distance and speed to the TextViews.
            updateUI();
            //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph
            speed = location.getSpeed() * 18 / 5;

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        public class LocalBinder extends Binder {

            public com.guardian.guardian_v1.LocationService getService() {
                return com.guardian.guardian_v1.LocationService.this;
            }


        }

        //The live feed of Distance and Speed are being set in the method below .
        private double updateUI() {
            if (MainActivitySpeedometer.p == 0) {
                distance = distance + (lStart.distanceTo(lEnd) / 1000.00);
                MainActivitySpeedometer.endTime = System.currentTimeMillis();
                endingTime = TimeUnit.MILLISECONDS.toMinutes(MainActivitySpeedometer.endTime);
                long diff = MainActivitySpeedometer.endTime - MainActivitySpeedometer.startTime;
                diff = TimeUnit.MILLISECONDS.toMinutes(diff);
                MainActivitySpeedometer.time.setText("Total Time: " + diff + " minutes");
                if (speed >= 0.0) {
                    MainActivitySpeedometer.speed.setText("Current speed: " + new DecimalFormat("#.##").format(speed) + " km/hr");

                    if (speed <= 7.0 ) {

                        if (first) {
                            firstTime = System.currentTimeMillis();
                            firstTime = TimeUnit.MILLISECONDS.toMinutes(firstTime);
                            MainActivitySpeedometer.startStopTime.setText(String.valueOf(firstTime));
                            first = false;
                        }

                    }
                    else {
                        first = true;

                        if(firstSpeedTime) {
                            saveSpeedTime = endingTime;
                            stopTime = endingTime - firstTime;
                            timeArray.add(stopTime);
                            firstSpeedTime = false;
                        }
                    }

                    if (speed <= 7.0 && (endingTime - firstTime) >= 5) {
                        MainActivitySpeedometer.stoptxt.setText("you have been stopped");
                        firstSpeedTime = true;
                    }
                    else {
                        MainActivitySpeedometer.stoptxt.setText("You are driving now");
                    }

                } else {
                    MainActivitySpeedometer.speed.setText(".......");
                }
                MainActivitySpeedometer.dist.setText(new DecimalFormat("#.###").format(distance) + " Km's.");

                lStart = lEnd;

                speedArray.add(counter , speed);
                if (firstArray) {
                    minspeed = 0;
                    maxspeed = 0;
                    first = false;
                }
                if (minspeed > speedArray.get(counter)) {
                    minspeed = speedArray.get(counter);
                }
                if(maxspeed < speedArray.get(counter)) {
                    maxspeed = speedArray.get(counter);
                }
                if(speedArray.size() == 16) {
                    speedArray.remove(0);
                }

            }
            return speed;
        }


        @Override
        public boolean onUnbind(Intent intent) {
            stopLocationUpdates();
            if (mGoogleApiClient.isConnected())
                mGoogleApiClient.disconnect();
            lStart = null;
            lEnd = null;
            distance = 0;
            return super.onUnbind(intent);
        }

        public double returnSeedTolarance() {
            double speedTolarance = maxspeed - minspeed;
            return speedTolarance;
        }

        public long returnStopTime() {
            return stopTime;
        }

    }

