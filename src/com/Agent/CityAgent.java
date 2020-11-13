package com.Agent;


import com.Behaviour.CitySubscriptionResponder;
import com.Behaviour.CityWeatherChange;
import com.Data.Graph;
import com.Data.RoadInfo;
import com.Data.WeatherStation;
import com.Manager.WeatherSubscriptionManager;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;
import java.util.Map;

public class CityAgent extends AgentRegister {
    WeatherSubscriptionManager manager;
    WeatherStation weatherStation;
    HashMap<String, Double> maxVelocity = new HashMap<>(); // p.e road23 ->50
    HashMap<String, Integer> distances = new HashMap<>() ; // p.e road32 -> 100

    /**
     * util functions to use after the object has been created
     */
    private void distanceCal() {
        // todo
    }

    private void intersectionsCal() {
        // todo
    }


    public CityAgent(WeatherStation weatherStation, Graph city) {
        this.manager = new WeatherSubscriptionManager();
        this.weatherStation = weatherStation;
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : city.getEdges().entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadInfo roadInfo = adj.getValue();
                System.out.println(src + " " + dest);
                this.maxVelocity.put("road" + src + dest, roadInfo.getMaxVelocity());
                this.distances.put("road" + src + dest, roadInfo.getDistance());
            }
        }
        distanceCal();
        intersectionsCal();
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
        //addBehaviour(new CitySubscriptionResponder(this, this.manager));
        //addBehaviour(new CityWeatherChange(this, 300));
    }
}
