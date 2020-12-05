package com.Behaviour;

import com.Agent.CarAgent;
//import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.core.AID;
import sajas.proto.SubscriptionInitiator;
//import jade.proto.SubscriptionInitiator;

import java.util.Vector;

public class CarSubscriptionInitiator extends SubscriptionInitiator {
    private CarAgent car;
    private boolean cancel_inform;
    private String responderName;
    private boolean isBehind;
    private boolean ignorePriority;
    private boolean ignoreRest;

    public CarSubscriptionInitiator(CarAgent car, ACLMessage msg) {
        super(car, msg);
        this.car = car;
        this.cancel_inform = false;
        int src = car.getCar().getCurrentRoad().getStartNode();
        int dest = car.getCar().getCurrentRoad().getEndNode();
        this.responderName = "road" + src + "-" + dest;
        this.isBehind = true;
        this.ignorePriority = false;
        this.ignoreRest = false;
    }

    @Override
    protected Vector prepareSubscriptions(ACLMessage subscription) {
        subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
        subscription.addReceiver(new sajas.core.AID((responderName), AID.ISLOCALNAME));
        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
        Vector l = new Vector(1);
        l.addElement(subscription);
        car.getLOGGER().info("Starting velocity subscription with " + responderName);
        return l;
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
        car.getLOGGER().info("Velocity subscription with " + responderName + " accepted");
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        super.handleRefuse(refuse);
        car.getLOGGER().info("Velocity subscription with " + responderName + " refused");

    }

    @Override
    protected void handleInform(ACLMessage inform) {
        if (cancel_inform) {
            car.getLOGGER().info("Subscription with " + responderName +" cancelled successfully");
            cancellationCompleted(inform.getSender());
            car.removeBehaviour(car.getSubscriptionInitiator());
        } else {
            String content = inform.getContent();
            String[] contentArray = content.split(":");
            if(contentArray.length == 2 || contentArray.length == 3) {
                switch (contentArray[0]) {
                    case "priority":
                        if(!isBehind && !ignorePriority) {
                            car.getLOGGER().info("Priority car in front of me on road " + responderName + ". Moving again");
                            car.getCar().setCurrentVelocity(Double.parseDouble(contentArray[2]));
                            ignoreRest = false;
                            ignorePriority = true;
                        } else if(car.getCar().getCurrentDistanceTravelled() >= Double.parseDouble(contentArray[1])) {
                            car.getLOGGER().info("Priority car behind me on road " + responderName + ". Stopping.");
                            car.getCar().setCurrentVelocity(0);
                            ignorePriority = false;
                            ignoreRest = true;
                            isBehind = true;
                        } else {
                            isBehind = false;
                        }
                        break;
                    default:
                        if(ignoreRest) return;
                        double maxVelocity = Double.parseDouble(contentArray[1]);
                        car.getCar().setCurrentVelocity(maxVelocity);
                        car.getLOGGER().info("Notification with velocity change in " + responderName +". Setting velocity to " + maxVelocity);
                        break;
                }
            }
        }
    }

    public void cancelInform() {
        car.getLOGGER().info("Canceling subscription with " + responderName);
        cancel(new sajas.core.AID((responderName), AID.ISLOCALNAME), false);
        this.cancel_inform = true;
    }
}
