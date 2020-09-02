package com.guardian.guardian_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.models.BannerText;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionLoader;
import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionView;
import com.mapbox.services.android.navigation.v5.instruction.Instruction;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
import com.mapbox.services.android.navigation.v5.milestone.BannerInstructionMilestone;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Main extends AppCompatActivity implements OnMapReadyCallback,
        Callback<DirectionsResponse>, MapboxMap.OnMapClickListener, NavigationEventListener,
        OffRouteListener, ProgressChangeListener, MilestoneEventListener, TextToSpeech.OnInitListener {


    // Map
    MapView mapView;
    View contentLayout;
    InstructionView instructionView;

    private Point ORIGIN = Point.fromLngLat(-0.358764, 39.494876);
    private Point DESTINATION = Point.fromLngLat(-0.383524, 39.497825);
    private Polyline polyline;

    private final RerouteActivityLocationCallback callback = new RerouteActivityLocationCallback(this);
    private Location lastLocation;
    private ReplayRouteLocationEngine mockLocationEngine;
    private MapboxNavigation navigation;
    private MapboxMap mapboxMap;
    private boolean running = false;
    private boolean tracking;
    private boolean wasInTunnel = false;

    TextView distanceRem;
    TextView durationRem;
    TextView arrivalTime;
    TextView primaryTxt;
    TextView secondaryTxt;
    Button stopButton;

    public static String routeStyle = Style.DARK;

    // Menu
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NavigationViewLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ORIGIN = Point.fromLngLat(getIntent().getDoubleExtra("originLng", 0), getIntent().getDoubleExtra("originLat", 0));
        DESTINATION = Point.fromLngLat(getIntent().getDoubleExtra("destinationLng", 0), getIntent().getDoubleExtra("destinationLat", 0));

        mapView = (MapView)findViewById(R.id.mapView);
        contentLayout = (View) findViewById(android.R.id.content);
        instructionView = (InstructionView) findViewById(R.id.instructionView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        MapboxNavigationOptions options = MapboxNavigationOptions.builder().isDebugLoggingEnabled(false).build();
        navigation = new MapboxNavigation(getApplicationContext(), getResources().getString(R.string.access_token), options);
        navigation.addNavigationEventListener(this);
        navigation.addMilestoneEventListener(this);

        primaryTxt = (TextView) findViewById(R.id.primaryTxt);
        distanceRem = (TextView) findViewById(R.id.distanceRem);
        durationRem = (TextView) findViewById(R.id.durationRem);
        arrivalTime = (TextView) findViewById(R.id.arrivalTime);
        secondaryTxt = (TextView) findViewById(R.id.secondaryTxt);
        stopButton = (Button) findViewById(R.id.stopButt);


        instructionView.retrieveSoundButton().hide();
        instructionView.retrieveSoundButton().addOnClickListener(
                v -> Toast.makeText(this, "Sound button clicked!", Toast.LENGTH_SHORT).show()
        );

        Instruction myInstruction = new Instruction() {
            @Override
            public String buildInstruction(RouteProgress routeProgress) {
                return routeProgress.currentLegProgress().upComingStep().maneuver().instruction();
            }
        };

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ViewDialog alert = new ViewDialog();
                alert.showDialog(Main.this, "آیا مطمئن هستید می خواهید مسیریابی را لغو کنید؟", Main.this);
            }
        });

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
                finish();
                break;
            case R.id.support:
                Intent i2 = new Intent(Main.this, Support.class);
                startActivity(i2);
                finish();
                break;
            case R.id.info:
                Intent i3 = new Intent(Main.this, Info.class);
                startActivity(i3);
                finish();
                break;
            case R.id.settings:
                Intent i4 = new Intent(Main.this, Setting.class);
                startActivity(i4);
                finish();
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








    public void changeIntent(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        shutdownLocationEngine();
        shutdownNavigation();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.addOnMapClickListener(this);
        mapboxMap.setStyle(routeStyle, style -> {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, style);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setRenderMode(RenderMode.GPS);

            mockLocationEngine = new ReplayRouteLocationEngine();
            getRoute(ORIGIN, DESTINATION);
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
//        if (!running || mapboxMap == null || lastLocation == null) {
//            return false;
//        }
//
//        mapboxMap.addMarker(new MarkerOptions().position(point));
//        mapboxMap.removeOnMapClickListener(this);
//
//        DESTINATION = Point.fromLngLat(point.getLongitude(), point.getLatitude());
//        resetLocationEngine(DESTINATION);
//
//        tracking = false;
        return false;
    }

    @Override
    public void onRunning(boolean running) {
        this.running = running;
        if (running) {
            navigation.addOffRouteListener(this);
            navigation.addProgressChangeListener(this);
        }
    }

    @Override
    public void userOffRoute(Location location) {
        ORIGIN = Point.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude());
        getRoute(ORIGIN, DESTINATION);
        Snackbar.make(contentLayout, "User Off Route", Snackbar.LENGTH_SHORT).show();
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        boolean isInTunnel = routeProgress.inTunnel();
        lastLocation = location;
        if (!wasInTunnel && isInTunnel) {
            wasInTunnel = true;
            Snackbar.make(contentLayout, "Enter tunnel!", Snackbar.LENGTH_SHORT).show();
        }
        if (wasInTunnel && !isInTunnel) {
            wasInTunnel = false;
            Snackbar.make(contentLayout, "Exit tunnel!", Snackbar.LENGTH_SHORT).show();
        }
        if (tracking) {
            mapboxMap.getLocationComponent().forceLocationUpdate(location);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .zoom(16)
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .bearing(location.getBearing())
                    .tilt(25)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
        }
        instructionView.updateDistanceWith(routeProgress);

        navigation.addProgressChangeListener(new ProgressChangeListener() {
            @Override
            public void onProgressChange(Location location, RouteProgress routeProgress) {
                distanceRem.setText(calculateDistance(routeProgress.distanceRemaining()));
                durationRem.setText(calculateDuration(routeProgress.durationRemaining()));
                arrivalTime.setText(calculateArrivalTime(routeProgress.durationRemaining()));
            }

//            public void onProgressChange(Location location, RouteStepProgress routeStepProgress) {
//                textView2.setText(String.valueOf(routeStepProgress.distanceRemaining()));
//
//            }


        });




    }

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

    @Override
    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
        Timber.d(call.request().url().toString());
        if (response.body() != null) {
            if (!response.body().routes().isEmpty()) {
                DirectionsRoute route = response.body().routes().get(0);
                drawRoute(route);
                resetLocationEngine(route);
                navigation.startNavigation(route);
                mapboxMap.addOnMapClickListener(this);
                tracking = true;
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable throwable) {
        Timber.e(throwable);
    }

    void updateLocation(Location location) {
        if (!tracking) {
            mapboxMap.getLocationComponent().forceLocationUpdate(location);
        }
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .origin(origin)
                .destination(destination)
                .accessToken(Mapbox.getAccessToken())
                .build().getRoute(this);
    }

    private void drawRoute(DirectionsRoute route) {
        List<LatLng> points = new ArrayList<>();
        List<Point> coords = LineString.fromPolyline(route.geometry(), Constants.PRECISION_6).coordinates();

        for (Point point : coords) {
            points.add(new LatLng(point.latitude(), point.longitude()));
        }

        if (!points.isEmpty()) {
            if (polyline != null) {
                mapboxMap.removePolyline(polyline);
            }
            polyline = mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .color(R.color.colorAccent)//Color.parseColor(getString(R.string.blue))
                    .width(5));
        }
    }

    private void resetLocationEngine(Point point) {
        mockLocationEngine.moveTo(point);
        navigation.setLocationEngine(mockLocationEngine);
    }

    private void resetLocationEngine(DirectionsRoute directionsRoute) {
        mockLocationEngine.assign(directionsRoute);
        navigation.setLocationEngine(mockLocationEngine);
    }

    private void shutdownLocationEngine() {
        if (mockLocationEngine != null) {
            mockLocationEngine.removeLocationUpdates(callback);
        }
    }

    private void shutdownNavigation() {
        navigation.removeNavigationEventListener(this);
        navigation.removeProgressChangeListener(this);
        navigation.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if(status==TextToSpeech.SUCCESS)
        {
//            Toast.makeText(getApplicationContext(), "engine installed",Toast.LENGTH_SHORT).show();
        }
        if(status==TextToSpeech.ERROR)
        {
//            Toast.makeText(getApplicationContext(), "engine not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private static class RerouteActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<Main> activityWeakReference;

        RerouteActivityLocationCallback(Main activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {
            Main activity = activityWeakReference.get();
            if (activity != null) {
                Location location = result.getLastLocation();
                if (location == null) {
                    return;
                }
                activity.updateLocation(location);
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Timber.e(exception);
        }
    }





    @Override
    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
        if (milestone instanceof BannerInstructionMilestone) {
            BannerText primaryInstruction = ((BannerInstructionMilestone) milestone).getBannerInstructions().primary();
            InstructionLoader loader = new InstructionLoader(primaryTxt, primaryInstruction);
            loader.loadInstruction();

            BannerText secondaryInstruction = ((BannerInstructionMilestone) milestone).getBannerInstructions().secondary();
            if(secondaryInstruction!=null){
                InstructionLoader loader2 = new InstructionLoader(secondaryTxt, secondaryInstruction);
                loader2.loadInstruction();
            } else {
                secondaryTxt.setText("");
            }
        }

        TextToSpeech tts = new TextToSpeech(this, this);
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
        tts.setLanguage(Locale.US);
        tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
//        Toast.makeText(getApplicationContext(), instruction,Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
//        exampleInstructionPlayer.play(instruction);
//    }


}
