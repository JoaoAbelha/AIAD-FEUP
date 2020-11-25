package com.Agent;

import com.Behaviour.CarMovement;
import com.Behaviour.CarSubscriptionInitiator;
import com.Data.Car;
import com.Data.PathResponse;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CarAgent extends AgentRegister {
    private Logger LOGGER = null;
    private Handler fileHandler;
    private DFAgentDescription dfd;
    private final Car car;
    private CarSubscriptionInitiator subscriptionInitiator;
    private PathResponse currentPathResponse;

    public CarAgent(Car car) {
        this.car = car;
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
        LOGGER.info("Agent Started");
        addBehaviour(new CarMovement(this, 300));
    }
}
