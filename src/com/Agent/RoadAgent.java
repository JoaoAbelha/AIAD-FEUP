package com.Agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class RoadAgent  extends Agent {
    private int distance;
    private int velocity;
    private DFAgentDescription dfd;


    public RoadAgent(int velocity, int distance) {
        this.distance = distance;
        this.velocity = velocity;
    }

    public void register() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("road");
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
        System.out.println("Road agent started");
    }
}
