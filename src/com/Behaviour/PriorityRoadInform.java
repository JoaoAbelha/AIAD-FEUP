package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class PriorityRoadInform extends OneShotBehaviour {
    PriorityCarAgent priorityCarAgent;
    private final RoadInfo roadInfo;

    public PriorityRoadInform(PriorityCarAgent priorityCarAgent, RoadInfo roadInfo) {
        this.priorityCarAgent = priorityCarAgent;
        this.roadInfo = roadInfo;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("road" + roadInfo.getStartNode() + "-" + roadInfo.getEndNode(), false));
        msg.setConversationId("PEOR");
        msg.setContent(String.valueOf(priorityCarAgent.getCar().getCurrentDistanceTravelled()));
        //System.out.println(" >>>>>> Sending inform message to preferred " + wishedRoad);
        myAgent.send(msg);
    }
}
