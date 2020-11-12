package com.Behaviour;


import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

import java.io.IOException;

public class RoadNetResponder extends ContractNetResponder {

    private RoadAgent road;

    public RoadNetResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
        this.road = (RoadAgent) a;
    }


    /**
     * todos: not sure here
     * */
    private boolean performAction() {
        // the action never fails
        return true;
    }


    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {
        //System.out.println("Agent " + myAgent.getLocalName() + ": CFP received from " + cfp.getSender().getName() + ". Action is " + cfp.getContent());
        int proposal = this.road.getUtility(cfp.getSender().getName());
        if (!road.isRoadFull()) {
            // We provide a proposal
            //System.out.println("Agent " + myAgent.getLocalName() + ": Proposing " + proposal);
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(proposal));
            return propose;
        } else {
            ACLMessage refuse = cfp.createReply();
            refuse.setPerformative(ACLMessage.REFUSE);
            refuse.setContent("full-road");
            //System.out.println("Agent " + myAgent.getLocalName() + ": Refuse");
            return refuse;
           // throw new RefuseException("evaluation-failed");
        }
    }

    /**
     * this function is executed when it receives all message, or when a timeout (there is a default value)
     * is activated
     * @param cfp
     * @param propose
     * @param accept
     * @return
     * @throws FailureException
     */
    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        //System.out.println("Agent "+ myAgent.getLocalName() +": Proposal accepted");

        if (performAction()) {
            try {
                ACLMessage inform = accept.createReply();
                inform.setPerformative(ACLMessage.INFORM); // INFORM DONE
                inform.setContentObject(road.getRoadInfo());
                return inform;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new FailureException("unexpected-error");
        }

        return null;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        //System.out.println("Agent "+ myAgent.getLocalName() +": Proposal rejected");
    }
}
