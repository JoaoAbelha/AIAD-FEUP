package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.Car;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class PriorityCarMovement extends TickerBehaviour {
    private PriorityCarAgent priorityCarAgent = null;
    private long time;

    public PriorityCarMovement(PriorityCarAgent priorityCarAgent, long time) {
        super(priorityCarAgent, time);
        this.time = time;
        this.priorityCarAgent = priorityCarAgent;
    }

    @Override
    protected void onTick() {
        if(priorityCarAgent.getCar().getCurrentNode() == priorityCarAgent.getCar().getDestNode()) {
            System.out.println("Dest node pc");
            priorityCarAgent.unregister();
            return;
        }

        if(priorityCarAgent.getCar().getStatus().equals(Car.Status.ROAD) && priorityCarAgent.getCar().getCurrentRoad() != null) {
            if(priorityCarAgent.getCar().getCurrentDistanceTravelled() >= priorityCarAgent.getCar().getCurrentRoad().getDistance()) {
                this.handleEndOfRoad();
            } else {
                this.handleMovement();
            }
        } else if(priorityCarAgent.getCar().getStatus().equals(Car.Status.INTERSECTION)) {
            this.handleIntersection();
        }
    }

    private void handleIntersection() {
        this.priorityCarAgent.addBehaviour(new SendInformPriorityCar(this.priorityCarAgent));
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(priorityCarAgent.getCar().getCurrentVelocity());
        priorityCarAgent.getCar().addDistanceTravelled(distanceTravelled);
        informDistanceTravelled();
        //this.priorityCarAgent.addBehaviour(new PriorityRoadInform(this.priorityCarAgent, this.priorityCarAgent.getCar().getCurrentRoad()));
    }

    private void handleEndOfRoad() {
        priorityCarAgent.getCar().setStatus(Car.Status.INTERSECTION);
        this.priorityCarAgent.getCar().updateCurrentNode();
        this.priorityCarAgent.getSubscriptionInitiator().cancelInform();
    }

    private double kmph_to_mps(double kmph) {
        return (0.277778 * kmph);
    }

    private void informDistanceTravelled() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        RoadInfo roadInfo = priorityCarAgent.getCar().getCurrentRoad();
        msg.addReceiver(new AID("road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode(), false));
        msg.setConversationId("PEOR");
        msg.setContent(String.valueOf(priorityCarAgent.getCar().getCurrentDistanceTravelled()));
        myAgent.send(msg);
    }
}

