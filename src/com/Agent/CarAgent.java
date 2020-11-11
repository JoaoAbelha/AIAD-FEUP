package com.Agent;

import com.Behaviour.CarMovement;
import com.Data.Car;
import com.Data.Graph;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarAgent extends AgentRegister {

    private DFAgentDescription dfd;
    private final Car car;
    private final Graph city; // since all agents know all the city


    public CarAgent(Car car, Graph city) {
        this.car = car;
        this.city = city;
    }

    public Car getCar() {
        return car;
    }

    public Graph getCity() {
        return city;
    }

    @Override
    protected void setup() {
        register(car.getName());
        car.calculateCarPath(this.city);
        System.out.println("Car agent started");
        addBehaviour(new CarMovement(this, 300));
    }


}
