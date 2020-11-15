package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class PriorityRoadReceiver extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;
    private HashMap<String, Double> priorityCars;
    private double minimumDistanceTravelled;

    public PriorityRoadReceiver(Agent a) {
        roadAgent = (RoadAgent) a;
        priorityCars = new HashMap<>();
        mt = MessageTemplate.MatchConversationId("PEOR");
        minimumDistanceTravelled = 0;
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            double distanceTravelled = Double.parseDouble(msg.getContent());
            priorityCars.put(msg.getSender().getLocalName(), distanceTravelled);
            Collection<Double> distancesTravelled = this.priorityCars.values();
            double min = Collections.min(distancesTravelled);
            roadAgent.getManager().notifyAll("priority:" + min + ":" + roadAgent.getRoadInfo().getMaxVelocity(),"road" + roadAgent.getRoadInfo().getStartNode() + "-" + roadAgent.getRoadInfo().getEndNode());

            if(distanceTravelled >= roadAgent.getRoadInfo().getDistance()) {
                roadAgent.getLOGGER().info("Priority car " + msg.getSender().getLocalName() + " reached end of road");
                priorityCars.remove(msg.getSender().getLocalName());
            }
        } else {
            block();
        }
    }
}
