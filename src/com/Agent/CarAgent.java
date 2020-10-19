package com.Agent;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class CarAgent extends Agent {

    private String name;
    private DFAgentDescription dfd;

    public CarAgent(String name) {
        this.name = name;
    }

    public void register() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType(this.name);
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

    @Override
    protected void setup() {
        register();
        System.out.println("Car agent started");
    }
}
