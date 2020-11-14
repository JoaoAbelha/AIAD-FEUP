package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.Car;
import com.Data.PathRequest;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CarMovement extends TickerBehaviour {
    private CarAgent carAgent = null;
    private long time;
    private boolean requestInitialized = false;

    public CarMovement(CarAgent carAgent, long time) {
        super(carAgent, time);
        this.time = time;
        this.carAgent = carAgent;
    }

    @Override
    protected void onTick() {
        if(carAgent.getCar().getCurrentNode() == carAgent.getCar().getDestNode()) {
            System.out.println("Dest node" + carAgent.getCar().getName());
            carAgent.unregister();
            return;
        }

        if(carAgent.getCar().getCarStatus().equals(Car.Status.ROAD) && carAgent.getCar().getCurrentRoad() != null) {
            if(carAgent.getCar().getCurrentDistanceTravelled() >= carAgent.getCar().getCurrentRoad().getDistance()) {
                this.handleEndOfRoad();
            } else {
                this.handleMovement();
            }
        } else if(carAgent.getCar().getCarStatus().equals(Car.Status.INTERSECTION)) {
            this.handleIntersection();
        }
    }

    private void handleIntersection() {
        try {
            if(requestInitialized) return;
            PathRequest pathRequest = new PathRequest(carAgent.getCar().getCurrentNode(), carAgent.getCar().getDestNode(), carAgent.getCar().getStrategy());
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(new AID(("city"), AID.ISLOCALNAME));
            request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            request.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
            request.setContentObject(pathRequest);
            this.carAgent.addBehaviour(new CarRequestInitiator(this.carAgent, request));
            this.requestInitialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(carAgent.getCar().getCurrentVelocity());
        carAgent.getCar().addDistanceTravelled(distanceTravelled);
    }

    private void handleEndOfRoad() {
        carAgent.getCar().setCarStatus(Car.Status.INTERSECTION);
        this.sendEnfOfRoad();
        this.requestInitialized = false;
        this.carAgent.getCar().updateCurrentNode();
        this.carAgent.getSubscriptionInitiator().cancelInform();
    }

    private double kmph_to_mps(double kmph) {
        return (0.277778 * kmph);
    }

    private void sendEnfOfRoad() {
        RoadInfo roadInfo = this.carAgent.getCar().getCurrentRoad();
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("road" + roadInfo.getStartNode() + roadInfo.getEndNode(), false));
        msg.setConversationId("EOR");
        msg.setContent(String.valueOf(carAgent.getCar().getLength()));
        myAgent.send(msg);
    }
}
