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
    private boolean ignorePriority;
    private boolean ignoreWeather;

    public CarSubscriptionInitiator(CarAgent car, ACLMessage msg) {
        super(car, msg);
        this.car = car;
        this.cancel_inform = false;
        int src = car.getCar().getCurrentRoad().getStartNode();
        int dest = car.getCar().getCurrentRoad().getEndNode();
        this.responderName = "road" + src + dest;
        this.ignorePriority = false;
        this.ignoreWeather = false;
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
            String content = inform.getContent();
            String[] contentArray = content.split(":");
            if(contentArray.length == 2 || contentArray.length == 3) {
                switch (contentArray[0]) {
                    case "priority":
                        if(car.getCar().getCurrentDistanceTravelled() >= Double.parseDouble(contentArray[1])) {
                            car.getCar().setCurrentVelocity(0);
                            ignorePriority = false;
                            ignoreWeather = true;
                        } else if(!ignorePriority){
                            //System.out.println("priority car passed");
                            car.getCar().setCurrentVelocity(Double.parseDouble(contentArray[2]));
                            ignorePriority = true;
                            ignoreWeather = false;
                        }
                        break;
                    case "weather":
                        if(ignoreWeather) return;
                        double maxVelocity = Double.parseDouble(contentArray[1]);
                        System.out.println(car.getCar().getName() + ": " + maxVelocity);
                        car.getCar().setCurrentVelocity(maxVelocity);
                        break;
                }
            }
        }
    }

    public void cancelInform() {
        cancel(new AID((responderName), AID.ISLOCALNAME), false);
        this.cancel_inform = true;
    }
}
