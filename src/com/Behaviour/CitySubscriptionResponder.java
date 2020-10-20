package com.Behaviour;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

public class CitySubscriptionResponder extends SubscriptionResponder {

    public CitySubscriptionResponder(Agent a, MessageTemplate mt, SubscriptionManager sm) {
        super(a, mt, sm);
    }

    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription) throws NotUnderstoodException, RefuseException {
        ACLMessage decision_result = subscription.createReply();
        try {
            super.handleSubscription(subscription);
            //System.out.println("subcriber added");
            decision_result.setPerformative(ACLMessage.AGREE);
        } catch (RefuseException e) {
            decision_result.setPerformative(ACLMessage.REFUSE);
        } catch (NotUnderstoodException e) {
            decision_result.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        }
        return decision_result;
    }

    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        super.handleCancel(cancel);
        //System.out.println("subscriber removed");
        ACLMessage cancel_response = cancel.createReply();
        cancel_response.setPerformative(ACLMessage.INFORM);
        return cancel_response;
    }
}
