package com.Behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class sendPreference extends Behaviour {

    boolean done = false;
    boolean firstMessageSent = false;
    MessageTemplate mt =
            MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    @Override
    public void action() {

        if (!firstMessageSent) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("road23", false));
            msg.setContent("preference");
            System.out.println(" >>>>>> Sending inform message do preferred road");
            myAgent.send(msg);
            firstMessageSent = true;
        }

        // i did this to guarantee that the first message arrived to the agent before the following...
        ACLMessage ack = myAgent.receive(mt); // received a message with content ack

        if (ack != null ) {
            System.out.println(">>>>>> received ack " + ack.getContent());
            done = true;
        }
        else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }


}
