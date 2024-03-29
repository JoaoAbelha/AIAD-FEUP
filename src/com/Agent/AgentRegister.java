package com.Agent;


import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import sajas.core.Agent;
import sajas.domain.DFService;


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

    public void unregister() {
        try {
            DFService.deregister(this);
            this.doDelete();
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
