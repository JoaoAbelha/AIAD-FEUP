package com.Behaviour;

import com.Agent.CarAgent;
import com.Agent.PriorityCarAgent;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class PriorityCarSubscriptionInitiator extends SubscriptionInitiator {
    private PriorityCarAgent priorityCar;
    private boolean cancel_inform;
    private String responderName;

    public PriorityCarSubscriptionInitiator(PriorityCarAgent priorityCar, ACLMessage msg) {
        super(priorityCar, msg);
        this.priorityCar = priorityCar;
        this.cancel_inform = false;
        int src = priorityCar.getCar().getCurrentRoad().getStartNode();
        int dest = priorityCar.getCar().getCurrentRoad().getEndNode();
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
            priorityCar.removeBehaviour(priorityCar.getSubscriptionInitiator());
        } else {
            try {
                double maxVelocity = (double) inform.getContentObject();
                priorityCar.getCar().setCurrentVelocity(maxVelocity);
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