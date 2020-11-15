package com.Behaviour;

import com.Agent.RoadAgent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

public class RoadSubscriptionResponder extends SubscriptionResponder {
    RoadAgent road;

    public RoadSubscriptionResponder(RoadAgent road, SubscriptionManager sm) {
        super(road, SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE), sm);
        this.road = road;
    }

    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException, RefuseException {
        ACLMessage decision_result = subscription.createReply();
        try {
            super.handleSubscription(subscription);
            decision_result.setPerformative(ACLMessage.AGREE);
            road.getLOGGER().info("Received valid subscription from " + subscription.getSender().getLocalName() + ". Sending Agree" );
        } catch (RefuseException e) {
            decision_result.setPerformative(ACLMessage.REFUSE);
            road.getLOGGER().info("Received invalid subscription from " + subscription.getSender().getLocalName() + ". Sending Refuse" );
        } catch (NotUnderstoodException e) {
            decision_result.setPerformative(ACLMessage.NOT_UNDERSTOOD);
            road.getLOGGER().info("Received invalid subscription from " + subscription.getSender().getLocalName() + ". Sending Not Understood" );
        }
        return decision_result;
    }

    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        super.handleCancel(cancel);
        road.getLOGGER().info(cancel.getSender().getLocalName() + " canceled its subscription" );
        ACLMessage cancel_response = cancel.createReply();
        cancel_response.setPerformative(ACLMessage.INFORM);
        return cancel_response;
    }
}
