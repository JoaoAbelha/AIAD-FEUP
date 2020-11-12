package com.Agent;

import com.Data.Car;
import com.Data.Graph;
import com.Data.PriorityCar;

public class PriorityCarAgent extends AgentRegister {

    private final PriorityCar car;
    private final Graph city; // since all agents know all the city

    public PriorityCarAgent(PriorityCar car, Graph city) {
        this.car = car;
        this.city = city;
    }

    @Override
    protected void setup() {
        register(car.getName());
        car.calculateCarPath(city);
        System.out.println("priority car created");
    }
}
