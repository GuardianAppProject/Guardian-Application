package com.guardian.guardian_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

public class GPSAndInternetChecker {
    public static boolean check(Context context, double height, double width){
        if (! isGPSOn(context)){
            showGPSAlert(context, height, width);
            return false;
        }
        if(!isInternetConnected(context)) {
            showInternetAlert(context, height, width);
            return false;
        }
        return true;
    }


    public static void showInternetAlert(final Context context, Double height, Double width){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.internet_alert_dialog, null);

        Button wifiBtn = view.findViewById(R.id.wifiButton);
        wifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        Button internetBtn = view.findViewById(R.id.internetButton);
        internetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
            }
        });

//        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 700;
        lp.height = 800;
        lp.x= (int)0;
        lp.y=(int)(height * 1/7);
        alertDialog.getWindow().setAttributes(lp);


//        builder.setPositiveButton("اینترنت Wi-Fi", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//            }
//        });
//
//        builder.setNegativeButton("اینترنت موبایل",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                context.startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
//
//            }
//        });


//        new AlertDialog.Builder(context)
//                .setTitle("عدم اتصال به اینترنت                 ")
//                .setMessage("اتصال شما به اینترنت برقرار نیست. برای استفاده از گاردین به اینترنت متصل شوید!")
//                .setPositiveButton("اینترنت Wi-Fi", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                    }
//                })
//                .setNegativeButton("اینترنت موبایل",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        context.startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
//
//                    }
//                })
//                .show();

    }

    public static void showGPSAlert(final Context context, Double height, Double width){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.gps_alert_dialog, null);

        Button gpsBtn = view.findViewById(R.id.gpsButton);
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        //        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 700;
        lp.height = 800;
        lp.x= (int)0;
        lp.y=(int)(height * 1/7);
        alertDialog.getWindow().setAttributes(lp);


//        new AlertDialog.Builder(context)
//                .setTitle("عدم فعال بودن GPS                  ")
//                .setMessage("برای استفاده از گاردین لطفا GPS تلفن همراه خود را روشن نمایید.")
//                .setPositiveButton("موقعیت مکانی GPS", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .show();

    }

    public static boolean isGPSOn(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        return !(!gps_enabled && !network_enabled) ;
    }

    public static boolean isInternetConnected(Context context){

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected ;
    }
}
