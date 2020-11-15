package com.Behaviour;

import com.Agent.RoadAgent;
import com.Data.PathRequest;
import com.Data.PathResponse;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;

public class RoadRequestResponder extends AchieveREResponder {
    private final RoadAgent roadAgent;

    public RoadRequestResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
        roadAgent = (RoadAgent) a;
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage reply = request.createReply();
        int node = Integer.parseInt(request.getContent());
        if(roadAgent.getRoadInfo().getStartNode() == node) {
            roadAgent.getLOGGER().info("Received valid priority car request from " +request.getSender().getLocalName() + ". Sending Agree");
            reply.setPerformative(ACLMessage.AGREE);
        } else {
            roadAgent.getLOGGER().info("Received invalid priority car request from " + request.getSender().getLocalName() + ". Sending Refuse");
            reply.setPerformative(ACLMessage.REFUSE);
        }

        return reply;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        try {
            roadAgent.getLOGGER().info("Stopping all cars and sending my information to " + request.getSender().getLocalName());
            roadAgent.getManager().notifyAll("priority:" + 0 + ":" + 0, "road" + roadAgent.getRoadInfo().getStartNode() + "-" + roadAgent.getRoadInfo().getEndNode());
            ACLMessage inform = request.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            inform.setContentObject(roadAgent.getRoadInfo());
            return inform;
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
