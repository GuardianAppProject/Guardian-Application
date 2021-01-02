package com.guardian.guardian_v1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.guardian.guardian_v1.DriveStatus.LocationService;
import com.guardian.guardian_v1.DriveStatus.Shake;
import com.guardian.guardian_v1.DriveStatus.Speedometer;
import com.guardian.guardian_v1.DriveStatus.Weather;
//import com.mapbox.android.core.location.LocationEngineCallback;
//import com.mapbox.android.core.location.LocationEngineResult;
//import com.mapbox.api.directions.v5.models.BannerText;
//import com.mapbox.api.directions.v5.models.DirectionsResponse;
//import com.mapbox.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.core.constants.Constants;
//import com.mapbox.geojson.LineString;
//import com.mapbox.geojson.Point;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.annotations.MarkerOptions;
//import com.mapbox.mapboxsdk.annotations.Polyline;
//import com.mapbox.mapboxsdk.annotations.PolylineOptions;
//import com.mapbox.mapboxsdk.camera.CameraPosition;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.location.LocationComponent;
//import com.mapbox.mapboxsdk.location.modes.RenderMode;
////import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionLoader;
//import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionView;
//import com.mapbox.services.android.navigation.v5.instruction.Instruction;
//import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
//import com.mapbox.services.android.navigation.v5.milestone.BannerInstructionMilestone;
//import com.mapbox.services.android.navigation.v5.milestone.Milestone;
//import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
//import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
//import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
//import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
//import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
//import com.squareup.picasso.Picasso;

//import org.osmdroid.api.IMapController;
//import org.osmdroid.config.Configuration;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import timber.log.Timber;


//


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Locale;


import java.util.ArrayList;

import java.util.Locale;

//


import java.util.Locale;

public class Main extends FragmentActivity implements SensorEventListener, OnMapReadyCallback, LocationListener {

//    ///
//    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
//    private org.osmdroid.views.MapView map = null;
//    IMapController mapController;
//    static Location location;
//    ImageView loadingGif;
//
//


    //Morteza
    private SensorManager sensorManager;
    private Sensor accelometerSensor;
    private boolean isAccelometerSensorAvailible, several = false;
    public float currentX, currentY, currentZ, lastX, lastY, lastZ, xDifference, yDifference, zDifference;

    public enum ShakeSituation {noShake, lowShake, mediumShake, highShake, veryHighShake}

    public Shake.ShakeSituation situation = Shake.ShakeSituation.noShake;

    //Morteza speedometer
    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    public static long startTime, endTime;
    public static ProgressDialog locate;
    public static int p = 0;

    private static boolean firstTime = true;

    // Map
//    MapView mapView;
//    View contentLayout;
//    InstructionView instructionView;
//
//
//    private Point ORIGIN = Point.fromLngLat(-0.358764, 39.494876);
//    private Point DESTINATION = Point.fromLngLat(-0.383524, 39.497825);
//    private Polyline polyline;
//
//    private final RerouteActivityLocationCallback callback = new RerouteActivityLocationCallback(this);
//    private Location lastLocation;
//    private ReplayRouteLocationEngine mockLocationEngine;
//    private MapboxNavigation navigation;
//    private MapboxMap mapboxMap;
//    private boolean running = false;
//    private boolean tracking;
//    private boolean wasInTunnel = false;

    TextView distanceRem;
    TextView durationRem;
    TextView arrivalTime;
    TextView primaryTxt;
    TextView secondaryTxt;
    TextView weatherTypeTxt;
    ImageView weatherTypeImg;
    Button stopButton;

//    public static String routeStyle = Style.DARK;

    // Menu
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    // Algorithm
    private TextView algorithmPercentageText;
    private TextView algorithmStatusText;
    private ImageView algorithmBackground;
    private TextView alertMessageText;
    private FrameLayout alertMessageBox;
    private ImageView alertMessageImage;
    private StatusCalculator statusCalculator;
    public static TextView speedText;
    public static TextView driveText;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    //Morteza speedometer
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            myService = binder.getService();
            status = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    void bindService() {
        if (status == true)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        status = true;
        startTime = System.currentTimeMillis();
    }

    void unbindService() {
        if (status == false)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        unbindService(sc);
        status = false;
    }
    //end Morteza speedometer


    private GoogleMap mMap;

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//                mMarker = mMap.addMarker(new MarkerOptions().position(loc));
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.NavigationViewLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        String languageToLoad = "fa_IR";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //Morteza shake
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelometerSensorAvailible = true;
        } else {
            Log.d("xAccelometer", "Accelometer is not availible");
            isAccelometerSensorAvailible = false;
        }
        //end Morteza shake

        //Morteza speedometer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        //The method below checks if Location is enabled on device or not. If not, then an alert dialog box appears with option
        //to enable gps.
        checkGps();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            return;
        }


        if (status == false)
            //Here, the Location Service gets bound and the GPS Speedometer gets Active.
            bindService();
        if (firstTime) {
            locate = new ProgressDialog(Main.this);
            locate.setIndeterminate(true);
            locate.setCancelable(false);
            locate.setMessage("Getting Location...");
            locate.show();
            firstTime = false;
        }

        //end Morteza speedometer

//        ORIGIN = Point.fromLngLat(getIntent().getDoubleExtra("originLng", 0), getIntent().getDoubleExtra("originLat", 0));
//        DESTINATION = Point.fromLngLat(getIntent().getDoubleExtra("destinationLng", 0), getIntent().getDoubleExtra("destinationLat", 0));
//
//        mapView = (MapView)findViewById(R.id.mapView);
//        contentLayout = (View) findViewById(android.R.id.content);
//        instructionView = (InstructionView) findViewById(R.id.instructionView);
//
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//
//        MapboxNavigationOptions options = MapboxNavigationOptions.builder().isDebugLoggingEnabled(false).build();
//        navigation = new MapboxNavigation(getApplicationContext(), getResources().getString(R.string.access_token), options);
//        navigation.addNavigationEventListener(this);
//        navigation.addMilestoneEventListener(this);

//        primaryTxt = (TextView) findViewById(R.id.primaryTxt);
//        distanceRem = (TextView) findViewById(R.id.distanceRem);
//        durationRem = (TextView) findViewById(R.id.durationRem);
//        arrivalTime = (TextView) findViewById(R.id.arrivalTime);
//        secondaryTxt = (TextView) findViewById(R.id.secondaryTxt);
//        stopButton = (Button) findViewById(R.id.stopButt);
        weatherTypeImg = findViewById(R.id.WeatherTypeImage);
        weatherTypeTxt = findViewById(R.id.WeatherTypeTextView);
        speedText = findViewById(R.id.speedTextView);
        driveText = findViewById(R.id.driveTextView);

//        instructionView.retrieveSoundButton().hide();
//        instructionView.retrieveSoundButton().addOnClickListener(
//                v -> Toast.makeText(this, "Sound button clicked!", Toast.LENGTH_SHORT).show()
//        );

//        Instruction myInstruction = new Instruction() {
//            @Override
//            public String buildInstruction(RouteProgress routeProgress) {
//                return routeProgress.currentLegProgress().upComingStep().maneuver().instruction();
//            }
//        };

//        stopButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                ViewDialog alert = new ViewDialog();
//                alert.showDialog(Main.this, "آیا مطمئن هستید می خواهید مسیریابی را لغو کنید؟", Main.this);
//            }
//        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // ...From section above...
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        Button button = (Button) findViewById(R.id.menuButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        algorithmPercentageText = findViewById(R.id.driving_percentage);
        algorithmStatusText = findViewById(R.id.driving_status);
        algorithmBackground = findViewById(R.id.driving_background);
        alertMessageText = findViewById(R.id.alertMessageText);
        alertMessageBox = findViewById(R.id.alertMessageBox);
        alertMessageImage = findViewById(R.id.alertMessageImage);
        statusCalculator = new StatusCalculator(this);

        callAlgorithmLogic();
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                callAlgorithmLogic();
                ha.postDelayed(this, 30000);
            }
        }, 30000);


        ///////
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////             TODOne: Consider calling
////                ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 10, mLocationListener);
//
//        //load/initialize the osmdroid configuration, this can be done
//        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
//        Context ctx = getApplicationContext();
//        //Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
////        setContentView(R.layout.activity_main);
//        loadingGif = findViewById(R.id.mapLoad);
//        Glide.with(this).load(R.drawable.loading).into(loadingGif);
//        map = (org.osmdroid.views.MapView) findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//        map.setVisibility(View.INVISIBLE);
//        requestPermissionsIfNecessary(new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        });
//        map.setMultiTouchControls(true);
//        mapController = map.getController();
//        mapController.setZoom(18);


    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });
            }
        }
    }

    //Morteza shake
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("x", sensorEvent.values[0]+ "m/s2");
        Log.d("y", sensorEvent.values[1]+ "m/s2");
        Log.d("z", sensorEvent.values[2]+ "m/s2");

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
                Log.d("shake situation", ShakeSituation.veryHighShake.toString());
                situation = Shake.ShakeSituation.veryHighShake;
            }
            else if ((xDifference > 6f && yDifference > 6f)
                    || (xDifference > 6f && zDifference > 6f)
                    || (yDifference > 6f && zDifference > 6f)) {
                Log.d("shake situation", ShakeSituation.highShake.toString());
                situation = Shake.ShakeSituation.highShake;
            }
            else if ((xDifference > 5f && yDifference > 5f)
                    || (xDifference > 5f && zDifference > 5f)
                    || (yDifference > 5f && zDifference > 5f)) {
                Log.d("shake situation", ShakeSituation.mediumShake.toString());
                situation = Shake.ShakeSituation.mediumShake;
            }
            else if ((xDifference > 4f && yDifference > 4f)
                    || (xDifference > 4f && zDifference > 4f)
                    || (yDifference > 4f && zDifference > 4f)) {
                Log.d("shake situation", ShakeSituation.lowShake.toString());
                situation = Shake.ShakeSituation.lowShake;
            }
            else {
                Log.d("shake situation", ShakeSituation.noShake.toString());
                situation = Shake.ShakeSituation.noShake;
            }

        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        several = true;

        statusCalculator.setVibration(situation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void openDrawer(){
        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
//        Fragment fragment = null;
//        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.account:
                Intent i = new Intent(Main.this, MyAccount.class);
                startActivity(i);
//                finish();
                break;
            case R.id.support:
                Intent i2 = new Intent(Main.this, Support.class);
                startActivity(i2);
//                finish();
                break;
            case R.id.info:
                Intent i3 = new Intent(Main.this, Info.class);
                startActivity(i3);
//                finish();
                break;
            case R.id.settings:
                Intent i4 = new Intent(Main.this, Setting.class);
                startActivity(i4);
//                finish();
                break;
        }

//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent,fragment ).commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);

        // Set action bar title
//        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

    private void setWeatherImage(String url){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(url).into(weatherTypeImg);
            }
        });

    }
    private void callAlgorithmLogic() {
        double percentage = statusCalculator.calculatePercentageAlgorithm();
        algorithmPercentageText.setText(String.valueOf((int)percentage) + "%");
        algorithmStatusText.setText(statusCalculator.calculateStatusAlgorithm(percentage));

        //Log.d("total time", StatusCalculator.totalTime + "");

        ///setting weather type
        Thread weatherThread = new Thread() {
            @Override
            public void run() {
                try {
                    Weather weather = Weather.getCurrentLocationWeather(getApplicationContext());
                    weatherTypeTxt.setText(weather.getWeatherTypePersian());
                    System.out.println(weather.getImageUrl());
                    setWeatherImage(weather.getImageUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        weatherThread.start();
        String toShowAlert = DriveAlertHandler.toShowAlert();
        if(toShowAlert.equalsIgnoreCase("")) {
            alertMessageText.setText("با دقت به رانندگی ادامه دهید.");
            alertMessageBox.setBackgroundResource(R.drawable.rectangle_alert_background_green);
//            alertMessageText.setTextColor(Color.BLACK);
            alertMessageImage.setImageResource(R.drawable.warning_white);
        } else {
            alertMessageText.setText(toShowAlert);
            alertMessageBox.setBackgroundResource(R.drawable.rectangle_alert_background_red);
            alertMessageImage.setImageResource(R.drawable.alert_icon);
        }

        int backgroundNumber = statusCalculator.calculateBackgroundAlgorithm(percentage);
        if(backgroundNumber == 1) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_green);
        } else if(backgroundNumber == 2) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_lightgreen);
        } else if(backgroundNumber == 3) {
            algorithmBackground.setImageResource(R.drawable.circle_gardient_yellow);
        } else if(backgroundNumber == 4) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_orange);
        } else if(backgroundNumber == 5) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_darkorange);
        } else if(backgroundNumber == 6) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_lightred);
        } else if(backgroundNumber == 7) {
            algorithmBackground.setImageResource(R.drawable.circle_gradient_red);
        }

        DriveAlertHandler.passCycle();


    }


    //Morteza speedometer
    //This method leads you to the alert dialog box.
    void checkGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            showGPSDisabledAlertToUser();
        }
    }

    //This method configures the Alert Dialog box.
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1000) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                finish();
//            }
//        }
//    }
//    //end Morteza speedometer
//
//
//    public void changeIntent(){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//
//        //Morteza
//        if(isAccelometerSensorAvailible) {
//            sensorManager.registerListener( this, accelometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//
//        //Morteza
//        if(isAccelometerSensorAvailible) {
//            sensorManager.unregisterListener( this);
//        }
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//        shutdownLocationEngine();
//        shutdownNavigation();
//    }
//
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(@NonNull MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//        this.mapboxMap.addOnMapClickListener(this);
//        mapboxMap.setStyle(routeStyle, style -> {
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//            locationComponent.activateLocationComponent(this, style);
//            locationComponent.setLocationComponentEnabled(true);
//            locationComponent.setRenderMode(RenderMode.GPS);
//
//            mockLocationEngine = new ReplayRouteLocationEngine();
//            getRoute(ORIGIN, DESTINATION);
//        });
//    }
//
//    @Override
//    public boolean onMapClick(@NonNull LatLng point) {
////        if (!running || mapboxMap == null || lastLocation == null) {
////            return false;
////        }
////
////        mapboxMap.addMarker(new MarkerOptions().position(point));
////        mapboxMap.removeOnMapClickListener(this);
////
////        DESTINATION = Point.fromLngLat(point.getLongitude(), point.getLatitude());
////        resetLocationEngine(DESTINATION);
////
////        tracking = false;
//        return false;
//    }
//
//    @Override
//    public void onRunning(boolean running) {
//        this.running = running;
//        if (running) {
//            navigation.addOffRouteListener(this);
//            navigation.addProgressChangeListener(this);
//        }
//    }
//
//    @Override
//    public void userOffRoute(Location location) {
//        ORIGIN = Point.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude());
//        getRoute(ORIGIN, DESTINATION);
//        Snackbar.make(contentLayout, "User Off Route", Snackbar.LENGTH_SHORT).show();
//        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
//    }
//
//    @Override
//    public void onProgressChange(Location location, RouteProgress routeProgress) {
//        boolean isInTunnel = routeProgress.inTunnel();
//        lastLocation = location;
//        if (!wasInTunnel && isInTunnel) {
//            wasInTunnel = true;
//            Snackbar.make(contentLayout, "Enter tunnel!", Snackbar.LENGTH_SHORT).show();
//        }
//        if (wasInTunnel && !isInTunnel) {
//            wasInTunnel = false;
//            Snackbar.make(contentLayout, "Exit tunnel!", Snackbar.LENGTH_SHORT).show();
//        }
//        if (tracking) {
//            mapboxMap.getLocationComponent().forceLocationUpdate(location);
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .zoom(16)
//                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                    .bearing(location.getBearing())
//                    .tilt(25)
//                    .build();
//            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
//        }
//        instructionView.updateDistanceWith(routeProgress);
//
//        navigation.addProgressChangeListener(new ProgressChangeListener() {
//            @Override
//            public void onProgressChange(Location location, RouteProgress routeProgress) {
//                distanceRem.setText(calculateDistance(routeProgress.distanceRemaining()));
//                durationRem.setText(calculateDuration(routeProgress.durationRemaining()));
//                arrivalTime.setText(calculateArrivalTime(routeProgress.durationRemaining()));
//            }
//
////            public void onProgressChange(Location location, RouteStepProgress routeStepProgress) {
////                textView2.setText(String.valueOf(routeStepProgress.distanceRemaining()));
////
////            }
//
//
//        });
//
//
//
//
//    }

    private String calculateDistance(double distance){
        int kiloMeter = (int) (distance/1000);
        int meter = (int) (distance%1000);

        if(kiloMeter<1){
            meter/=100;
            return ((meter*100) + " m");
        } else if(kiloMeter>=20) {
            return (kiloMeter + " km");
        } else {
            meter/=100;
            return (kiloMeter + "." + meter + " km");
        }
    }

    private String calculateDuration(double duration){
        int durationInMin = (int)(duration/60);

        int hourPlus = (int) (durationInMin/60);
        int minPlus = (int) (durationInMin%60);

        String hourStr = String.valueOf(hourPlus);
        String minStr = String.valueOf(minPlus);

        if(hourPlus<1){
            return (minStr + " min");
        } else {
            return (hourStr + " hr " + minStr + " min");
        }
    }

    private String calculateArrivalTime(double duration){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.getTime().getHours();
        int minute = calendar.getTime().getMinutes();
        int durationInMin = (int)(duration/60);

        int hourPlus = (int) (durationInMin/60);
        int minPlus = (int) (durationInMin%60);
        hour += hourPlus;
        minute += minPlus;
        if(minute>=60){
            hour ++;
            minute -= 60;
        }

        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(minute);
        if(hour<10){
            hourStr = "0" + String.valueOf(hour);
        }
        if(minute<10){
            minStr = "0" + String.valueOf(minute);
        }

        return ("(" + hourStr + ":" + minStr + ")");
    }

//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
//        if (milestone instanceof VoiceInstructionMilestone) {
//            Snackbar.make(contentLayout, instruction, Snackbar.LENGTH_SHORT).show();
//        }
//        instructionView.updateBannerInstructionsWith(milestone);
//        Timber.d("onMilestoneEvent - Current Instruction: %s", instruction);
//    }

//    @Override
//    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
//        Timber.d(call.request().url().toString());
//        if (response.body() != null) {
//            if (!response.body().routes().isEmpty()) {
//                DirectionsRoute route = response.body().routes().get(0);
//                drawRoute(route);
//                resetLocationEngine(route);
//                navigation.startNavigation(route);
//                mapboxMap.addOnMapClickListener(this);
//                tracking = true;
//            }
//        }
//    }
//
//    @Override
//    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
//        Timber.e(throwable);
//    }
//
//    void updateLocation(Location location) {
//        if (!tracking) {
//            mapboxMap.getLocationComponent().forceLocationUpdate(location);
//        }
//    }
//
//    private void getRoute(Point origin, Point destination) {
//        NavigationRoute.builder(this)
//                .origin(origin)
//                .destination(destination)
//                .accessToken(Mapbox.getAccessToken())
//                .build().getRoute(this);
//    }
//
//    private void drawRoute(DirectionsRoute route) {
//        List<LatLng> points = new ArrayList<>();
//        List<Point> coords = LineString.fromPolyline(route.geometry(), Constants.PRECISION_6).coordinates();
//
//        for (Point point : coords) {
//            points.add(new LatLng(point.latitude(), point.longitude()));
//        }
//
//        if (!points.isEmpty()) {
//            if (polyline != null) {
//                mapboxMap.removePolyline(polyline);
//            }
//            polyline = mapboxMap.addPolyline(new PolylineOptions()
//                    .addAll(points)
//                    .color(R.color.colorAccent)//Color.parseColor(getString(R.string.blue))
//                    .width(5));
//        }
//    }
//
//    private void resetLocationEngine(Point point) {
//        mockLocationEngine.moveTo(point);
//        navigation.setLocationEngine(mockLocationEngine);
//    }
//
//    private void resetLocationEngine(DirectionsRoute directionsRoute) {
//        mockLocationEngine.assign(directionsRoute);
//        navigation.setLocationEngine(mockLocationEngine);
//    }
//
//    private void shutdownLocationEngine() {
//        if (mockLocationEngine != null) {
//            mockLocationEngine.removeLocationUpdates(callback);
//        }
//    }
//
//    private void shutdownNavigation() {
//        navigation.removeNavigationEventListener(this);
//        navigation.removeProgressChangeListener(this);
//        navigation.onDestroy();
//    }
//
//    @Override
//    public void onInit(int status) {
//
//        if(status==TextToSpeech.SUCCESS)
//        {
////            Toast.makeText(getApplicationContext(), "engine installed",Toast.LENGTH_SHORT).show();
//        }
//        if(status==TextToSpeech.ERROR)
//        {
////            Toast.makeText(getApplicationContext(), "engine not installed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private static class RerouteActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<Main> activityWeakReference;
//
//        RerouteActivityLocationCallback(Main activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            Main activity = activityWeakReference.get();
//            if (activity != null) {
//                Location location = result.getLastLocation();
//                if (location == null) {
//                    return;
//                }
//                activity.updateLocation(location);
//            }
//        }
//
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            Timber.e(exception);
//        }
//    }
//
//
//
//
//
//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
//        if (milestone instanceof BannerInstructionMilestone) {
//            BannerText primaryInstruction = ((BannerInstructionMilestone) milestone).getBannerInstructions().primary();
//            InstructionLoader loader = new InstructionLoader(primaryTxt, primaryInstruction);
//            loader.loadInstruction();
//
//            BannerText secondaryInstruction = ((BannerInstructionMilestone) milestone).getBannerInstructions().secondary();
//            if(secondaryInstruction!=null){
//                InstructionLoader loader2 = new InstructionLoader(secondaryTxt, secondaryInstruction);
//                loader2.loadInstruction();
//            } else {
//                secondaryTxt.setText("");
//            }
//        }

//        TextToSpeech tts = new TextToSpeech(this, this);
//        TextToSpeech tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    int ttsLang = tts.setLanguage(Locale.US);
//
//                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
//                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        Log.e("TTS", "The Language is not supported!");
//                    } else {
//                        Log.i("TTS", "Language Supported.");
//                    }
//                    Log.i("TTS", "Initialization success.");
//                } else {
//                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        tts.setLanguage(Locale.US);
//        tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
//        Toast.makeText(getApplicationContext(), instruction,Toast.LENGTH_SHORT).show();

//    }

//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
//        exampleInstructionPlayer.play(instruction);
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        map.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        map.onPause();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (int i = 0; i < grantResults.length; i++) {
//            permissionsToRequest.add(permissions[i]);
//        }
//        if (permissionsToRequest.size() > 0) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//    private void requestPermissionsIfNecessary(String[] permissions) {
//        ArrayList<String> permissionsToRequest = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionsToRequest.add(permission);
//            }
//        }
//        if (permissionsToRequest.size() > 0) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    permissionsToRequest.toArray(new String[0]),
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//
//
//    }
//
//
//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//            mapController.setCenter(startPoint);
//            setIcon(startPoint);
//            Main.location=location;
//            map.setVisibility(View.VISIBLE);
//            loadingGif.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        Marker startMarker;
//        public void setIcon(GeoPoint startPoint){
//            map.getOverlays().remove(startMarker);
//            startMarker = new Marker(map);
//            startMarker.setIcon(getDrawable(R.drawable.marker_default));
//            startMarker.setPosition(startPoint);
//            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            map.getOverlays().add(startMarker);
//        }
//    };
//
//
//    public void currentLocation(View view) {
//        if(location == null) return;
//        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//        mapController.setCenter(startPoint);
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);
                //return;
            } else {
                googleMap.setMyLocationEnabled(true);
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,false));

                if (location != null)
                {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_BOTTOM, 0);
        rlp.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
        locationButton.setLeft(100);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        rlp.setMargins(0, (height - 250), 110, 0);

        locationButton.post(new Runnable(){
            @Override
            public void run() {
                locationButton.performClick();
            }
        });
        setUpMapIfNeeded();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        case 1:
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            // permission not granted
        }
        else {
            // permission granted
        }
//        break;
        //default:
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onBackPressed() {
        ViewDialog alert = new ViewDialog();
        alert.showDialog(Main.this, "آیا مطمئن هستید می خواهید مسیریابی را لغو کنید؟", Main.this);
    }
}

