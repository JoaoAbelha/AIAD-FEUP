package com.Agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class AgentRegister extends Agent {

    protected DFAgentDescription dfd;

    public void register(String type) {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(getLocalName());

        this.dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, this.dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
