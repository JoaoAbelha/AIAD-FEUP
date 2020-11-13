package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;

public class PriorityRoadReceiver extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;
    private HashSet<String> priorityCars;
    private double minimumDistanceTravelled;

    public PriorityRoadReceiver(Agent a) {
        roadAgent = (RoadAgent) a;
        priorityCars = new HashSet<>();
        mt = MessageTemplate.MatchConversationId("PEOR");
        minimumDistanceTravelled = 0;
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            double distanceTravelled = Double.parseDouble(msg.getContent());

            if(!priorityCars.contains(msg.getSender().getLocalName())) {
                priorityCars.add(msg.getSender().getLocalName());
            } else if(distanceTravelled >= roadAgent.getRoadInfo().getDistance()) {
                priorityCars.remove(msg.getSender().getLocalName());
            }

            if(priorityCars.size() > 1) {
                minimumDistanceTravelled = Math.min(minimumDistanceTravelled, distanceTravelled);
                roadAgent.getManager().notifyAll("priority:" + minimumDistanceTravelled + ":" + roadAgent.getRoadInfo().getMaxVelocity());
            } else {
                minimumDistanceTravelled = distanceTravelled;
                roadAgent.getManager().notifyAll("priority:" + distanceTravelled + ":" + roadAgent.getRoadInfo().getMaxVelocity());
            }
        } else {
            block();
        }
    }
}
