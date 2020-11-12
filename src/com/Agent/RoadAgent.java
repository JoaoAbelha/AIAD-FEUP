package com.Agent;

import com.Behaviour.PreferenceListener;
import com.Behaviour.RoadNetResponder;
import com.Behaviour.RoadSubscriptionInitiator;
import com.Behaviour.RoadSubscriptionResponder;
import com.Data.Car;
import com.Data.RoadInfo;
import com.Manager.VelocitySubscriptionManager;
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
    //private HashMap<String, Car> carsThatWishToBe;
    private HashSet<String> carsWishToBe = new HashSet<>();
    private float spaceOccupied; // sum of the length of current cars
    private final float PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD = 15;
    VelocitySubscriptionManager manager;


    public RoadAgent(RoadInfo roadInfo) {

        this.roadInfo = roadInfo;
        this.currentCars = new HashSet<>();
        //this.carsThatWishToBe = new HashMap<>();
        this.spaceOccupied = 0;
        this.manager = new VelocitySubscriptionManager();
    }

    public RoadInfo getRoadInfo() {
        return roadInfo;
    }

    public void updateWishList(String carIdentifier, boolean add) {
        //this.carsThatWishToBe.put()
        if (add)
            carsWishToBe.add(carIdentifier);
        else
            carsWishToBe.remove(carIdentifier);

        System.out.println(">>>Cars preferred by the road updated size " + carsWishToBe.size());
    }
    
    public VelocitySubscriptionManager getManager() {
        return manager;
    }

    /*
    * todo: melhorar esta formula
    * */
    public int getUtility(String agent) {
        return - currentCars.size() + (carsWishToBe.contains(agent)  ? 50 : 0) + (int) (Math.random() * 10);
    }

    public boolean isRoadFull() {
        return spaceOccupied > this.roadInfo.getDistance() * (1 - PERCENTAGE_SPACE_BETWEEN_CARS_IN_A_ROAD/100.0) ;
    }


    @Override
    protected void setup() {
        register("road");
        addBehaviour(new PreferenceListener(this));
        System.out.println("Road agent started");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        addBehaviour(new RoadNetResponder(this, template));
        addBehaviour(new RoadSubscriptionResponder(this, this.manager));
        addBehaviour(new RoadSubscriptionInitiator(this, null));
    }
}
