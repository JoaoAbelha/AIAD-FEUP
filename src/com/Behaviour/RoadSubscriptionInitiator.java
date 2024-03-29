package com.Behaviour;

import com.Agent.RoadAgent;
//import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.core.AID;
import sajas.proto.SubscriptionInitiator;
//import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class RoadSubscriptionInitiator extends SubscriptionInitiator {
    private RoadAgent road;

    public RoadSubscriptionInitiator(RoadAgent road, ACLMessage msg) {
        super(road, msg);
        this.road = road;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscription.addReceiver(new sajas.core.AID("city", AID.ISLOCALNAME));
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
        Vector l = new Vector(1);
        l.addElement(subscription);
        road.getLOGGER().info("Starting weather subscription with city");

        return l;
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        road.getLOGGER().info("Velocity subscription with city accepted");
        super.handleAgree(agree);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        road.getLOGGER().info("Velocity subscription with city refused");
        super.handleRefuse(refuse);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            float velocity = (float) inform.getContentObject();
            double roadVelocity = road.getRoadInfo().getRoadInitialVelocity();
            road.getRoadInfo().setMaxVelocity(velocity * roadVelocity);
            road.getLOGGER().info("Notification with weather change. Sending notification to all cars in the road");
            road.getManager().notifyAll("weather:" + road.getRoadInfo().getMaxVelocity(), "road" + road.getRoadInfo().getStartNode() + "-" + road.getRoadInfo().getEndNode());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
