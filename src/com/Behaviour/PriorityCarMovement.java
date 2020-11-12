package com.Behaviour;

import com.Agent.PriorityCarAgent;
import jade.core.behaviours.TickerBehaviour;

public class PriorityCarMovement extends TickerBehaviour {
    public enum Status {ROAD, INTERSECTION};

    private PriorityCarAgent priorityCarAgent = null;
    private Status carStatus = Status.ROAD;
    private long time;

    public PriorityCarMovement(PriorityCarAgent priorityCarAgent, long time) {
        super(priorityCarAgent, time);
        this.time = time;
        this.priorityCarAgent = priorityCarAgent;
    }

    @Override
    protected void onTick() {
        if(priorityCarAgent.getCar().getCurrentNode() == priorityCarAgent.getCar().getDestNode()) {
            System.out.println("Dest node");
            priorityCarAgent.unregister();
            return;
        }

        if(carStatus.equals(Status.ROAD) && priorityCarAgent.getCar().getCurrentRoad() != null) {
            if(priorityCarAgent.getCar().getCurrentDistanceTravelled() >= priorityCarAgent.getCar().getCurrentRoad().getDistance()) {
                this.handleEndOfRoad();
            } else {
                this.handleMovement();
            }
        } else if(carStatus.equals(Status.INTERSECTION)) {
            this.handleIntersection();
        }
    }

    private void handleIntersection() {
        //TODO
        this.carStatus = Status.ROAD;
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(priorityCarAgent.getCar().getCurrentVelocity());
        priorityCarAgent.getCar().addDistanceTravelled(distanceTravelled);
    }

    private void handleEndOfRoad() {
        carStatus = Status.INTERSECTION;
        this.priorityCarAgent.getCar().updateCurrentNode();
        this.priorityCarAgent.getSubscriptionInitiator().cancelInform();
    }

    private double kmph_to_mps(double kmph) {
        return (0.277778 * kmph);
    }
}

