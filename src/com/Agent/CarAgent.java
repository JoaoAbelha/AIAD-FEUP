package com.Agent;

import com.Behaviour.CarMovement;
import com.Behaviour.CarSubscriptionInitiator;
import com.Data.Car;
import com.Data.Graph;
import com.Data.PathRequest;
import com.Data.PathResponse;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarAgent extends AgentRegister {
    private DFAgentDescription dfd;
    private final Car car;
    private CarSubscriptionInitiator subscriptionInitiator;
    private PathResponse currentPathResponse;

    public CarAgent(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public CarSubscriptionInitiator getSubscriptionInitiator() {
        return subscriptionInitiator;
    }

    public PathResponse getCurrentPathResponse() {
        return currentPathResponse;
    }

    public void updateSubscriptionInitiator() {
        subscriptionInitiator = new CarSubscriptionInitiator(this, null);
        addBehaviour(subscriptionInitiator);
    }

    public void updatePathResponse(PathResponse pathResponse) {
        this.currentPathResponse = pathResponse;
    }

    @Override
    protected void setup() {
        register(car.getName());
        System.out.println("Car agent started");
        addBehaviour(new CarMovement(this, 300));
    }
}
