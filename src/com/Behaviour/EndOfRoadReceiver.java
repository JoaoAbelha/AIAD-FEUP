package com.Behaviour;

import com.Agent.RoadAgent;
//import jade.core.Agent;
//import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

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
            roadAgent.updateCars(msg.getSender().getLocalName(), Double.valueOf(msg.getContent()), false);
            roadAgent.getLOGGER().info(msg.getSender().getLocalName() + " left the road" + ":" + roadAgent.getSpaceOccupied());
        } else {
            block();
        }
    }
}

