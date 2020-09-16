package com.guardian.guardian_v1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RoadInformation extends AppCompatActivity {
    public static final String URL_FORMAT = "http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=";
    //"http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=72.998251,33.670049,72.998451,33.670249]"
    //bbox = min Longitude , min Latitude , max Longitude , max Latitude

    private int counter, i;
    public int speed = 0;
    public int lanes = 0;
    public int test = 0;
    enum highwayTags {motorway, trunk, primary, secondary, tertiary, unclassified, residential, motorway_link
        , trunk_link, primary_link, secondary_link, tertiary_link, road}
    highwayTags highwayResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = getXmlFromUrl(33.670049, 33.670249, 72.998251, 72.998451);
                String[] lines = result.split(System.getProperty("line.separator"));
                for(counter = 0; counter < lines.length; counter++) {

                    //speed limit
                    if(lines[counter].contains("maxspeed")) {
                        String[] parts = lines[counter].split(" ");
                        String[] hold = parts[parts.length - 1].split("=");
                        String[] temp = hold[hold.length - 1].split("\"");
                        Log.d("temp", temp[1]);
                        speed = Integer.parseInt(temp[1]);
                        Log.d("speed", lines[counter]);
                    }

                    //lanes
                    if(lines[counter].contains("lanes")) {
                        String[] parts = lines[counter].split(" ");
                        String[] hold = parts[parts.length - 1].split("=");
                        String[] temp = hold[hold.length - 1].split("\"");
                        Log.d("temp", temp[1]);
                        lanes = Integer.parseInt(temp[1]);
                        Log.d("lanes", lanes + "");
                        Log.d("lanes", lines[counter]);
                    }

                    if(lines[counter].contains("highway")) {
                        if(lines[counter].contains("motorway")) {
                            highwayResult = highwayTags.motorway;
                        } else if(lines[counter].contains("trunk")) {
                            highwayResult = highwayTags.trunk;
                        } else if(lines[counter].contains("primary")) {
                            highwayResult = highwayTags.primary;
                        } else if(lines[counter].contains("secondary")) {
                            highwayResult = highwayTags.secondary;
                        } else if(lines[counter].contains("tertiary")) {
                            highwayResult = highwayTags.tertiary;
                        } else if(lines[counter].contains("unclassified")) {
                            highwayResult = highwayTags.unclassified;
                        } else if(lines[counter].contains("residential")) {
                            highwayResult = highwayTags.residential;
                        } else if(lines[counter].contains("motorway_link")) {
                            highwayResult = highwayTags.motorway_link;
                        } else if(lines[counter].contains("trunk_link")) {
                            highwayResult = highwayTags.trunk_link;
                        } else if(lines[counter].contains("primary_link")) {
                            highwayResult = highwayTags.primary_link;
                        } else if(lines[counter].contains("secondary_link")) {
                            highwayResult = highwayTags.secondary_link;
                        } else if(lines[counter].contains("tertiary_link")) {
                            highwayResult = highwayTags.tertiary_link;
                        } else {
                            highwayResult = highwayTags.road;
                        }
                        Log.d("highway result", highwayResult.toString() + "");
                        Log.d("lanes", lines[counter]);
                    }
                }
                Log.d("responce", result);

            }
        });

        thread.start();

    }

    public String getXmlFromUrl(double lat1, double lat2, double long1, double long2) {
        String xml = null;
        String url = URL_FORMAT + long1 + "," + lat1 + "," + long2 + "," + lat2 + "]";
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

}
