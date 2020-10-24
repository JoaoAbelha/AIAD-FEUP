package com.Agent;

import com.Behaviour.RoadNetResponder;
import com.Data.Car;
import com.Data.RoadInfo;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.HashSet;

public class RoadAgent  extends AgentRegister {
    private RoadInfo roadInfo;
    private DFAgentDescription dfd;
    private HashSet<Car> currentCars; // current cars in the road
    private HashMap<String, Car> carsThatWishToBe;
    private float spaceOccupied; // sum of the length of current cars
    private final float PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD = 15;

    public RoadAgent(RoadInfo roadInfo) {

        this.roadInfo = roadInfo;
        this.currentCars = new HashSet<>();
        this.carsThatWishToBe = new HashMap<>();
        this.spaceOccupied = 0;
    }

    /*
    * todo: melhorar esta formula
    * */
    public int getUtility(String agent) {
        return - currentCars.size() + (carsThatWishToBe.get(agent) != null ? 50 : 0) + (int) (Math.random() * 10);
    }

    public boolean isRoadFull() {
        return spaceOccupied > this.roadInfo.getDistance() * (1 - PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD/100.0) ;
    }


    @Override
    protected void setup() {
        register("road");
        System.out.println("Road agent started");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        addBehaviour(new RoadNetResponder(this, template));
    }
}
