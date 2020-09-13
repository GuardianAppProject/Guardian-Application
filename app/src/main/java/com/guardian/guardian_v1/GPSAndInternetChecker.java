package com.guardian.guardian_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

public class GPSAndInternetChecker {
    public static boolean check(Context context){
        if (! isGPSOn(context)){
            showGPSAlert(context);
            return false;
        }
        if(!isInternetConnected(context)) {
            showInternetAlert(context);
            return false;
        }
        return true;
    }


    public static void showInternetAlert(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("عدم اتصال به اینترنت                 ")
                .setMessage("اتصال شما به اینترنت برقرار نیست. برای استفاده از گاردین به اینترنت متصل شوید!")
                .setPositiveButton("اینترنت Wi-Fi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("اینترنت موبایل",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        context.startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));

                    }
                })
                .show();

    }

    public static void showGPSAlert(final Context context){
        new AlertDialog.Builder(context)
                .setTitle("عدم فعال بودن GPS                  ")
                .setMessage("برای استفاده از گاردین لطفا GPS تلفن همراه خود را روشن نمایید.")
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .show();

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
