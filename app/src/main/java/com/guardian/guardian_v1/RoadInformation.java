package com.guardian.guardian_v1;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import android.os.Bundle;

public class RoadInformation extends AppCompatActivity {

    public static final String URL_FORMAT = "http://api.tomtom.com/traffic/map/4/tile/incidents/5/4/8.pbf?key=8GF1ZAsuFhzezxeharQurYGxedjTmGiQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestData(0, 10, 20, 30);
        
    }

    private void requestData(double minlog, double minlat, double maxlog, double maxlat) {
        String url;
        url = URL_FORMAT;
        final JsonObjectRequest myjson = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responce", "responce " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(myjson);
    }

}
