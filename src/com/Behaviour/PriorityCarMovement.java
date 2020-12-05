package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.Car;
import com.Data.RoadInfo;
//import jade.core.AID;
//import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import sajas.core.behaviours.TickerBehaviour;

import java.util.Date;

public class PriorityCarMovement extends TickerBehaviour {
    private PriorityCarAgent priorityCarAgent = null;
    private long time;
    private boolean requestInitilized = false;

    public PriorityCarMovement(PriorityCarAgent priorityCarAgent, long time) {
        super(priorityCarAgent, time);
        this.time = time;
        this.priorityCarAgent = priorityCarAgent;
    }

    @Override
    protected void onTick() {
        if(priorityCarAgent.getCar().getCurrentNode() == priorityCarAgent.getCar().getDestNode()) {
            priorityCarAgent.getCar().setCurrentVelocity(0);
            priorityCarAgent.getLOGGER().info("Priority car arrived at destination");
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

    public void setRequestInitilized(boolean requestInitilized) {
        this.requestInitilized = requestInitilized;
    }

    private void handleIntersection() {
        if(requestInitilized) return;

        int nextNode = priorityCarAgent.getCar().getNextNode();
        if(nextNode == -1) return;
        String roadToInform = "road" + priorityCarAgent.getCar().getCurrentNode() + "-" + nextNode;
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new sajas.core.AID((roadToInform), AID.ISLOCALNAME));
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        request.setContent(String.valueOf(priorityCarAgent.getCar().getCurrentNode()));
        priorityCarAgent.getLOGGER().info("Sending request to " + roadToInform + " to stop all cars");
        this.priorityCarAgent.addBehaviour(new PriorityCarRequestInitiator(this.priorityCarAgent, request, this));
        this.requestInitilized = true;
    }

    private void handleMovement() {
        double distanceTravelled = this.time / 1000.0 * this.kmph_to_mps(priorityCarAgent.getCar().getCurrentVelocity());
        priorityCarAgent.getCar().addDistanceTravelled(distanceTravelled);
        informDistanceTravelled();
    }

    private void handleEndOfRoad() {
        sendEndOfRoad();
        priorityCarAgent.getCar().setStatus(Car.Status.INTERSECTION);
        this.priorityCarAgent.getCar().updateCurrentNode();
        this.priorityCarAgent.getSubscriptionInitiator().cancelInform();
        priorityCarAgent.getLOGGER().info(priorityCarAgent.getCar().getName() + " reached end of road");
        this.requestInitilized = false;
    }

    private double kmph_to_mps(double kmph) {
        return (0.277778 * kmph);
    }

    private void informDistanceTravelled() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        RoadInfo roadInfo = priorityCarAgent.getCar().getCurrentRoad();
        msg.addReceiver(new sajas.core.AID("road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode(), false));
        msg.setConversationId("PEOR");
        msg.setContent(String.valueOf(priorityCarAgent.getCar().getCurrentDistanceTravelled()));
        myAgent.send(msg);
    }

    private void sendEndOfRoad() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        RoadInfo roadInfo = priorityCarAgent.getCar().getCurrentRoad();
        msg.addReceiver(new sajas.core.AID("road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode(), false));
        msg.setConversationId("PEOR");
        msg.setContent("EOF");
        myAgent.send(msg);
    }
}

