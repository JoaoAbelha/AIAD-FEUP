package com.Agent;

import com.Behaviour.CarNetInitiator;
import com.Data.Car;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.Date;

public class CarAgent extends AgentRegister {

    private DFAgentDescription dfd;
    private final Car car;


    public CarAgent(Car car) {
        this.car = car;
    }

    @Override
    protected void setup() {
        register(car.getName());
        System.out.println("Car agent started");
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
        message.setContent("what-best-road-value?");
        ArrayList<String> ids = new ArrayList<>();
        ids.add("road12");
        ids.add("road13");
        for(int i = 0 ; i < ids.size(); i++) {
            message.addReceiver(new AID(ids.get(i), false));
        }
        // todo dfSearch to find the roads registered that we want and pass the number of agents
        int nrAgents = ids.size();
        addBehaviour(new CarNetInitiator(this, message, nrAgents));
    }
}
