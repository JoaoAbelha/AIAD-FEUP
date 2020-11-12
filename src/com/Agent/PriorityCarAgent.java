package com.Agent;

import com.Behaviour.PriorityCarMovement;
import com.Behaviour.PriorityCarSubscriptionInitiator;
import com.Data.Graph;
import com.Data.PriorityCar;

public class PriorityCarAgent extends AgentRegister {

    private final PriorityCar car;
    private final Graph city; // since all agents know all the city
    private PriorityCarSubscriptionInitiator subscriptionInitiator;

    public PriorityCarAgent(PriorityCar car, Graph city) {
        this.car = car;
        this.city = city;
    }

    public PriorityCar getCar() {
        return car;
    }

    public PriorityCarSubscriptionInitiator getSubscriptionInitiator() {
        return subscriptionInitiator;
    }

    public void updateSubscriptionInitiator() {
        subscriptionInitiator = new PriorityCarSubscriptionInitiator(this,null);
        addBehaviour(subscriptionInitiator);
    }

    @Override
    protected void setup() {
        register(car.getName());
        car.calculateCarPath(city);
        System.out.println("priority car created");
        addBehaviour(new PriorityCarMovement(this, 300));
        this.subscriptionInitiator = new PriorityCarSubscriptionInitiator(this, null);
        addBehaviour(this.subscriptionInitiator);
    }
}
