package com.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WeatherStation {

    private HashMap<String, Float> velocities = new HashMap<>();
    private String currentWeather;

    public WeatherStation(HashMap<String, Float> velocities) {
        this.velocities = velocities;
        ArrayList<String> weathers = new ArrayList<>(velocities.keySet());
        Collections.shuffle(weathers);
        this.currentWeather = weathers.get(0);
    }

    public float getVelocity(String weather) {
        return velocities.get(weather);
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public HashMap<String, Float> getVelocities() {
        return velocities;
    }
}

