package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.ContractNetCfp;
import com.Data.PathResponse;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.io.IOException;
import java.util.Date;

public class CarRequestInitiator extends AchieveREInitiator {
    private final CarAgent carAgent;
    private CarMovement carMovement;

    public CarRequestInitiator(CarAgent carAgent, ACLMessage msg, CarMovement carMovement) {
        super(carAgent, msg);
        this.carAgent = carAgent;
        this.carMovement = carMovement;
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        carAgent.getLOGGER().severe("City refused request. Car currentNode - " + carAgent.getCar().getCurrentNode() + " does not exist");
        carAgent.unregister();
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        super.handleAgree(agree);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            PathResponse response = (PathResponse) inform.getContentObject();
            this.carAgent.updatePathResponse(response);
            carAgent.getLOGGER().info("City returned " + response.getPaths().size() + " possible paths");
            this.carMovement.carContractNet();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        super.handleFailure(failure);
    }
}
