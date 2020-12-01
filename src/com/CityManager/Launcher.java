package com.CityManager;

import com.Agent.CarAgent;
import com.Agent.CityAgent;
import com.Agent.PriorityCarAgent;
import com.Agent.RoadAgent;
import com.Data.*;
import com.utils.*;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;

import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Launcher extends Repast3Launcher {

    private ContainerController mainContainer;
    String folder;

    /* devemos usar estes dois parametros? */
    public static final int TICKS_IN_HOUR = 30;
    private Schedule schedule;

    public Launcher(String arg) {
        folder = arg;
    }

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "Traffic manager city";
    }

    @Override
    protected void launchJADE() {

        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        mainContainer = rt.createMainContainer(p1);

        /*
        if(SEPARATE_CONTAINERS) {
            Profile p2 = new ProfileImpl();
            agentContainer = rt.createAgentContainer(p2);
        } else {
            agentContainer = mainContainer;
        }*/

        try { launchAgents("exp1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void launchAgents(String arg) throws FileNotFoundException {


       Graph graph;
       HashSet<Car> cars = new HashSet<>();
       HashSet<PriorityCar> priorityCars = new HashSet<>();
       HashSet<RoadInfo> roads = new HashSet<>();
       HashMap<String, Float> weatherVelocityRestriction;
       HashMap<Integer, String> weather = new HashMap<>();

        CarReader r = new CarReader(arg + "/car.txt");
        r.readFile();
        cars = r.getInfo();

        PriorityCarReader pr = new PriorityCarReader(arg + "/priorityCars.txt");
        pr.readFile();
        priorityCars = pr.getInfo();

        TypeWeatherReader twr = new TypeWeatherReader(arg + "/typeWeather.txt");
        twr.readFile();
        weatherVelocityRestriction = twr.getInfo();

        WeatherReader wr = new WeatherReader(arg + "/weather.txt");
        wr.readFile();
        weather = wr.getInfo();

        GraphReader gr = new GraphReader(arg + "/city.txt");
        gr.readFile();
        graph = gr.getInfo();


        // AID resultsCollectorAID = null;



        for(Car car : cars) {
            CarAgent carAgent = new CarAgent(car);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        WeatherStation weatherStation = new WeatherStation(weatherVelocityRestriction,weather);
        CityAgent cityAgent = new CityAgent(weatherStation, graph);
        try {
            mainContainer.acceptNewAgent("city", cityAgent).start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : graph.getEdges().entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadAgent roadAgent = new RoadAgent(adj.getValue());
                try {
                    mainContainer.acceptNewAgent("road" + src + "-" + dest, roadAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }

        for(PriorityCar car : priorityCars) {
            PriorityCarAgent carAgent = new PriorityCarAgent(car, graph);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Launching Repast3
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length != 1) {
            return;
        }
        System.out.println("Arg here :" + args[0]);
        File dir = new File(args[0] + "/");
        if (!dir.exists()){
            System.err.println("Folder does not exist");
            return;
        }

        //CityManager cityManager = new CityManager(args[0]);
        //CheckValidaty X = new CheckValidaty(cityManager.graph.getEdges());
        //if (!X.validateNodeNr()) return;
        //if (!X.validateCars(cityManager.cars)) return;
        //if (!X.validatePriorityCars(cityManager.priorityCars)) return;
        //if (!X.check()) return;

        //cityManager.cleanLogFiles();

       // cityManager.createCity();
       // cityManager.createAgentRoads();
       // cityManager.createAgentCars();
       // cityManager.createPriorityCars();
        //System.out.println("city manager running...");


        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        init.loadModel(new Launcher(args[0]), null, false);

    }

}

