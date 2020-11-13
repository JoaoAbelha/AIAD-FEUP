package com.Behaviour;

import com.Agent.RoadAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

public class ReceiveInformPriorityCar extends CyclicBehaviour {

    private final RoadAgent roadAgent;
    MessageTemplate mt = null;

    public ReceiveInformPriorityCar(Agent a) {
        roadAgent = (RoadAgent) a;
        //mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        mt = MessageTemplate.MatchConversationId("IPC");
    }

    @Override
    public void action() {
        ACLMessage msg = roadAgent.receive(mt);
        if(msg != null) {
            // update road agent
            //System.out.println("received msg from the priority car " + msg.getSender().getLocalName());
            roadAgent.getManager().notifyAll("priority:" + 0 + ":" + 0);
            try {
                ACLMessage reply = msg.createReply();
                reply.setConversationId("IPC");
                reply.setContentObject(roadAgent.getRoadInfo());
                roadAgent.send(reply);
            } catch (IOException e){
                e.printStackTrace();
            }
            // do here stuff
        } else {
            block();
        }


    }
}
