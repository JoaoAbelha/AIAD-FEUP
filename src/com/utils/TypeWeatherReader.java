package com.utils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TypeWeatherReader extends FileReader {

    final int WEATHER_TYPE = 0;
    final int SPEED_FACTOR = 1;

    private final HashMap<String, Float> velocities = new HashMap<>();

    public TypeWeatherReader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readLine(String[] line) {
        velocities.put(line[WEATHER_TYPE], Float.parseFloat(line[SPEED_FACTOR]));
    }

    @Override
    public HashMap<String, Float> getInfo() {
        for(Map.Entry<String, Float> v : velocities.entrySet()) {
            String weather = v.getKey();
            Float velocity = v.getValue();
            System.out.println(weather + ":" + velocity);
        }
        return null;
    }
}
