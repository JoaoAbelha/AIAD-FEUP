package com.CityManager;

import com.Agent.CarAgent;
import com.Agent.PriorityCarAgent;
import com.Agent.RoadAgent;
import com.Data.Car;
import com.Data.Graph;
import com.Data.PriorityCar;
import com.Data.RoadInfo;
import com.utils.*;
import jade.wrapper.StaleProxyException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CityManager extends AgentCreator {

    private Graph graph;
    private HashSet<Car> cars = new HashSet<>();
    private HashSet<PriorityCar> priorityCars = new HashSet<>();
    private HashSet<RoadInfo> roads = new HashSet<>();
    private HashMap<String, Float> weatherVelocityRestriction;
    private HashMap<Integer, String> weather = new HashMap<>();


    public CityManager() throws FileNotFoundException {
        super();

        CarReader r = new CarReader("src/car.txt");
        r.readFile();
        this.cars = r.getInfo();

        PriorityCarReader pr = new PriorityCarReader("src/priorityCars.txt");
        pr.readFile();
        this.priorityCars = pr.getInfo();


        GraphReader gr = new GraphReader("src/city.txt");
        gr.readFile();
        this.graph = gr.getInfo();
    }

    @Override
    void createAgentCars() {
        int unique = 0;
        for(Car car : this.cars) {
            CarAgent carAgent = new CarAgent(car, this.graph);
            try {
                this.agentController = this.containerController.acceptNewAgent("car" + String.valueOf(unique++), carAgent);
                this.agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void createWeatherStation() {
        // todo
    }

    @Override
    void createAgentRoads() {
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : this.graph.getEdges().entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadAgent roadAgent = new RoadAgent(adj.getValue());
                try {
                    System.out.println(src + " " + dest);
                    this.agentController = this.containerController.acceptNewAgent("road" + src + dest, roadAgent);
                    this.agentController.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    void createPriorityCars() {
        int unique = 0;
        for(PriorityCar car : this.priorityCars) {
            PriorityCarAgent carAgent = new PriorityCarAgent();
            try {
                this.agentController = this.containerController.acceptNewAgent("priorityCar" + String.valueOf(unique++), carAgent);
                this.agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        CityManager cityManager = new CityManager();
        cityManager.createAgentRoads();
        cityManager.createAgentCars();
        cityManager.createPriorityCars();
        System.out.println("city manager running...");
    }

}
