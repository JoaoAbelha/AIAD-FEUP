package com.Behaviour;

import com.Agent.CityAgent;
import jade.core.behaviours.TickerBehaviour;

public class CityWeatherChange extends TickerBehaviour{
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
            float velocity = city.getWeatherStation().getVelocity(weather);
            city.getManager().notifyAll(velocity);
        }
    }

}
