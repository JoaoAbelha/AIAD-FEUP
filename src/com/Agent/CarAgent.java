package com.Agent;

import com.Behaviour.CarNetInitiator;
import com.Data.Car;
import com.Data.Graph;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CarAgent extends AgentRegister {

    private DFAgentDescription dfd;
    private final Car car;
    private final Graph city; // since all agents know all the city


    public CarAgent(Car car, Graph city) {
        this.city = city;
        this.car = car;
    }

    @Override
    protected void setup() {
        register(car.getName());
        System.out.println("Car agent started");
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
        message.setContent("road-value?");
        Map<Integer, RoadInfo> adjacentRoads = this.city.getAdjacent(car.getCurrentNode());

        adjacentRoads.forEach((followingNode, roadInfo) -> {
           message.addReceiver(new AID( "road"+ car.getCurrentNode() + followingNode, false));
        });


        // todo dfSearch to find the roads registered that we want and pass the number of agents
        int nrAgents = adjacentRoads.size();
        addBehaviour(new CarNetInitiator(this, message, nrAgents));
    }
}
