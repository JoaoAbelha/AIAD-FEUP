package com.Behaviour;

import com.Agent.CarAgent;
import jade.core.behaviours.TickerBehaviour;

public class CarMovement extends TickerBehaviour {
    public enum Status {ROAD, INTERSECTION};

    private CarAgent carAgent = null;
    private Status carStatus = Status.ROAD;
    private long time;

    public CarMovement(CarAgent carAgent, long time) {
        super(carAgent, time);
        this.time = time;
        this.carAgent = carAgent;
    }

    @Override
    protected void onTick() {
        if(carAgent.getCar().getCurrentNode() == carAgent.getCar().getDestNode()) {
            System.out.println("Dest node");
            carAgent.unregister();
            return;
        }

        if(carStatus.equals(Status.ROAD) && carAgent.getCar().getCurrentRoad() != null) {

            if(carAgent.getCar().getCurrentDistanceTravelled() >= carAgent.getCar().getCurrentRoad().getDistance()) {
                this.handleEndOfRoad();
            } else {
                this.handleMovement();
            }
        } else if(carStatus.equals(Status.INTERSECTION)) {
            this.handleIntersection();
        }
    }

    private void handleIntersection() {
        carAgent.getCar().updateCarPath(carAgent.getCity());
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(carAgent.getCar().getCurrentVelocity());
        carAgent.getCar().addDistanceTravelled(distanceTravelled);
    }

    private void handleEndOfRoad() {
        carStatus = Status.INTERSECTION;
    }

    private int kmph_to_mps(int kmph) {
        return(int) (0.277778 * kmph);
    }
}
