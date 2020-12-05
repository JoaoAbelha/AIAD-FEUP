package com.CityManager;

import com.Agent.CarAgent;
import com.Agent.PriorityCarAgent;
import com.Agent.CityAgent;
import com.Agent.RoadAgent;
import com.Data.Car;
import com.Data.Graph;
import com.Data.PriorityCar;
import com.Data.RoadInfo;
import com.Data.WeatherStation;
import com.utils.*;
import jade.wrapper.StaleProxyException;

import java.io.File;
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

    public CityManager(String arg) throws FileNotFoundException {
        super();

        CarReader r = new CarReader(arg + "/car.txt");
        r.readFile();
        this.cars = r.getInfo();

        PriorityCarReader pr = new PriorityCarReader(arg + "/priorityCars.txt");
        pr.readFile();
        this.priorityCars = pr.getInfo();

        TypeWeatherReader twr = new TypeWeatherReader(arg + "/typeWeather.txt");
        twr.readFile();
        this.weatherVelocityRestriction = twr.getInfo();

        WeatherReader wr = new WeatherReader(arg + "/weather.txt");
        wr.readFile();
        this.weather = wr.getInfo();

        GraphReader gr = new GraphReader(arg + "/city.txt");
        gr.readFile();
        this.graph = gr.getInfo();
    }

    @Override
    void createAgentCars() {
        int unique = 0;
        for(Car car : this.cars) {
            CarAgent carAgent = new CarAgent(car);
            try {
                this.agentController = this.containerController.acceptNewAgent(car.getName(), carAgent);
                this.agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void createCity() {
        WeatherStation weatherStation = new WeatherStation(this.weatherVelocityRestriction, this.weather);
        CityAgent cityAgent = new CityAgent(weatherStation, this.graph);
        try {
            this.agentController = this.containerController.acceptNewAgent("city", cityAgent);
            this.agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
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
                    this.agentController = this.containerController.acceptNewAgent("road" + src + "-" + dest, roadAgent);

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
            PriorityCarAgent carAgent = new PriorityCarAgent(car, this.graph);
            try {
                this.agentController = this.containerController.acceptNewAgent(car.getName(), carAgent);
                this.agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 1) {
            System.err.println("Missing name of folder with input files");
            return;
        }

        File dir = new File(args[0] + "/");
        if (!dir.exists()){
            System.err.println("Folder does not exist");
            return ;
        }

        CityManager cityManager = new CityManager(args[0]);
        CheckValidaty X = new CheckValidaty(cityManager.graph.getEdges());
        if (!X.validateNodeNr()) return;
        if (!X.validateCars(cityManager.cars)) return;
        if (!X.validatePriorityCars(cityManager.priorityCars)) return;
        if (!X.check()) return;

        cityManager.cleanLogFiles();

        cityManager.createCity();
        cityManager.createAgentRoads();
        cityManager.createAgentCars();
        cityManager.createPriorityCars();
        System.out.println("city manager running...");
    }

    private void cleanLogFiles() {
        File dir = new File("logs/");
        if (!dir.exists()){
            dir.mkdir();
        }
        for (File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

}
