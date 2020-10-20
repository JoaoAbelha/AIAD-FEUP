package com.Manager;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.proto.SubscriptionResponder;

public class WeatherSubscriptionManager implements SubscriptionResponder.SubscriptionManager {
    @Override
    public boolean register(SubscriptionResponder.Subscription subscription) throws RefuseException, NotUnderstoodException {
        return true;
    }

    @Override
    public boolean deregister(SubscriptionResponder.Subscription subscription) throws FailureException {
        return true;
    }
}
