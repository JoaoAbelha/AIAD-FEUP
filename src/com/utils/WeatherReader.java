package com.utils;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class WeatherReader extends FileReader {

    final int WEATHER_TIME = 0;
    final int WEATHER_TYPE = 1;

    private final HashMap<Integer, String> weather = new HashMap<>();

    public WeatherReader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readLine(String[] line) {
        weather.put(Integer.parseInt(line[WEATHER_TIME]), line[WEATHER_TYPE]);
    }

    @Override
    public HashMap<Integer, String> getInfo() {
        return weather;
    }
}
