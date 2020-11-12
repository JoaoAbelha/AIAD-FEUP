package com.Behaviour;

import com.Agent.PriorityCarAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SendInformPriorityCar extends OneShotBehaviour {

    PriorityCarAgent agent;

    SendInformPriorityCar(PriorityCarAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {

        String roadToInform = "road" + agent.getCar().getCurrentNode() ;
        if (agent.getCar().getNextNode() == -1) {
            return;
        } else {
            roadToInform += agent.getCar().getNextNode();
        }

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(roadToInform, false));
        msg.setConversationId("IPC"); // Inform Priority Car
        msg.setContent("preference");
        System.out.println(" >>>>>> Sending by priority car inform message to preferred " + roadToInform);
        myAgent.send(msg);
    }
}
