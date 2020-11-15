package com.Manager;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VelocitySubscriptionManager implements SubscriptionResponder.SubscriptionManager {
    private Map<String, SubscriptionResponder.Subscription> subscriptions = new HashMap<String, SubscriptionResponder.Subscription>();

    @Override
    public boolean register(SubscriptionResponder.Subscription s) throws RefuseException, NotUnderstoodException {
        subscriptions.put(s.getMessage().getConversationId(), s);
        return true;
    }

    @Override
    public boolean deregister(SubscriptionResponder.Subscription s) throws FailureException {
        subscriptions.remove(s.getMessage().getConversationId());
        return true;
    }

    public void notifyAll(String content, String roadName) {
        Iterator<SubscriptionResponder.Subscription> i = subscriptions.values().iterator();
        while (i.hasNext()) {
            SubscriptionResponder.Subscription s = (SubscriptionResponder.Subscription) i.next();
            ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
            notification.setContent(content);
            s.notify(notification);
            System.out.println(roadName + " notify: " + content + ":" + s.getMessage().getSender().getLocalName());
        }
    }
}