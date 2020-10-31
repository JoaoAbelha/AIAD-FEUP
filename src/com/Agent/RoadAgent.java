package com.Agent;

import com.Behaviour.RoadNetResponder;
import com.Data.RoadInfo;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RoadAgent  extends AgentRegister {
    private RoadInfo road;
    private DFAgentDescription dfd;

    public RoadAgent(RoadInfo roadInfo) {
        this.road = roadInfo;
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
