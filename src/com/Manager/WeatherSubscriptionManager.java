package com.Manager;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import sajas.proto.SubscriptionResponder;
//import jade.proto.SubscriptionResponder;
import sajas.proto.SubscriptionResponder.Subscription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WeatherSubscriptionManager implements SubscriptionResponder.SubscriptionManager {
    private Map<String, Subscription> subscriptions = new HashMap<String, Subscription>();

    @Override
    public boolean register(Subscription s) throws RefuseException, NotUnderstoodException {
        subscriptions.put(s.getMessage().getConversationId(), s);
        return true;
    }

    @Override
    public boolean deregister(Subscription s) throws FailureException {
        subscriptions.remove(s.getMessage().getConversationId());
        return true;
    }

    public void notifyAll(float event) {
        Iterator<Subscription> i = subscriptions.values().iterator();
        while (i.hasNext()) {
            try {
                Subscription s = (Subscription) i.next();
                ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
                notification.setContentObject(event);
                s.notify(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
