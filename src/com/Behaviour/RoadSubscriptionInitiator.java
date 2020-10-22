package com.Behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class RoadSubscriptionInitiator extends SubscriptionInitiator {

    public RoadSubscriptionInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscription.addReceiver(new AID(("responder"), AID.ISLOCALNAME));
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
        Vector l = new Vector(1);
        l.addElement(subscription);
        //System.out.println("subscription started");
        return l;
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        super.handleRefuse(refuse);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        int weather = Integer.valueOf(inform.getContent());
        System.out.println("current weather is - " + weather);
    }
}
