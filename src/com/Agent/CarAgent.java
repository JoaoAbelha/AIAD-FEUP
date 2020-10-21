package com.Agent;

import com.Behaviour.CarNetInitiator;
import com.Data.Car;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Date;

public class CarAgent extends AgentRegister {

    private String name;
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
        message.setContent("what-best-road-value");
        // todo dfSearch to find the roads registered that we want and pass the number of agents
        int nrAgents = 0;
        addBehaviour(new CarNetInitiator(this, message, nrAgents));
    }
}
