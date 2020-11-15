package com.Behaviour;


import com.Agent.RoadAgent;
import com.Data.ContractNetCfp;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;

import java.io.IOException;

public class RoadNetResponder extends ContractNetResponder {

    private RoadAgent road;

    public RoadNetResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
        this.road = (RoadAgent) a;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {
        road.getLOGGER().info("Received CFP from " + cfp.getSender().getLocalName());
        try {
            ContractNetCfp contractNetCfp = (ContractNetCfp) cfp.getContentObject();
            double proposal = this.road.getUtility(contractNetCfp.getStrategy());
            if (!road.isRoadFull(contractNetCfp.getLength())) {
                road.getLOGGER().info("Road is not full. Sending proposal with value " + proposal + " to " + cfp.getSender().getLocalName());
                ACLMessage propose = cfp.createReply();
                propose.setPerformative(ACLMessage.PROPOSE);
                propose.setContent(String.valueOf(proposal));
                return propose;
            } else {
                ACLMessage refuse = cfp.createReply();
                refuse.setPerformative(ACLMessage.REFUSE);
                refuse.setContent("full-road");
                road.getLOGGER().info("Road is full. Sending refuse to " + cfp.getSender().getLocalName());
                return refuse;
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        return null;
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
       // System.out.println("<>>>> handle proposal: " + road.getSpaceOccupied());
        //if (road.isRoadFull(Double.valueOf(accept.getContent()))) {
          //  System.out.println(">>>>>>>>>>>problem found bro");
        //}
        try {
            road.updateCars(accept.getSender().getLocalName(), Double.valueOf(accept.getContent()), true);
            road.getLOGGER().info(accept.getSender().getLocalName() + " accepted my proposal. Sending my information");
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM); // INFORM DONE
            inform.setContentObject(road.getRoadInfo());
            return inform;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        road.getLOGGER().info(reject.getSender().getLocalName() + " reject my proposal");
    }
}