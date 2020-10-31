package com.Agent;

import com.Behaviour.CarMovement;
import com.Data.Car;
import com.Data.Graph;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarAgent extends AgentRegister {

    private String name;
    private DFAgentDescription dfd;
    private final Car car;
    private final Graph city;

    public CarAgent(Car car, Graph city) {
        this.car = car;
        this.city = city;
    }

    public Car getCar() {
        return car;
    }

    public Graph getCity() {
        return city;
    }

    @Override
    protected void setup() {
        register(car.getName());
        car.calculateCarPath(this.city);
        System.out.println("Car agent started");
        addBehaviour(new CarMovement(this, 300));
        /*ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000)); // wait 10 seconds for reply
        message.setContent("what-best-road-value");
        // todo dfSearch to find the roads registered that we want and pass the number of agents
        int nrAgents = 0;
        addBehaviour(new CarNetInitiator(this, message, nrAgents));*/
    }
}
