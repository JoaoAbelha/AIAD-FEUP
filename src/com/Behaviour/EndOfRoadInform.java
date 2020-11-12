package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EndOfRoadInform extends OneShotBehaviour {
    private final RoadInfo roadInfo;
    private CarAgent carAgent;

    public EndOfRoadInform(CarAgent agent, RoadInfo roadInfo) {
        this.carAgent = agent;
        this.roadInfo = roadInfo;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("road" + roadInfo.getStartNode() + roadInfo.getEndNode(), false));
        msg.setConversationId("EOR");
        //System.out.println(" >>>>>> Sending inform message to preferred " + wishedRoad);
        myAgent.send(msg);
    }
}
