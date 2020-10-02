package com.guardian.guardian_v1.DriveStatus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.guardian.guardian_v1.Setting;

public class GPSTracker extends Service implements LocationListener {

    private Context context;
    private LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longtitude;
    int PERMISSION_ID = 44;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private long timeUpdate = 5000;
    private long distanceUpdate = 10;
    private int counter;
    private Location destLocation = new Location("destLocationn");
    private double[] result = new double[31];
    private double min;
    private int cityIndex;
    private String nearestCIty;
    private double nearestDistance;

    String[] cities = {"Tehran", "Mashhad", "Isfahan", "Karaj", "Shiraz", "Tabriz", "Qom", "Ahvaz", "Kermanshah", "Orumiyeh", "Rasht", "Zahedan", "Kerman",
            "Yazd", "Ardabil", "BandarAbbas", "Arak", "Zanjan", "Sanandaj", "Qazvin", "Khorramabad", "Gorgan", "Sari", "Bojnourd", "Bushehr", "Birjand",
            "Ilam", "Shahrekord", "Semnan", "Yasuj", "Hamedan"};
    double[] longtitudes = {51.42151, 59.56796, 51.67462, 50.99155, 52.53113, 46.2919, 50.8764, 48.6842, 47.065, 45.07605, 49.58319, 60.8629, 57.07879,
            54.3675, 48.2933, 56.2808, 49.68916, 48.4787, 46.99883, 50.0041, 48.35583, 54.44361, 53.06009, 57.333332, 50.8166634, 59.22114, 46.4227,
            50.8556632, 53.9191629, 51.58796, 48.5166646};
    double[] latitudes = {35.69439, 36.31559, 32.65246, 35.83226, 29.61031, 38.08, 34.6401, 31.31901, 34.31417, 37.55274, 37.28077, 29.4963, 30.28321,
            31.89722, 38.2498, 27.1865, 34.09174, 36.6736, 35.31495, 36.26877, 33.48778, 36.84165, 36.56332, 37.47166478, 28.9833294, 32.86628, 33.6374,
            32.32333204, 35.23416573, 30.66824, 34.7999968};

    double[] borderLongtitude = {51.092977, 51.316824, 51.595602, 51.471662, 59.437313, 59.608288, 59.720211, 59.621334, 51.565480, 51.573720,
            51.832585, 51.679463, 50.892403, 50.977547, 51.038659, 50.995400, 52.406663, 52.387437, 52055632, 52.632570, 46.181107, 46.165314, 46.236036, 46.496964,
            50.766911, 50.844502, 50.924153, 50.925526, 48.580632, 48.0581662, 48.779759, 48.660626, 47.084191, 47.158349, 47.192681, 47.050546,
            44.978390, 45.060788, 45.141812, 45.018216, 49.500873, 49.631679, 49.643009, 49.664620, 60.787119, 60.827288, 60.965990, 60.821794,
            56.949346, 57.044618, 57.169244, 57.056634, 54.220891, 54.404056, 54.448688, 54.379680, 48.269963, 48.413129, 48.339641, 48.233898,
            56.028460, 56.222094, 56.445254, 56.208362, 49.614663, 49.687190, 49.801431, 49.827866, 48.400180, 48.522060, 48.582141, 48.474681,
            46.963666, 47.014135, 47.059797, 47.008985, 49.950765, 50.043119, 50.081228, 50.006384, 48.251779, 48.300016, 48.443868, 48.306539,
            54.358753, 54.428104, 54.488701, 54.466042, 53.024383, 53.063865, 53.119484, 53.078285, 57.260611, 57.322066, 57.389701, 57.389701,
            57.301124, 50.808392, 50.825558, 50.910359, 50.983830, 59.164337, 59.266991, 59.304242, 59.262013, 46.384138, 46.417527, 46.448082,
            46.430229, 50.787391, 50.905151, 50.938282, 50.837688, 53.331756, 53.364715, 53.424110, 53.436469, 51.515659, 51.525616, 51.573337,
            51.592907, 48.474016, 48.540277, 48.575296, 48.502169};

    double[] borderLatitude = {35.746661, 35.806825, 35.735515, 35.561986, 36.355626, 36.363368, 36.245503, 36.191768, 32.647180, 32.818722,
            32.652383, 32.506577, 35.874979, 35.874979, 35.888331, 35.775602, 35.740498, 29.620146, 29.829445, 29.688172, 29.500095, 38.169952,
            38.041359, 38.032166, 38.010529, 34.590769, 34.812057, 34.663091, 34.598117, 31.359239, 31.359826, 31.383569, 31.252171, 31.887119,
            31.928213, 31.833173, 31.794080, 38.306579, 38.350210, 38.206925, 38.189387, 27.118381, 27.223451, 27.274729, 27.147713, 34.046966,
            34.129422, 34.075124, 34.025628, 36.695041, 36.725867, 36.639138, 36.659245, 35.349903, 35.344302, 35.304806, 35.228841, 36.280369,
            36.326575, 36.238844, 36.228875, 33.441765, 33.571516, 33.443842, 33.406878, 36.841490, 36.868961, 36.848496, 36.794354, 36.549019,
            36.593962, 36.561705, 36.517156, 37.482710, 37.536090, 37.479169, 37.425476, 28.919016, 29.001329, 28.932238, 28.915110, 32.868152,
            32.910243, 32.845656, 32.831233, 33.627415, 33.652568, 33.647780, 33.621554, 32.386400, 32.356098, 32.293578, 32.316792, 35.550300,
            35.616892, 35.590374, 35.567896, 30.721824, 30.746612, 30.702638, 30.625855, 34.830629, 34.880213, 34.793985, 34.744489};


    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() throws SecurityException{
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {
            canGetLocation = true;
            if (isNetworkEnabled) {
                if(checkPermissions()) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distanceUpdate, this);
                    if(locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                        //Log.d("location information 2", "lat : " + latitude + " long : " + getLongtitude());

                    }
                } else {
                    requestPermissions();
                }

            }

            if(checkPermissions()) {
                if(isGPSEnabled && location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distanceUpdate, this);
                    if(locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();

                        //Log.d("location information 3", "lat : " + latitude + " long : " + getLongtitude());
                    }
                }
            } else {
                requestPermissions();
            }

        } else {
            canGetLocation = false;
            Log.d("error", "error");
        }

        measuringDistance();
        return location;
    }

    private void measuringDistance() {
        for (counter = 0; counter < 31; counter++) {

            destLocation.setLatitude(latitudes[counter]);
            destLocation.setLongitude(longtitudes[counter]);
            location.setLongitude(location.getLongitude());
            location.setLatitude(location.getLatitude());
            result[counter] = location.distanceTo(destLocation);
        }
        Log.d("entering measuring", "sdvhkdfhvl");
        min = result[0];
        cityIndex = 0;
        for (counter = 0; counter < 31; counter++) {
            if (result[counter] < min) {
                min = result[counter];
                cityIndex = counter;
            }
        }

        if(result[cityIndex] > 30000) {
            for(counter = (cityIndex * 4); counter < (cityIndex * 4) + 4; counter++) {
                location.setLongitude(location.getLongitude());
                location.setLatitude(location.getLatitude());
                destLocation.setLatitude(borderLatitude[counter]);
                destLocation.setLongitude(borderLongtitude[counter]);
                if(location.distanceTo(destLocation) < result[cityIndex]) {
                    result[cityIndex] = location.distanceTo(destLocation);
                }
            }
        }
        Log.d("cityTextView", "nearest city is " + cities[cityIndex]);
        nearestCIty = cities[cityIndex];
        Log.d("distanceTextView", "your distance from this city is " + result[cityIndex] + "");
        nearestDistance = result[cityIndex];
        if (result[cityIndex] < 30000) {
            Log.d("areaTextView", "you are in area of " + cities[cityIndex]);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongtitude() {
        if(location != null) {
            longtitude = location.getLongitude();
        }
        return longtitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public String getNearestCIty() {
        return nearestCIty;
    }

    public double getNearestDistance() {
        return nearestDistance;
    }

    public void ShowGPSAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GPS").setMessage("GPS is not enabaled. Do you want to make it active?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

