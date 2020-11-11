package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Map;

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
        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
        message.setContent("road-value?");

        Map<Integer, RoadInfo> adjacentRoads = this.carAgent.getCity().getAdjacent(this.carAgent.getCar().getCurrentNode());
        adjacentRoads.forEach((followingNode, roadInfo) -> {
           message.addReceiver(new AID( "road"+ this.carAgent.getCar().getCurrentNode() + followingNode, false));
        });

        int nrAgents = adjacentRoads.size();
        SequentialBehaviour negotiation = new SequentialBehaviour();
        negotiation.addSubBehaviour(new sendPreference());
        negotiation.addSubBehaviour(new CarNetInitiator(this.carAgent, message, nrAgents));
        this.carAgent.addBehaviour(negotiation);


        this.carStatus = Status.ROAD;
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(carAgent.getCar().getCurrentVelocity());
        carAgent.getCar().addDistanceTravelled(distanceTravelled);
    }

    private void handleEndOfRoad() {
        carStatus = Status.INTERSECTION;
        this.carAgent.getCar().updateCurrentNode();
    }

    private int kmph_to_mps(int kmph) {
        return(int) (0.277778 * kmph);
    }
}
