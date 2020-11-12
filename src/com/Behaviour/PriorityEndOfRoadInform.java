package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class PriorityEndOfRoadInform extends OneShotBehaviour {
    PriorityCarAgent priorityCarAgent;
    private final RoadInfo roadInfo;

    public PriorityEndOfRoadInform(PriorityCarAgent priorityCarAgent, RoadInfo roadInfo) {
        this.priorityCarAgent = priorityCarAgent;
        this.roadInfo = roadInfo;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("road" + roadInfo.getStartNode() + roadInfo.getEndNode(), false));
        msg.setConversationId("PEOR");
        //System.out.println(" >>>>>> Sending inform message to preferred " + wishedRoad);
        myAgent.send(msg);
    }
}
