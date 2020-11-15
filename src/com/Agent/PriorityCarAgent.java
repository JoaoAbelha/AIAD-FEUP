package com.Agent;

import com.Behaviour.PriorityCarMovement;
import com.Behaviour.PriorityCarSubscriptionInitiator;
import com.Data.Graph;
import com.Data.PriorityCar;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PriorityCarAgent extends AgentRegister {
    private Logger LOGGER = null;
    private Handler fileHandler;
    private final PriorityCar car;
    private final Graph city; // since all agents know all the city
    private PriorityCarSubscriptionInitiator subscriptionInitiator;

    public PriorityCarAgent(PriorityCar car, Graph city) {
        this.car = car;
        this.city = city;
        this.setupLogger();
    }

    private void setupLogger() {
        try {
            LOGGER = Logger.getLogger(this.car.getName());
            fileHandler = new FileHandler("logs/" + car.getName() + ".log");
            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    public PriorityCar getCar() {
        return car;
    }

    public Graph getCity() {
        return city;
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
        LOGGER.info("Agent Started");
        addBehaviour(new PriorityCarMovement(this, 300));
    }
}
