package com.CityManager;

import com.Agent.CarAgent;
import com.Agent.CityAgent;
import com.Agent.PriorityCarAgent;
import com.Agent.RoadAgent;
import com.Data.*;
import com.utils.*;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.ScheduleBase;
import uchicago.src.sim.engine.SimInit;

import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class Launcher extends Repast3Launcher {

    private ContainerController mainContainer;
    private String folder;

    // viz
    private static List<DefaultDrawableNode> nodesViz;
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private DisplaySurface surf;
    private Color LOW = Color.green;
    private Color MEDIUM = Color.orange;
    private Color HIGH = Color.red;
    HashMap<Integer, DefaultDrawableNode> nodestoDraw = new HashMap<>();


    private boolean runInBatchMode;
    private OpenSequenceGraph plotNumberCars;
    private OpenSequenceGraph plotCity;
    private OpenSequenceGraph plotVelocity;
    private OpenSequenceGraph plotRoads;
    private OpenSequenceGraph plotNrIntersections;
    private OpenSequenceGraph plotCars;
    private OpenSequenceGraph plotDistanceTraveled;
    private Configuration config;
    public static ArrayList<Integer> nodes;
    private ArrayList<CarAgent> carAgents = new ArrayList<>();
    private ArrayList<PriorityCarAgent> priorityCarAgents = new ArrayList<>();
    private ArrayList<RoadAgent> roadAgents;
    private CityAgent cityAgent;

    public Launcher(String arg, Configuration config, boolean runMode) {
        super();
        folder = arg;
        this.runInBatchMode = runMode;
        this.config = config;
    }

    public void changeColor (int src, int dest, int capacity_percentage) {
        this.nodestoDraw.get(src).removeEdgesTo(this.nodestoDraw.get(dest));
        EdgeDrawable edge = new EdgeDrawable(nodestoDraw.get(src), nodestoDraw.get(dest));
        if (capacity_percentage <= 20) {
            edge.setColor(LOW);
        } else if (capacity_percentage <= 85) {
            edge.setColor(MEDIUM);
        } else {
            edge.setColor(HIGH);
        }
        this.nodestoDraw.get(src).addOutEdge(edge);
    }

    @Override
    public String[] getInitParam() {
        return new String[]{ "fileOfWeather","fileOfTypeWeather", "fileOfCity" ,"numberPriorityCars", "numberShortestTimeCar" , "numberShortestPathCar", "numberMinIntersectionCar"};
    }

    //viz
    public static DefaultDrawableNode getNode(String label) {
        for(DefaultDrawableNode node : nodesViz) {
            if(node.getNodeLabel().equals(label)) {
                return node;
            }
        }
        return null;
    }

    public String getFileOfWeather() {
        return config.getFileOfWeather();
    }

    public void setFileOfWeather(String fileOfWeather) {
        this.config.setFileOfWeather(fileOfWeather);
    }

    public String getFileOfTypeWeather() {
        return config.getFileOfTypeWeather();
    }

    public void setFileOfTypeWeather(String fileOfTypeWeather) {
        this.config.setFileOfTypeWeather(fileOfTypeWeather);
    }


    public String getFileOfCity() {
        return config.getFileofCity();
    }

    public void setFileOfCity(String fileofCity) {
        this.config.setFileofCity(fileofCity);
    }

    public void setNumberShortestTimeCar(int cars) {
        this.config.setNumberShortestTimeCar(cars);
    }

    public int getNumberShortestTimeCar() {
        return config.getNumberShortestTimeCar();
    }

    public void setNumberPriorityCars(int cars) {
        this.config.setNumberPriorityCars(cars);
    }

    public int getNumberPriorityCars() {
        return config.getPriorityNumberCars();
    }

    public int getNumberMinIntersectionCar() {
        return config.getNumberMinIntersectionCar();
    }

    public void setNumberMinIntersectionCar(int numberMinIntersectionCar) {
        this.config.setNumberMinIntersectionCar(numberMinIntersectionCar);
    }

    public int getNumberShortestPathCar() {
        return config.getNumberShortestPathCar();
    }

    public void setNumberShortestPathCar(int numberShortestPathCar) {
        this.config.setNumberShortestPathCar(numberShortestPathCar);
    }

    @Override
    public String getName() {
        return "Traffic manager city";
    }

    @Override
    protected void launchJADE() {
        try {
            Runtime rt = Runtime.instance();
            Profile p1 = new ProfileImpl();
            mainContainer = rt.createMainContainer(p1);
            launchAgents();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*
        if(SEPARATE_CONTAINERS) {
            Profile p2 = new ProfileImpl();
            agentContainer = rt.createAgentContainer(p2);
        } else {
            agentContainer = mainContainer;
        }*/
    }


    private DefaultDrawableNode generateNode(String label, Color color, int x, int y) {
        OvalNetworkItem oval = new OvalNetworkItem(x,y);
        oval.allowResizing(false);
        oval.setHeight(5);
        oval.setWidth(5);

        DefaultDrawableNode node = new DefaultDrawableNode(label, oval);
        node.setColor(color);

        return node;
    }

    private void launchAgents() throws FileNotFoundException {
        // viz
        Random random = new Random(System.currentTimeMillis());
        nodesViz = new ArrayList<DefaultDrawableNode>();


        GraphReader gr = new GraphReader(this.getFileOfCity());
        gr.readFile();
        Graph graph = gr.getInfo();
        Launcher.nodes = new ArrayList<>(graph.getEdges().keySet()); // todo: isto pode nao dar os nodes todos

        TypeWeatherReader twr = new TypeWeatherReader(this.getFileOfTypeWeather());
        twr.readFile();
        HashMap<String, Float> weatherVelocityRestriction = twr.getInfo();

        WeatherReader wr = new WeatherReader(this.getFileOfWeather());
        wr.readFile();
        HashMap<Integer, String> weather = wr.getInfo();

        WeatherStation weatherStation = new WeatherStation(weatherVelocityRestriction,weather);
        cityAgent = new CityAgent(weatherStation, graph);
        try {
            mainContainer.acceptNewAgent("city", cityAgent).start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }



        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : graph.getEdges().entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            nodestoDraw.putIfAbsent(src, generateNode( src + "", Color.WHITE,
                    random.nextInt(WIDTH/2),random.nextInt(HEIGHT/2)));

            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadAgent roadAgent = new RoadAgent(adj.getValue());
                nodestoDraw.putIfAbsent(dest,generateNode( dest + "", Color.WHITE,
                        random.nextInt(WIDTH/2),random.nextInt(HEIGHT/2)));

                DefaultDrawableNode srcNode = nodestoDraw.get(src);
                DefaultDrawableNode destNode = nodestoDraw.get(dest);

                nodesViz.add(srcNode);
                nodesViz.add(destNode);

                EdgeDrawable edge = new EdgeDrawable(srcNode,  destNode);
                edge.setColor(Color.ORANGE);
                srcNode.addOutEdge(edge);

                try {
                    mainContainer.acceptNewAgent("road" + src + "-" + dest, roadAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i = 0; i < this.getNumberShortestTimeCar(); i++) {
            Car car = CarFactory.buildCar(Car.Strategy.SHORTEST_TIME);
            CarAgent carAgent = new CarAgent(car);
            carAgents.add(carAgent);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < this.getNumberShortestPathCar(); i++) {
            Car car = CarFactory.buildCar(Car.Strategy.SHORTEST_PATH);
            CarAgent carAgent = new CarAgent(car);
            carAgents.add(carAgent);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < this.getNumberMinIntersectionCar(); i++) {
            Car car = CarFactory.buildCar(Car.Strategy.MINIMUM_INTERSECTIONS);
            CarAgent carAgent = new CarAgent(car);
            carAgents.add(carAgent);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i< this.getNumberPriorityCars(); i++) {
            PriorityCar car = CarFactory.buildPriorityCar();
            PriorityCarAgent carAgent = new PriorityCarAgent(car, graph);
            priorityCarAgents.add(carAgent);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void begin() {
        super.begin();
        if(!runInBatchMode) {
           // buildAndScheduleDisplayVelocity();
           // buildAndScheduleDisplayIntersections();
            //buildAndScheduleDisplayNumberCars();
            //buildAndScheduleDisplayDistanceTraveled(); //todo
            if (surf != null) surf.dispose();
            surf = new DisplaySurface(this, "City Display");
            registerDisplaySurface("City Display", surf);
            Network2DDisplay display = new Network2DDisplay(nodesViz, WIDTH,HEIGHT);
            surf.addDisplayableProbeable(display, "Network Display");
            surf.addZoomable(display);
            addSimEventListener(surf); // why this????

            surf.display();
            getSchedule().scheduleActionAtInterval(500, surf, "updateDisplay", Schedule.LAST);

        }
    }
    private void printAndSchedule(double v, OpenSequenceGraph o , String s, ScheduleBase.Order order) {

        o.display();
        getSchedule().scheduleActionAtInterval(v, o, s, order);
    }

    /**
     * todo
     * */
    private void buildAndScheduleDisplayDistanceTraveled() {
        if (plotDistanceTraveled != null) plotDistanceTraveled.dispose();
        plotDistanceTraveled = new OpenSequenceGraph("Avg distance travelled", this);
        plotDistanceTraveled.addSequence("Min Intersection strategy", new Sequence() {
            @Override
            public double getSValue() {
                return 0;
            };
        });
    }

    private void buildAndScheduleDisplayIntersections() {
        if (plotNrIntersections != null) plotNrIntersections.dispose();
        plotNrIntersections = new OpenSequenceGraph("Avg Nr Intersections", this);

        plotNrIntersections.addSequence("Min intersection strategy", new Sequence() {
            @Override
            public double getSValue() {
                return (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                        / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count();
            }
        });
        plotNrIntersections.addSequence("Min distance strategy", new Sequence() {
            @Override
            public double getSValue() {
                return (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                        / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).count();
            }
        });

        plotNrIntersections.addSequence("Min time strategy", new Sequence() {
            @Override
            public double getSValue() {
                return (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                        / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count();
            }
        });

        printAndSchedule(100, plotNrIntersections, "step", Schedule.LAST);

    }


    private void buildAndScheduleDisplayVelocity() {
        if (plotVelocity != null) plotVelocity.dispose();
        plotVelocity = new OpenSequenceGraph("Velocities", this);

        plotVelocity.addSequence("Average velocity of cars", new Sequence() {
            @Override
            public double getSValue() {
                return carAgents.stream().mapToDouble(f -> f.getCar().getCurrentVelocity()).sum() / carAgents.size();
            }
        });

        plotVelocity.addSequence("Average velocity of priority cars", new Sequence() {
            @Override
            public double getSValue() {
                return priorityCarAgents.stream().mapToDouble(f -> f.getCar().getCurrentVelocity()).sum()/priorityCarAgents.size();
            }
        });
       printAndSchedule(100, plotVelocity, "step", Schedule.LAST);

    }

    private void buildAndScheduleDisplayCity() {
        if (plotCity != null) plotCity.dispose();
        plotCity = new OpenSequenceGraph("City", this);
        plotCity.setAxisTitles("time", "weather");


        plotCity.addSequence("Percentage of max velocity", new Sequence() {
            public double getSValue() {
                return cityAgent.getWeatherStation().getVelocity(cityAgent.getWeatherStation().getCurrentWeather());
            }
        });

        printAndSchedule(100, plotCity, "step", Schedule.LAST);
    }

    private void buildAndScheduleDisplayNumberCars() {
        if (plotNumberCars != null) plotNumberCars.dispose();
        plotNumberCars = new OpenSequenceGraph("Cars Information", this);
        plotNumberCars.setAxisTitles("time", "Nº Cars");

        plotNumberCars.addSequence("Number Stopped Cars", new Sequence() {
            public double getSValue() {
                return carAgents.stream().filter(c -> c.getCar().getCurrentVelocity() == 0).count() +
                        priorityCarAgents.stream().filter(c -> c.getCar().getCurrentVelocity() == 0).count();

            }
        });

        plotNumberCars.addSequence("Number not Stopped Cars", new Sequence() {
            public double getSValue() {
                return carAgents.stream().filter(c -> c.getCar().getCurrentVelocity() > 0).count() +
                        priorityCarAgents.stream().filter(c -> c.getCar().getCurrentVelocity() > 0).count();

            }
        });

        plotNumberCars.addSequence("Number of Cars in dest node", new Sequence() {
            public double getSValue() {
                return (int) carAgents.stream().filter(c -> c.getCar().getCurrentNode() == c.getCar().getDestNode()).count();

            }
        });

        plotNumberCars.addSequence("Number of Priority Cars in dest node", new Sequence() {
            public double getSValue() {
                return (int) priorityCarAgents.stream().filter(c -> c.getCar().getCurrentNode() == c.getCar().getDestNode()).count();

            }
        });

        plotNumberCars.display();
        getSchedule().scheduleActionAtInterval(100, plotNumberCars, "step", Schedule.LAST);
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

        File dir2 = new File("logs/");
        if (!dir2.exists()){
            dir2.mkdir();
        }
        for (File file: dir2.listFiles())
            if (!file.isDirectory())
                file.delete();


        SimInit init = new SimInit();
        Configuration config = new Configuration(args);
        init.setNumRuns(1);   // works only in batch mode
        init.loadModel(new Launcher(args[0], config, config.getBatchMode()), null, false);
    }


}

