package com.CityManager;

import com.Agent.CarAgent;
import com.Data.Car;
import com.Data.Graph;
import com.utils.*;
import jade.wrapper.StaleProxyException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;

public class cityManager extends AgentCreator {

    private Graph graph;
    private HashSet<Car> cars;
    private HashMap<String, Float> weatherVelocityRestriction;
    private HashMap<Integer, String> weather = new HashMap<>();


    public cityManager() throws FileNotFoundException {
        super();
        CarReader r = new CarReader("src/car.txt");
        r.readFile();
        this.cars = r.getInfo();

        //GraphReader gr = new GraphReader("src/city.txt");
        //gr.readFile();
        //this.graph = gr.getInfo();

        //ShortestPath sp = new ShortestPath();
        //sp.buildRoute(1,4, this.graph);
        // initialize the rest

    }

    @Override
    void createAgentCars() {
        for(Car car : this.cars) {
            CarAgent carAgent = new CarAgent(car.getName());
            try {
                this.agentController = this.containerController.acceptNewAgent(car.getName(), carAgent);
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
        // todo
    }

    public static void main(String[] args) throws FileNotFoundException {
        cityManager cityManager = new cityManager();
        cityManager.createAgentCars();
    }

}
