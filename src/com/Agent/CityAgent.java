package com.Agent;

public class CityAgent extends AgentRegister {

    @Override
    protected void setup() {
        register("weather-forecast");
        System.out.println("city agent");
    }
}
