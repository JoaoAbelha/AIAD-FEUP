package com.Agent;


import com.Behaviour.CitySubscriptionResponder;
import com.Behaviour.CityWeatherChange;
import com.Data.WeatherStation;
import com.Manager.WeatherSubscriptionManager;

public class CityAgent extends AgentRegister {
    WeatherSubscriptionManager manager;
    WeatherStation weatherStation;

    public CityAgent(WeatherStation weatherStation) {
        this.manager = new WeatherSubscriptionManager();
        this.weatherStation = weatherStation;
    }

    public WeatherStation getWeatherStation() {
        return weatherStation;
    }

    public WeatherSubscriptionManager getManager() {
        return manager;
    }

    @Override
    protected void setup() {
        register("weather-forecast");
        System.out.println("city agent");
        addBehaviour(new CitySubscriptionResponder(this, this.manager));
        addBehaviour(new CityWeatherChange(this, 300));
    }
}
