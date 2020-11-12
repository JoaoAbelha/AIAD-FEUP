package com.Behaviour;

import com.Agent.CarAgent;
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

    private final CarAgent carAgent;

    sendPreference(CarAgent a) {
        this.carAgent = a;
    }

    @Override
    public void action() {

        if (!firstMessageSent) {
            String wishedRoad = "road" + carAgent.getCar().getCurrentNode() ;
            if (carAgent.getCar().getNextNode() == -1) {
                done = true;
                return;
            } else {
                wishedRoad += carAgent.getCar().getNextNode();
            }

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(wishedRoad, false));
            msg.setContent("preference");
            System.out.println(" >>>>>> Sending inform message to preferred " + wishedRoad);
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
