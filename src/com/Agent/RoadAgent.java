package com.Agent;

import com.Behaviour.RoadNetResponder;
import com.Data.Car;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.HashSet;

public class RoadAgent  extends AgentRegister {
    private int distance;
    private int velocity;
    private DFAgentDescription dfd;
    private HashSet<Car> currentCars; // current cars in the road
    private HashMap<String, Car> carsThatWishToBe;

    public RoadAgent(int velocity, int distance) {
        this.distance = distance;
        this.velocity = velocity;
        this.currentCars = new HashSet<>();
        this.carsThatWishToBe = new HashMap<>();
    }

    /*
    * todo: melhorar esta formula
    * */
    public int getUtility(String agent) {
        return - currentCars.size() + (carsThatWishToBe.get(agent) != null ? 50 : 0) + (int) (Math.random() * 10);

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
