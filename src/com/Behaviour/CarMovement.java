package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.Car;
import com.Data.ContractNetCfp;
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
    private boolean restartContractNet = false;

    public CarMovement(CarAgent carAgent, long time) {
        super(carAgent, time);
        this.time = time;
        this.carAgent = carAgent;
    }

    @Override
    protected void onTick() {
        if(carAgent.getCar().getCurrentNode() == carAgent.getCar().getDestNode()) {
            carAgent.getLOGGER().info("Car arrived at destination");
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
           // System.out.println(carAgent.getCar().getName() + " in the intersection");
            this.handleIntersection();
        }
    }

    public void setRestartContractNet(boolean restartContractNet) {
        this.restartContractNet = restartContractNet;
    }

    private void handleIntersection() {
        if(!requestInitialized) {
            this.carRequest();
        } else if(restartContractNet) {
            this.carContractNet();
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
        this.restartContractNet = false;
        this.carAgent.getCar().updateCurrentNode();
        this.carAgent.getSubscriptionInitiator().cancelInform();
    }

    private double kmph_to_mps(double kmph) {
        return (0.277778 * kmph);
    }

    private void carRequest() {
        try {
            PathRequest pathRequest = new PathRequest(carAgent.getCar().getCurrentNode(), carAgent.getCar().getDestNode(), carAgent.getCar().getStrategy());
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(new AID(("city"), AID.ISLOCALNAME));
            request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            request.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
            request.setContentObject(pathRequest);
            carAgent.getLOGGER().info("Starting request with city to get possible paths in node " + carAgent.getCar().getCurrentNode());
            this.carAgent.addBehaviour(new CarRequestInitiator(this.carAgent, request, this));
            this.requestInitialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carContractNet() {
        try {
            ContractNetCfp cfp = new ContractNetCfp(carAgent.getCar().getStrategy(), carAgent.getCar().getLength());
            ACLMessage contractNet = new ACLMessage(ACLMessage.CFP);
            contractNet.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            contractNet.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
            contractNet.setContentObject(cfp);
            for (String roadName : carAgent.getCurrentPathResponse().getPaths().keySet())
                contractNet.addReceiver(new AID(roadName, AID.ISLOCALNAME));
            carAgent.getLOGGER().info("Starting contract net with roads in the intersection");
            carAgent.addBehaviour(new CarNetInitiator(this.carAgent, contractNet, this));
            restartContractNet = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEnfOfRoad() {
        RoadInfo roadInfo = this.carAgent.getCar().getCurrentRoad();
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        String roadName = "road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode();
        msg.addReceiver(new AID(roadName, false));
        msg.setConversationId("EOR");
        msg.setContent(String.valueOf(carAgent.getCar().getLength()));
        carAgent.getLOGGER().info("Car reached end of " + roadName + " sending EOF message");
        myAgent.send(msg);
    }
}
