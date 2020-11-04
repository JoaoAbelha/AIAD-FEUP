package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.Vector;

public class CarNetInitiator extends ContractNetInitiator {

    private int nrResponders;
    private CarAgent car;

    public CarNetInitiator(Agent a, ACLMessage cfp, int nrResponders) {
        super(a, cfp);
        this.nrResponders = nrResponders;
        this.car = (CarAgent) a;
    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector acceptances) {
        System.out.println("Agent "+propose.getSender().getName()+" proposed "+propose.getContent());
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Agent "+refuse.getSender().getName()+" refused");
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver does not exist
            System.out.println("Responder does not exist");
        }
        else {
            System.out.println("Agent "+failure.getSender().getName()+" failed");
        }
        // Immediate failure --> we will not receive a response from this agent
        nrResponders--;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        if (responses.size() < nrResponders) {
            // Some responder didn't reply within the specified timeout
            System.out.println("Timeout expired: missing "+(nrResponders - responses.size())+" responses");
        }
        // Evaluate proposals. Chooses the one with the highest value
        //todo: can receive refuses (roads are full)
        int bestProposal = -1;
        AID bestProposer = null;
        ACLMessage accept = null;
        Enumeration e = responses.elements();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                int proposal = Integer.parseInt(msg.getContent());
                if (proposal > bestProposal) {
                    bestProposal = proposal;
                    bestProposer = msg.getSender();
                    accept = reply;
                }
            }
            else if (msg.getPerformative() == ACLMessage.REFUSE) {
                System.out.println(myAgent.getName() + "was refused by a full road " + msg.getSender().getName());
            }
        }
        // Accept the proposal of the best proposer
        if (accept != null) {
            System.out.println("Accepting proposal "+bestProposal+" from responder "+bestProposer.getName());
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        }

    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            RoadInfo roadInfo = (RoadInfo) inform.getContentObject();
            car.getCar().updateCarPath(car.getCity(), roadInfo);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    // todo:
    // handleFailure function nao existe pq?
}
