package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PreferenceListener extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;

    public PreferenceListener(Agent a) {
        roadAgent = (RoadAgent) a;
        //mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        mt = MessageTemplate.MatchConversationId("SP");
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            // update road agent
            System.out.println("received from the car " + msg.getSender().getLocalName());
            roadAgent.updateWishList( msg.getSender().getLocalName(), true);
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("ACK");
            roadAgent.send(reply);
        } else {
            block();
        }


    }
}