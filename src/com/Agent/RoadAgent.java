package com.Agent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class RoadAgent  extends AgentRegister {
    private int distance;
    private int velocity;
    private DFAgentDescription dfd;


    public RoadAgent(int velocity, int distance) {
        this.distance = distance;
        this.velocity = velocity;
    }

    @Override
    protected void setup() {
        register("road");
        System.out.println("Road agent started");
    }
}
