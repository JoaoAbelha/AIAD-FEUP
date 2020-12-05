package com.Data;

import java.util.HashMap;
import java.util.Map;

public class WeatherStation {

    private HashMap<String, Float> velocities = new HashMap<>();
    private HashMap<Integer, String> weather = new HashMap<>();
    private String currentWeather;

    public WeatherStation(HashMap<String, Float> velocities, HashMap<Integer, String> weather) {
        this.velocities = velocities;
        this.weather = weather;

        Map.Entry<Integer,String> entry = weather.entrySet().iterator().next();
        this.currentWeather = entry.getValue();
    }

    public float getVelocity(String weather) {
        return velocities.get(weather);
    }

    public String getWeather(int time) {
        return weather.get(time);
    }

    public boolean existsWeather(int time) {
        return weather.containsKey(time);
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }
}

