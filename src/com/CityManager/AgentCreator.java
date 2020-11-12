package com.CityManager;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public abstract class AgentCreator {

    protected ContainerController containerController;
    private Runtime runtime;
    private Profile profile;
    protected AgentController agentController;

    AgentCreator() {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl(true);
        this.containerController = this.runtime.createMainContainer(this.profile);
    }

    abstract void createAgentCars();
    abstract void createCity();
    abstract void createAgentRoads();
}
