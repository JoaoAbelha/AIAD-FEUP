package com.CityManager;

import com.Agent.CarAgent;
import com.Agent.RoadAgent;
import com.Data.Car;
import com.Data.Graph;
import com.Data.RoadInfo;
import com.utils.*;
import jade.wrapper.StaleProxyException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class cityManager extends AgentCreator {

    private Graph graph = new Graph();
    private HashSet<Car> cars = new HashSet<>();
    private HashMap<String, Float> weatherVelocityRestriction;
    private HashMap<Integer, String> weather = new HashMap<>();


    public cityManager() throws FileNotFoundException {
        super();

        CarReader r = new CarReader("src/car.txt");
        r.readFile();
        this.cars = r.getInfo();


        GraphReader gr = new GraphReader("src/city.txt");
        gr.readFile();
        this.graph = gr.getInfo();

        //ShortestPath sp = new ShortestPath();
        //sp.buildRoute(1,4, this.graph);
        // initialize the rest

    }

    @Override
    void createAgentCars() {
        int unique = 0;
        for(Car car : this.cars) {
            CarAgent carAgent = new CarAgent(car, graph);
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
    void createRoads() {
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : this.graph.getEdges().entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadAgent roadAgent = new RoadAgent(adj.getValue().getMaxVelocity(), adj.getValue().getDistance());
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

    public static void main(String[] args) throws FileNotFoundException {
        cityManager cityManager = new cityManager();
        cityManager.createAgentCars();
        cityManager.createRoads();
        System.out.println("city manager running...");

    }

}
