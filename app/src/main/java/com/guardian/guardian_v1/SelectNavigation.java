package com.guardian.guardian_v1;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.guardian.guardian_v1.SleepSpeedManager.SleepSpeedDetectorService;
import com.guardian.guardian_v1.Transmission.AverageWorker;
import com.guardian.guardian_v1.Transmission.SingleUserDetailed;

//package com.guardian.guardian_v1;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.google.android.material.navigation.NavigationView;
//import com.guardian.guardian_v1.DriveStatus.Shake;
//import com.guardian.guardian_v1.Transmission.AverageWorker;
//import com.mapbox.android.core.permissions.PermissionsListener;
//import com.mapbox.android.core.permissions.PermissionsManager;
//import com.mapbox.api.directions.v5.models.DirectionsResponse;
//import com.mapbox.api.directions.v5.models.DirectionsRoute;
//import com.mapbox.api.geocoding.v5.models.CarmenFeature;
//import com.mapbox.geojson.Feature;
//import com.mapbox.geojson.FeatureCollection;
//import com.mapbox.geojson.Point;
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.camera.CameraPosition;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.location.LocationComponent;
//import com.mapbox.mapboxsdk.location.modes.CameraMode;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
//import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
//import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
//import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
//import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
//import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
//import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
//
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
//import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
//
public class SelectNavigation extends Activity {//implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    //
//    // variables for adding location layer
//    private MapView mapView;
//    private MapboxMap mapboxMap;
//    // variables for adding location layer
//    private PermissionsManager permissionsManager;
//    private LocationComponent locationComponent;
//    // variables for calculating and drawing a route
//    private DirectionsRoute currentRoute;
//    private static final String TAG = "DirectionsActivity";
//    private NavigationMapRoute navigationMapRoute;
//    // variables needed to initialize navigation
//    private Button button;
//
//    Point destinationPoint2;
//    Point originPoint2;
//
//
//    private CarmenFeature home;
//    private CarmenFeature work;
//    private String geojsonSourceLayerId = "geojsonSourceLayerId";
//    private String symbolIconId = "symbolIconId";
//    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
//
//    // Menu
//    private DrawerLayout mDrawer;
//    private NavigationView nvDrawer;
//
    private  int counter = 0;
    private int total = 0; // the total number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//
        super.onCreate(savedInstanceState);
//        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_select_navigation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.appThemeColor));
        }

//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync(this);
//
//
//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
//
//        // ...From section above...
//        // Find our drawer view
//        nvDrawer = (NavigationView) findViewById(R.id.nvView2);
//        // Setup drawer view
//        setupDrawerContent(nvDrawer);
//
//        Button button = (Button) findViewById(R.id.menuButton2);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDrawer();
//            }
//        });
//
        TextView allTimeAvg = (TextView) findViewById(R.id.tv);

        SingleUserDetailed.getUserDetailed();

        String s = AverageWorker.getAverage();
        double x;
        if(isNumeric(s)) {
            x = Double.parseDouble(AverageWorker.getAverage());
        } else {
            x = 100;
        }
        counter = (int) Math.floor(x);
//
//                            allTimeAvg.setText("" + counter + "%");
//
        StatusCalculator statusCalculator = new StatusCalculator(this);
        int backgroundNumber = statusCalculator.calculateBackgroundAlgorithm(counter);
        ImageView algorithmBackground = (ImageView) findViewById(R.id.avg_background);
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

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, counter);// here you set the range, from 0 to "count" value
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                allTimeAvg.setText(String.valueOf(animation.getAnimatedValue()) + "%");
            }
        });
        animator.setDuration(1900); // here you set the duration of the anim
        animator.start();

        TextView algorithmStatusText = (TextView) findViewById(R.id.avg_desc);
        algorithmStatusText.setText(statusCalculator.calculateStatusAlgorithm(counter));

        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPermissionRequest()) {
                    Intent i = new Intent(SelectNavigation.this, SeatBelt.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        TextView tipText = (TextView) findViewById(R.id.tipText);
        TipHandler tipHandler = new TipHandler();
        tipText.setText(tipHandler.getTip());
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                tipText.setText(tipHandler.getTip());
                ha.postDelayed(this, 13000);
            }
        }, 13000);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
                //return;
            }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    private boolean firstTime = true;
    private boolean showPermissionRequest() {

        if(firstTime) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission
                                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 1);
                    //return;
                }
            }
            firstTime = false;
            return !(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED);
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.permission_dialog, null);

                Button updateBtn = view.findViewById(R.id.permissionButton);
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });

                //        builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.setView(view, 0, 0, 0, 0);
                alertDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        lp.width = 700;
//        lp.height = 800;
                lp.x= (int)0;
                lp.y=(int)0;
                alertDialog.getWindow().setAttributes(lp);

                Toast.makeText(this, "گاردین برای یافتن موقعیت شما روی نقشه به موقعیت مکانی شما نیاز دارد. لطفا این دسترسی را به برنامه بدهید!", Toast.LENGTH_LONG).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        99);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
                return false;
            }
        }
        return true;
    }
}
//
//
//
//    public void openDrawer(){
//        mDrawer.openDrawer(GravityCompat.START);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // The action bar home/up action should open or close the drawer.
//        if (item.getItemId() == android.R.id.home) {
//            mDrawer.openDrawer(GravityCompat.START);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        selectDrawerItem(menuItem);
//                        return true;
//                    }
//                });
//    }
//
//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the fragment to show based on nav item clicked
////        Fragment fragment = null;
////        Class fragmentClass;
//        switch(menuItem.getItemId()) {
//            case R.id.account:
//                Intent i = new Intent(SelectNavigation.this, MyAccount.class);
//                startActivity(i);
//                finish();
//                break;
//            case R.id.support:
//                Intent i2 = new Intent(SelectNavigation.this, Support.class);
//                startActivity(i2);
//                finish();
//                break;
//            case R.id.info:
//                Intent i3 = new Intent(SelectNavigation.this, Info.class);
//                startActivity(i3);
//                finish();
//                break;
//            case R.id.settings:
//                Intent i4 = new Intent(SelectNavigation.this, Setting.class);
//                startActivity(i4);
//                finish();
//                break;
//        }
//
////        try {
////            fragment = (Fragment) fragmentClass.newInstance();
////            FragmentManager fragmentManager = getSupportFragmentManager();
////            fragmentManager.beginTransaction().replace(R.id.flContent,fragment ).commit();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        // Insert the fragment by replacing any existing fragment
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
//
//
//        // Highlight the selected item has been done by NavigationView
////        menuItem.setChecked(true);
//
//        // Set action bar title
////        setTitle(menuItem.getTitle());
//        // Close the navigation drawer
//        mDrawer.closeDrawers();
//
//    }
//
//
//
//
//
//
//
//
//
//    @Override
//    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//
//                initSearchFab();
//
////                addUserLocations();
//
//// Add the symbol layer icon to map for future use
////                style.addImage(symbolIconId, BitmapFactory.decodeResource(
////                        MainActivity.this.getResources(), R.drawable.green_marker2));
//
//// Create an empty GeoJSON source using the empty feature collection
//                setUpSource(style);
//
//// Set up a new symbol layer for displaying the searched location's feature coordinates
//                setupLayer(style);
//
//                LocalizationPlugin localizationPlugin = new LocalizationPlugin(mapView, mapboxMap , style);
//                localizationPlugin.setMapLanguage(MapLocale.LOCAL_NAME);
//
//                enableLocationComponent(style);
//
//                addDestinationIconSymbolLayer(style);
//
//                mapboxMap.addOnMapClickListener(SelectNavigation.this);
//                button = findViewById(R.id.startButton);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        boolean simulateRoute = true;
////                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
////                                .directionsRoute(currentRoute)
////                                .shouldSimulateRoute(simulateRoute)
////                                .build();
////// Call this method with Context from within an Activity
////                        NavigationLauncher.startNavigation(MainActivity.this, options);
//
//                        Intent i = new Intent(SelectNavigation.this, Main.class);
//                        i.putExtra("originLat", originPoint2.latitude());
//                        i.putExtra("originLng", originPoint2.longitude());
//                        i.putExtra("destinationLat", destinationPoint2.latitude());
//                        i.putExtra("destinationLng", destinationPoint2.longitude());
//                        startActivity(i);
//                        finish();
//                    }
//                });
//            }
//        });
//    }
//
//
//    private void initSearchFab() {
//        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new PlaceAutocomplete.IntentBuilder()
//                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.access_token))
//                        .placeOptions(PlaceOptions.builder()
//                                .backgroundColor(Color.parseColor("#EEEEEE"))
//                                .limit(10)
////                                .addInjectedFeature(home)
////                                .addInjectedFeature(work)
//                                .country("IR")
//                                .build(PlaceOptions.MODE_CARDS))
//                        .build(SelectNavigation.this);
//                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
//            }
//        });
//    }
//
//
//    private void addUserLocations() {
////        home = CarmenFeature.builder().text("Mapbox SF Office")
////                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
////                .placeName("50 Beale St, San Francisco, CA")
////                .id("mapbox-sf")
////                .properties(new JsonObject())
////                .build();
////
////        work = CarmenFeature.builder().text("Mapbox DC Office")
////                .placeName("740 15th Street NW, Washington DC")
////                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
////                .id("mapbox-dc")
////                .properties(new JsonObject())
////                .build();
//    }
//
//    private void setUpSource(@NonNull Style loadedMapStyle) {
//        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
//    }
//
//    private void setupLayer(@NonNull Style loadedMapStyle) {
//        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
//                iconImage(symbolIconId),
//                iconOffset(new Float[] {0f, -8f})
//        ));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
//
//// Retrieve selected location's CarmenFeature
//            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
//
//// Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
//// Then retrieve and update the source designated for showing a selected location's symbol layer icon
//
//            if (mapboxMap != null) {
//                Style style = mapboxMap.getStyle();
//                if (style != null) {
//                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
//                    if (source != null) {
//                        source.setGeoJson(FeatureCollection.fromFeatures(
//                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
//                    }
//
//// Move map camera to the selected location
//                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
//                            new CameraPosition.Builder()
//                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
//                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
//                                    .zoom(14)
//                                    .build()), 4000);
//                }
//            }
//        }
//    }
//
//    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
////        marker_green.png
//
//        loadedMapStyle.addImage("destination-icon-id",
//                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));//mapbox_marker_icon_default
//        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
//        loadedMapStyle.addSource(geoJsonSource);
//        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
//        destinationSymbolLayer.withProperties(
//                iconImage("destination-icon-id"),
//                iconAllowOverlap(true),
//                iconIgnorePlacement(true)
//        );
//        loadedMapStyle.addLayer(destinationSymbolLayer);
//    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    @Override
//    public boolean onMapClick(@NonNull LatLng point) {
//
//        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
//        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
//                locationComponent.getLastKnownLocation().getLatitude());
//
//        destinationPoint2 = destinationPoint;
//        originPoint2 = originPoint;
//
//        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
//        if (source != null) {
//            source.setGeoJson(Feature.fromGeometry(destinationPoint));
//        }
//
//        getRoute(originPoint, destinationPoint);
//        button.setEnabled(true);
//        button.setBackgroundResource(R.color.mapboxBlue);
//        return true;
//    }
//
//    private void getRoute(Point origin, Point destination) {
//        NavigationRoute.builder(this)
//                .accessToken(Mapbox.getAccessToken())
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//// You can get the generic HTTP info about the response
//                        Log.d(TAG, "Response code: " + response.code());
//                        if (response.body() == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
//                            return;
//                        } else if (response.body().routes().size() < 1) {
//                            Log.e(TAG, "No routes found");
//                            return;
//                        }
//
//                        currentRoute = response.body().routes().get(0);
//
//// Draw the route on the map
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute.removeRoute();
//                        } else {
//                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
//                        }
//                        navigationMapRoute.addRoute(currentRoute);
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
//                    }
//                });
//    }
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//// Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//// Activate the MapboxMap LocationComponent to show user location
//// Adding in LocationComponentOptions is also an optional parameter
//            locationComponent = mapboxMap.getLocationComponent();
//            locationComponent.activateLocationComponent(this, loadedMapStyle);
//            locationComponent.setLocationComponentEnabled(true);
//// Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            enableLocationComponent(mapboxMap.getStyle());
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//            finish();
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
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    public String read(){
//        //reading text from file
//        String string = "";
//        try {
//            FileInputStream fileIn= openFileInput("tokenFile.txt");
//            InputStreamReader InputRead= new InputStreamReader(fileIn);
//
//            char[] inputBuffer= new char[10000];
//
//            int charRead;
//
//            while ((charRead=InputRead.read(inputBuffer))>0) {
//                // char to string conversion
//                String readstring=String.copyValueOf(inputBuffer,0,charRead);
//                string +=readstring;
//            }
//            InputRead.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return string;
//    }
//
//}