package com.Actions;

import com.Agent.CityAgent;
import uchicago.src.sim.engine.BasicAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class ChangeWeather extends BasicAction {
    private double probability;
    private CityAgent city = null;

    public ChangeWeather(CityAgent cityAgent, double probability) {
        this.city = cityAgent;
        this.probability = probability;
    }

    @Override
    public void execute() {
        if(new Random().nextDouble() < probability) {
            System.out.println("changing weather");
            ArrayList<String> weathers = new ArrayList<>(city.getWeatherStation().getVelocities().keySet());
            Collections.shuffle(weathers);
            String weather = weathers.get(0);
            city.getLOGGER().info("Weather change to " + weather+". Notifying all subscribers" );
            city.getWeatherStation().setCurrentWeather(weather);
            float velocity = city.getWeatherStation().getVelocity(weather);
            for(Map.Entry<String, Double> entry: city.getMaxVelocity().entrySet()) {
                entry.setValue(entry.getValue() * velocity);
            }
            city.getManager().notifyAll(velocity);
        }
    }
}
