package com.Behaviour;

import com.Agent.CityAgent;
import sajas.core.behaviours.TickerBehaviour;
//import jade.core.behaviours.TickerBehaviour;

import java.util.Map;

public class CityWeatherChange extends TickerBehaviour {
    private CityAgent city = null;
    private int time;

    public CityWeatherChange(CityAgent city, long time) {
        super(city, time);
        this.city = city;
        this.time = 0;
    }

    @Override
    protected void onTick() {
        time++;
        if(city.getWeatherStation().existsWeather(time)) {
            String weather = city.getWeatherStation().getWeather(time);
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
