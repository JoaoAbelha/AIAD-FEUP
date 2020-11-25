package com.Behaviour;

import com.Agent.PriorityCarAgent;
import com.Data.Car;
import com.Data.PathResponse;
import com.Data.RoadInfo;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SenderBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.proto.AchieveREInitiator;
//import jade.proto.AchieveREInitiator;

public class PriorityCarRequestInitiator extends AchieveREInitiator {

    PriorityCarAgent agent;
    private PriorityCarMovement carMovement;

    PriorityCarRequestInitiator(PriorityCarAgent agent, ACLMessage message, PriorityCarMovement carMovement) {
        super(agent, message);
        this.agent = agent;
        this.carMovement = carMovement;
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        carMovement.setRequestInitilized(false);
        agent.getLOGGER().info(refuse.getSender().getLocalName() + " refused request");
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
        agent.getLOGGER().info(agree.getSender().getLocalName() + " agreed with the request");

    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            RoadInfo roadInfo = (RoadInfo) inform.getContentObject();
            this.agent.getCar().updateCarPath(agent.getCity(), roadInfo);
            agent.getLOGGER().info(agent.getCar().getName() + " driving on road " + roadInfo.getStartNode() + "-" + roadInfo.getEndNode());
            this.agent.getCar().setStatus(Car.Status.ROAD);
            this.agent.updateSubscriptionInitiator();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        carMovement.setRequestInitilized(false);
    }
}