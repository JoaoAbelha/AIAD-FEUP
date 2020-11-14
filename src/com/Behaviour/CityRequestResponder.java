package com.Behaviour;

import com.Agent.CityAgent;
import com.Data.PathRequest;
import com.Data.PathResponse;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;

public class CityRequestResponder extends AchieveREResponder {
    private CityAgent city;
    private PathRequest request;

    public CityRequestResponder(CityAgent city, MessageTemplate mt) {
        super(city, mt);
        this.city = city;
        this.request = null;
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        try {
            ACLMessage reply = request.createReply();
            this.request = (PathRequest) request.getContentObject();
            if(city.existsNode(this.request.getStartNode())) {
                reply.setPerformative(ACLMessage.AGREE);
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
            }

            return reply;
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        try {
            ACLMessage inform = request.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            PathResponse pathResponse = city.calculatePath(this.request);
            inform.setContentObject(pathResponse);
            return inform;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
