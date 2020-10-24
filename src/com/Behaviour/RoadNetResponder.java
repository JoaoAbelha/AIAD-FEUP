package com.Behaviour;


import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class RoadNetResponder extends ContractNetResponder {

    private RoadAgent road;

    public RoadNetResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
        this.road = (RoadAgent) a;
    }

    // todo: should this function be in the road class
    private int utility() {
        // todo change in the future
        return (int) (Math.random() * 10);
    }

    private boolean roadNotFull() {
        // Simulate action execution by generating a random number
        return (Math.random() > 0.2);
    }

    private boolean performAction() {
        // Simulate action execution by generating a random number
        return (Math.random() > 0.2);
    }


    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        System.out.println("Agent " + myAgent.getLocalName() + ": CFP received from " + cfp.getSender().getName() + ". Action is " + cfp.getContent());
        int proposal = utility();
        if (roadNotFull()) {
            // We provide a proposal
            System.out.println("Agent " + myAgent.getLocalName() + ": Proposing " + proposal);
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(proposal));
            return propose;
        } else {
            // todo: We refuse to provide a proposal, send a message
            System.out.println("Agent " + myAgent.getLocalName() + ": Refuse");
            // todo: should send a refuse message?
            throw new RefuseException("evaluation-failed");
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
        System.out.println("Agent "+ myAgent.getLocalName() +": Proposal accepted");

        // todo: I think we are not gonna need this case
        if (performAction()) {
            System.out.println("Agent "+ myAgent.getLocalName() +": Action successfully performed");
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            return inform;
        }
        else {
            System.out.println("Agent "+ myAgent.getLocalName() +": Action execution failed");
            throw new FailureException("unexpected-error");
        }
    }

    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println("Agent "+ myAgent.getLocalName() +": Proposal rejected");
    }
}
