package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PriorityEndOfRoadReceiver extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;

    public PriorityEndOfRoadReceiver(Agent a) {
        roadAgent = (RoadAgent) a;
        mt = MessageTemplate.MatchConversationId("PEOR");
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            roadAgent.getManager().notifyAll("weather:" + roadAgent.getRoadInfo().getMaxVelocity());
            System.out.println("removed car " + msg.getSender().getLocalName() + " - size: " + roadAgent.getCurrentCars().size());
        } else {
            block();
        }
    }
}
