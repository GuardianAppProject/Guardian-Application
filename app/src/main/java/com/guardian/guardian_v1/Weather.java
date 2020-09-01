package com.guardian.guardian_v1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Weather {

    private static final String KEY = "91ff80ce4b10edc0f4ebf7391d2edfb2";  //api key

    private float temperature;
    private WeatherType weatherType;
    private String description;
    private float windSpeed;
    private String Id;

    private Weather(Float temperature, String weatherType, String description, Float windSpeed, String Id) {
        this.temperature = temperature;
        this.weatherType = WeatherType.valueOf(weatherType);
        this.description = description;
        this.windSpeed = windSpeed;
        this.Id = Id;
    }

    public static Weather getWeather(int lon, int lat) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + KEY).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        JSONObject response = null;
        try {
            response = new JSONObject(content.toString());
            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
            JSONObject wind = response.getJSONObject("wind");
            Float windSpeed = Float.valueOf(wind.get("speed").toString());
            Float temperature = Float.valueOf(response.getJSONObject("main").get("temp").toString());
            String main = weather.get("main").toString().replace("\"", "");
            String description = weather.get("description").toString().replace("\"", "");
            String icon = weather.get("icon").toString().replace("\"", "");
            System.out.println(response.toString());
            return new Weather(temperature, main, description, windSpeed, icon);
        } catch (JSONException e) {
            return null;
        }
    }


    enum WeatherType {
        Thunderstorm,
        Drizzle,
        Rain,
        Snow,
        Clear,
        Clouds,
        Mist, Smoke, Haze, Dust, Fog, Sand, Ash, Squall, Tornado
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public String getImageUrl() {
        return "http://openweathermap.org/img/wn/" + Id + "@2x.png";
    }

    //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
}
