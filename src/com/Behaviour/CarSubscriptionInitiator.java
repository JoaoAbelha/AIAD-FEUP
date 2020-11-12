package com.Behaviour;

import com.Agent.CarAgent;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class CarSubscriptionInitiator extends SubscriptionInitiator {
    private CarAgent car;
    private boolean cancel_inform;
    private String responderName;

    public CarSubscriptionInitiator(CarAgent car, ACLMessage msg) {
        super(car, msg);
        this.car = car;
        this.cancel_inform = false;
        int src = car.getCar().getCurrentRoad().getStartNode();
        int dest = car.getCar().getCurrentRoad().getEndNode();
        this.responderName = "road" + src + dest;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscription.addReceiver(new AID((responderName), AID.ISLOCALNAME));
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
        if (cancel_inform) {
            cancellationCompleted(inform.getSender());
            car.removeBehaviour(car.getSubscriptionInitiator());
        } else {
            try {
                double maxVelocity = (double) inform.getContentObject();
                car.getCar().setCurrentVelocity(maxVelocity);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelInform() {
        cancel(new AID((responderName), AID.ISLOCALNAME), false);
        this.cancel_inform = true;
    }
}
