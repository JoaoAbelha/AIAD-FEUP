package com.Agent;

import com.Behaviour.*;
import com.Data.Car;
import com.Data.RoadInfo;
import com.Manager.VelocitySubscriptionManager;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;

public class RoadAgent  extends AgentRegister {
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
        //addBehaviour(new PreferenceListener(this));
        System.out.println("Road agent started");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        addBehaviour(new RoadNetResponder(this, template));
        addBehaviour(new RoadSubscriptionResponder(this, this.manager));
        addBehaviour(new RoadSubscriptionInitiator(this, null));
        addBehaviour(new ReceiveInformPriorityCar(this));
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
