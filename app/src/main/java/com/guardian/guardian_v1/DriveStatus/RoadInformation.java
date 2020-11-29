package com.guardian.guardian_v1.DriveStatus;

import androidx.appcompat.app.AppCompatActivity;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RoadInformation extends AppCompatActivity {
    public static final String URL_FORMAT = "http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=";
    //"http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=72.998251,33.670049,72.998451,33.670249]"
    //bbox = min Longitude , min Latitude , max Longitude , max Latitude

    private int counter, i;
    public int speed = 0;
    public int lanes = 0;

    public boolean oneway = false;
    public enum HighwayTags {motorway, trunk, primary, secondary, tertiary, unclassified, residential, motorway_link
        , trunk_link, primary_link, secondary_link, tertiary_link, road}
    public HighwayTags highwayResult;

    public RoadInformation() {

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

    public int GetSpeedLimit(String xml) {
        int speedResult = 0;
        String[] lines = xml.split(System.getProperty("line.separator"));
        for (counter = 0; counter < lines.length; counter++) {

            //speed limit
            if (lines[counter].contains("maxspeed")) {
                String[] parts = lines[counter].split(" ");
                String[] hold = parts[parts.length - 1].split("=");
                String[] temp = hold[hold.length - 1].split("\"");
                speedResult = Integer.parseInt(temp[1]);
            }

        }
        return speedResult;
    }

    public int GetLanes(String xml) {
        int lanesResult = 0;
        String[] lines = xml.split(System.getProperty("line.separator"));
        for(counter = 0; counter < lines.length; counter++) {
            if(lines[counter].contains("lanes")) {
                String[] parts = lines[counter].split(" ");
                String[] hold = parts[parts.length - 1].split("=");
                String[] temp = hold[hold.length - 1].split("\"");
                lanesResult = Integer.parseInt(temp[1]);
            }
        }
        return lanesResult;
    }

    public HighwayTags RoadType(String xml) {
        HighwayTags tagResult = HighwayTags.road;
        String[] lines = xml.split(System.getProperty("line.separator"));
        for(counter = 0; counter < lines.length; counter++) {
            if(lines[counter].contains("highway")) {
                if(lines[counter].contains("motorway")) {
                    tagResult = HighwayTags.motorway;
                } else if(lines[counter].contains("trunk")) {
                    tagResult = HighwayTags.trunk;
                } else if(lines[counter].contains("primary")) {
                    tagResult = HighwayTags.primary;
                } else if(lines[counter].contains("secondary")) {
                    tagResult = HighwayTags.secondary;
                } else if(lines[counter].contains("tertiary")) {
                    tagResult = HighwayTags.tertiary;
                } else if(lines[counter].contains("unclassified")) {
                    tagResult = HighwayTags.unclassified;
                } else if(lines[counter].contains("residential")) {
                    tagResult = HighwayTags.residential;
                } else if(lines[counter].contains("motorway_link")) {
                    tagResult = HighwayTags.motorway_link;
                } else if(lines[counter].contains("trunk_link")) {
                    tagResult = HighwayTags.trunk_link;
                } else if(lines[counter].contains("primary_link")) {
                    tagResult = HighwayTags.primary_link;
                } else if(lines[counter].contains("secondary_link")) {
                    tagResult = HighwayTags.secondary_link;
                } else if(lines[counter].contains("tertiary_link")) {
                    tagResult = HighwayTags.tertiary_link;
                } else {
                    tagResult = HighwayTags.road;
                }
            }
        }
        return tagResult;
    }

    public boolean IsOneway(String xml) {
        String[] lines = xml.split(System.getProperty("line.separator"));
        for(counter = 0; counter < lines.length; counter++) {
            if(lines[counter].contains("oneway")) {
                if(lines[counter].contains("yes")) {
                    oneway = true;
                }
                else if(lines[counter].contains("no")) {
                    oneway = false;
                }
                else {
                    oneway = false;
                }
            }
        }
        return oneway;
    }
}
