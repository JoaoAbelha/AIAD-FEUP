package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.Car;
import com.Data.RoadInfo;
import com.utils.Pair;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.Vector;

public class CarNetInitiator extends ContractNetInitiator {
    private CarAgent car;
    private CarMovement carMovement;

    public CarNetInitiator(Agent a, ACLMessage cfp, CarMovement carMovement) {
        super(a, cfp);
        this.car = (CarAgent) a;
        this.carMovement = carMovement;
    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        double proposal = Double.parseDouble(propose.getContent());
        car.getLOGGER().info("Received proposal from " + propose.getSender().getLocalName() + " with value " + proposal);
        super.handlePropose(propose, acceptances);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        super.handleRefuse(refuse);
        car.getLOGGER().info("Received refuse from " + refuse.getSender().getLocalName());
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        super.handleFailure(failure);
        this.carMovement.setRestartContractNet(true);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        double bestProposal = Double.POSITIVE_INFINITY;
        double bestResult = Double.POSITIVE_INFINITY;
        AID bestProposer = null;
        ACLMessage accept = null;
        Enumeration e = responses.elements();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                double proposal = Double.parseDouble(msg.getContent());
                double result = analyzeProposal(proposal, msg.getSender().getLocalName());
                if (result < bestResult) {
                    bestResult = result;
                    bestProposal = proposal;
                    bestProposer = msg.getSender();
                    accept = reply;
                }
            }

        }
        // Accept the proposal of the best proposer
        if (accept != null) {
            car.getLOGGER().info("Accepting proposal " + bestProposal + " from responder " + bestProposer.getLocalName() + " with estimated result of " + bestResult);
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            accept.setContent(String.valueOf(car.getCar().getLength()));
        } else {
            this.carMovement.setRestartContractNet(true);
        }
    }

    private double analyzeProposal(double proposal, String name) {
        double result = proposal;
        for(Pair<Double, Integer> road: car.getCurrentPathResponse().getPaths().get(name)) {
            switch (car.getCar().getStrategy()) {
                case SHORTEST_PATH:
                    result += road.getValue();
                    break;
                case SHORTEST_TIME:
                    result += road.getValue() * 1.0 / road.getKey();
                    break;
                case MINIMUM_INTERSECTIONS:
                    result += 1;
                    break;
            }
        }

        return result;
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            RoadInfo roadInfo = (RoadInfo) inform.getContentObject();
            car.getCar().updateCarPath(roadInfo);
            car.getLOGGER().info(car.getCar().getName() + " driving on road " + roadInfo.getStartNode() + "-" + roadInfo.getEndNode());
            car.updateSubscriptionInitiator();
            car.getCar().setCarStatus(Car.Status.ROAD);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
