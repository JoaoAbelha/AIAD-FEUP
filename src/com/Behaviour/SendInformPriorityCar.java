package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.Car;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SenderBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SendInformPriorityCar extends OneShotBehaviour {

    PriorityCarAgent agent;
    boolean done = false;
    boolean firstMessageSent = false;

    SendInformPriorityCar(PriorityCarAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        if (!firstMessageSent) {
            String roadToInform = "road" + agent.getCar().getCurrentNode() + "-";
            if (agent.getCar().getNextNode() == -1) {
                done = true;
                return;
            } else {
                roadToInform += agent.getCar().getNextNode();
            }

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(roadToInform, false));
            msg.setConversationId("IPC"); // Inform Priority Car
            msg.setContent("preference");
            //System.out.println(" >>>>>> Sending by priority car inform message to preferred " + roadToInform);
            myAgent.send(msg);
            firstMessageSent = true;
        }

        MessageTemplate mt = MessageTemplate.MatchConversationId("IPC");
        ACLMessage ack = myAgent.receive(mt); // received a message with content ack

        if (ack != null ) {
            //System.out.println(">>>>>> received ack pc com roadinfo");
            try {
                RoadInfo roadInfo = (RoadInfo) ack.getContentObject();
                this.agent.getCar().updateCarPath(agent.getCity(), roadInfo);
                this.agent.getCar().setStatus(Car.Status.ROAD);
                done = true;
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        else {
            block();
        }
    }
}