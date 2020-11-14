package com.Behaviour;

import com.Agent.CarAgent;
import com.Data.Car;
import com.Data.ContractNetCfp;
import com.Data.PathResponse;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CarRequestInitiator extends AchieveREInitiator {
    private final CarAgent carAgent;

    public CarRequestInitiator(CarAgent carAgent, ACLMessage msg) {
        super(carAgent, msg);
        this.carAgent = carAgent;
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
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
            ContractNetCfp cfp = new ContractNetCfp(carAgent.getCar().getStrategy(), carAgent.getCar().getLength());

            ACLMessage contractNet = new ACLMessage(ACLMessage.CFP);
            contractNet.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            contractNet.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
            contractNet.setContentObject(cfp);
            for (String roadName : response.getPaths().keySet())
                contractNet.addReceiver(new AID(roadName, AID.ISLOCALNAME));

            carAgent.addBehaviour(new CarNetInitiator(this.carAgent, contractNet));
        } catch (UnreadableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        super.handleFailure(failure);
    }
}
