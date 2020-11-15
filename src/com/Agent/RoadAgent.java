package com.Agent;

import com.Behaviour.*;
import com.Data.Car;
import com.Data.RoadInfo;
import com.Manager.VelocitySubscriptionManager;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RoadAgent  extends AgentRegister {
    private static Logger LOGGER = null;
    private Handler fileHandler;
    private RoadInfo roadInfo;
    private DFAgentDescription dfd;
    private HashSet<String> currentCars; // current cars in the road
    private double spaceOccupied; // sum of the length of current cars
    private final float PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD = 15;
    VelocitySubscriptionManager manager;


    public RoadAgent(RoadInfo roadInfo) {
        this.roadInfo = roadInfo;
        this.currentCars = new HashSet<>();
        this.spaceOccupied = 0;
        this.manager = new VelocitySubscriptionManager();
        this.setupLogger();
    }

    private void setupLogger() {
        try {
            LOGGER = Logger.getLogger("road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode());
            fileHandler = new FileHandler("logs/" + "road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode() + ".log");
            LOGGER.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public RoadInfo getRoadInfo() {
        return roadInfo;
    }

    public HashSet<String> getCurrentCars() {
        return currentCars;
    }
    
    public VelocitySubscriptionManager getManager() {
        return manager;
    }

    public double getUtility(Car.Strategy strategy) {
        double penalty = this.spaceOccupied / this.getRoadInfo().getDistance();
        switch (strategy) {
            case SHORTEST_PATH:
                return roadInfo.getDistance() * (1 + penalty);
            case SHORTEST_TIME:
                double estimatedTime = this.roadInfo.getDistance() * 1.0 / this.roadInfo.getMaxVelocity();
                return estimatedTime * (1 + penalty);
            default:
                return penalty;
        }
    }

    public boolean isRoadFull(Double length) {
        return spaceOccupied + length > this.roadInfo.getDistance() * (1 - PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD/100.0);
    }

    public double getSpaceOccupied() {
        return spaceOccupied;
    }

    @Override
    protected void setup() {
        register("road");
        LOGGER.info("Agent Started");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        MessageTemplate request_template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );

        addBehaviour(new RoadNetResponder(this, template));
        addBehaviour(new RoadSubscriptionResponder(this, this.manager));
        addBehaviour(new RoadSubscriptionInitiator(this, null));
        addBehaviour(new RoadRequestResponder(this, request_template));
        addBehaviour(new EndOfRoadReceiver(this));
        addBehaviour(new PriorityRoadReceiver(this));
    }

    public void updateCars(String carName, Double length, boolean add) {
        System.out.println("Space occupied:" +  this.spaceOccupied + " out of " + this.roadInfo.getDistance());
        if(add) {
            this.currentCars.add(carName);
            this.spaceOccupied += length;
        } else {
            this.currentCars.remove(carName);
            this.spaceOccupied -= length;
        }
    }
}
