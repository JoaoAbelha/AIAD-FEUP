package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveInformPriorityCard extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;

    public ReceiveInformPriorityCard(Agent a) {
        roadAgent = (RoadAgent) a;
        //mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        mt = MessageTemplate.MatchConversationId("IPC");
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            // update road agent
            System.out.println("received msg from the priority car " + msg.getSender().getLocalName());
            // do here stuff
        } else {
            block();
        }


    }
}
