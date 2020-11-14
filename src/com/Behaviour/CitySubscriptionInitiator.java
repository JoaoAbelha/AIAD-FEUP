package com.Behaviour;

import com.Agent.CityAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class CitySubscriptionInitiator extends SubscriptionInitiator {
    CityAgent city;

    public CitySubscriptionInitiator(CityAgent city, ACLMessage msg) {
        super(city, msg);
        this.city = city;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        for(String road: city.getRoads())
            subscription.addReceiver(new AID(road, AID.ISLOCALNAME));
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
        Vector l = new Vector(1);
        l.addElement(subscription);
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
        String content = inform.getContent();
        String[] contentArray = content.split(":");
        if(contentArray.length != 2 || !contentArray[0].equals("traffic"))
            return;
        city.updateRoadMaxVelocity(inform.getSender().getLocalName(), Double.parseDouble(contentArray[1]));
    }
}
