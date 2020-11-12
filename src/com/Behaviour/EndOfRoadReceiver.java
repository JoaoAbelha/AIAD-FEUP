package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class EndOfRoadReceiver extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;

    public EndOfRoadReceiver(Agent a) {
        roadAgent = (RoadAgent) a;
        mt = MessageTemplate.MatchConversationId("EOR");
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            roadAgent.getCurrentCars().remove(msg.getSender().getLocalName());
            System.out.println("removed car " + msg.getSender().getLocalName() + " - size: " + roadAgent.getCurrentCars().size());
        } else {
            block();
        }
    }
}

