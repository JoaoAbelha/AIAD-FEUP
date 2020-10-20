package com.Behaviour;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class RoadSubscriptionInitiator extends SubscriptionInitiator {

    public RoadSubscriptionInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }
}
