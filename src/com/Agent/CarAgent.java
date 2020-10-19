package com.Agent;

import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarAgent extends AgentRegister {

    private String name;
    private DFAgentDescription dfd;

    public CarAgent(String name) {
        this.name = name;
    }

    @Override
    protected void setup() {
        register(this.name);
        System.out.println("Car agent started");
    }
}
