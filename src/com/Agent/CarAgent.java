package com.Agent;

import com.Behaviour.CarMovement;
import com.Behaviour.CarSubscriptionInitiator;
import com.Data.Car;
import com.Data.Graph;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarAgent extends AgentRegister {

    private DFAgentDescription dfd;
    private final Car car;
    private final Graph city; // since all agents know all the city
    private CarSubscriptionInitiator subscriptionInitiator;

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

    public CarSubscriptionInitiator getSubscriptionInitiator() {
        return subscriptionInitiator;
    }

    public void updateSubscriptionInitiator() {
        subscriptionInitiator = new CarSubscriptionInitiator(this, null);
        addBehaviour(subscriptionInitiator);
    }

    @Override
    protected void setup() {
        register(car.getName());
        car.calculateCarPath(this.city);
        System.out.println("Car agent started");
        addBehaviour(new CarMovement(this, 300));
        subscriptionInitiator = new CarSubscriptionInitiator(this, null);
        addBehaviour(subscriptionInitiator);
    }
}
