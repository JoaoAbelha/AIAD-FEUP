package com.Behaviour;

import com.Agent.CarAgent;
import com.Agent.PriorityCarAgent;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

import java.util.ArrayList;
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
        this.responderName = "road" + src + "-" + dest;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscription.addReceiver(new AID((responderName), AID.ISLOCALNAME));
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
        Vector l = new Vector(1);
        l.addElement(subscription);
        priorityCar.getLOGGER().info("Starting velocity subscription with " + responderName);

        return l;
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        priorityCar.getLOGGER().info("Velocity subscription with " + responderName + " accepted");
        super.handleAgree(agree);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        priorityCar.getLOGGER().info("Velocity subscription with " + responderName + " refused");
        super.handleRefuse(refuse);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        if (cancel_inform) {
            priorityCar.getLOGGER().info("Subscription with " + responderName +" cancelled successfully");
            cancellationCompleted(inform.getSender());
            priorityCar.removeBehaviour(priorityCar.getSubscriptionInitiator());
        } else {
            String content = inform.getContent();
            String[] contentArray = content.split(":");
            if(contentArray.length != 2) return;
            double maxVelocity = Double.parseDouble(contentArray[1]);
            priorityCar.getLOGGER().info("Notification with velocity change in " + responderName +". Setting velocity to " + maxVelocity);
            priorityCar.getCar().setCurrentVelocity(maxVelocity);
        }
    }

    public void cancelInform() {
        priorityCar.getLOGGER().info("Canceling subscription with " + responderName);
        cancel(new AID((responderName), AID.ISLOCALNAME), false);
        this.cancel_inform = true;
    }
}
