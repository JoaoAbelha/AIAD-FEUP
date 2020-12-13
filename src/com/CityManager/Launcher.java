package com.CityManager;

import com.Actions.ChangeWeather;
import com.Actions.CreateCars;
import com.Actions.CreatePriorityCars;
import com.Agent.CarAgent;
import com.Agent.CityAgent;
import com.Agent.PriorityCarAgent;
import com.Agent.RoadAgent;
import com.Data.*;
import com.utils.*;
import uchicago.src.sim.analysis.DataRecorder;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;import uchicago.src.sim.engine.ActionGroup;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;


public class Launcher extends Repast3Launcher {

    private ContainerController mainContainer;
    private String folder;
    private boolean specialCars;

    // viz
    private static List<DefaultDrawableNode> nodesViz;
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private DisplaySurface surf;
    private static HashMap<Integer, DefaultDrawableNode> nodestoDraw = new HashMap<>();

    private CarAgent carAgentSpecial;
    private PriorityCarAgent priorityCarAgentSpecial;
    private boolean runInBatchMode;
    private OpenSequenceGraph plotNumberCars;
    private OpenSequenceGraph plotNrIntersections;
    private OpenSequenceGraph plotDistanceTraveled;
    private Configuration config;
    public static ArrayList<Integer> nodes; // bug in here
    private ArrayList<CarAgent> carAgents = new ArrayList<>();
    private ArrayList<PriorityCarAgent> priorityCarAgents = new ArrayList<>();
    private ArrayList<RoadAgent> roadAgents = new ArrayList<>();
    private CityAgent cityAgent;
    private Graph graph;

    public Launcher(String arg, Configuration config, boolean runMode, boolean specialCars) {
        super();
        folder = arg;
        this.runInBatchMode = runMode;
        this.config = config;
        this.specialCars = specialCars;
    }

    @Override
    public String[] getInitParam() {
        return new String[]{"priorityCarToFollow" ,"carToFollow", "fileOfCars", "fileOfPriorityCars", "fileOfTypeWeather", "fileOfCity" ,"numberPriorityCars",
                "numberShortestTimeCar" , "numberShortestPathCar", "numberMinIntersectionCar", "probabilityChangeWeather", "probabilityAddCar", "probabilityAddPriorityCar", "tickIntervalMakeCar", "tickIntervalChangeWeather"};
    }

    public String getPriorityCarToFollow() {
        return config.getPrioritycarToFollow();
    }

    public void setPriorityCarToFollow(String prioritycarToFollow) {
        this.config.setPrioritycarToFollow(prioritycarToFollow);
    }

    public String getCarToFollow() {
        return config.getCarToFollow();
    }

    public void setCarToFollow(String carToFollow) {
        this.config.setCarToFollow(carToFollow);
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

    public String getFileOfCars() {
        return config.getCars();
    }

    public void setFileOfCars(String fileOfCars) {
        this.config.setCars(fileOfCars);
    }

    public String getFileOfPriorityCars() {
        return config.getPriorityCars();
    }

    public void setFileOfPriorityCars(String fileOfPriorityCars) {
        this.config.setPriorityCars(fileOfPriorityCars);
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

    public double getProbabilityChangeWeather() {
        return config.getProbabilityChangeWeather();
    }

    public void setProbabilityChangeWeather(double probabilityChangeWeather) {
        config.setProbabilityChangeWeather(probabilityChangeWeather);
    }

    public double getProbabilityAddCar() {
        return config.getProbabilityAddCar();
    }

    public void setProbabilityAddCar(double probabilityAddCar) {
        config.setProbabilityAddCar(probabilityAddCar);
    }

    public double getProbabilityAddPriorityCar() {
        return config.getProbabilityAddPriorityCar();
    }

    public void setProbabilityAddPriorityCar(double probabilityAddPriorityCar) {
        config.setProbabilityAddPriorityCar(probabilityAddPriorityCar);
    }

    public int getTickIntervalMakeCar() {
        return config.getTickIntervalMakeCars();
    }

    public void setTickIntervalMakeCar(int tickIntervalMakeCar) {
        this.config.setTickIntervalMakeCars(tickIntervalMakeCar);
    }

    public int getTickIntervalChangeWeather() {
        return config.getTickIntervalChangeWeather();
    }

    public void setTickIntervalChangeWeather(int tickIntervalChangeWeather) {
        this.config.setTickIntervalChangeWeather(tickIntervalChangeWeather);
    }

    public void addCarAgent(CarAgent carAgent) {
        this.carAgents.add(carAgent);
    }

    public void addPriorityCarAgent(PriorityCarAgent carAgent) {
        this.priorityCarAgents.add(carAgent);
    }

    public ContainerController getMainContainer() {
        return mainContainer;
    }

    public Graph getGraph() {
        return graph;
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

    private void createSpecialCar() {
        if(this.getCarToFollow() == null) return;
        String [] carToFollowSpecs = this.getCarToFollow().split(" ");
        Car.Strategy st;
        switch (carToFollowSpecs[2].toLowerCase()) {
            case "dijkstra"-> {st = Car.Strategy.SHORTEST_PATH;}
            case "minIntersection" -> {st = Car.Strategy.MINIMUM_INTERSECTIONS;}
            case "shortestTime" -> {st = Car.Strategy.SHORTEST_TIME;}
            default -> {
                System.out.println("invalid strategy for the car. Not creating it");
                return;
            }
        }

        Car carSpecial = new Car("specialCar", Integer.parseInt(carToFollowSpecs[0]), Integer.parseInt(carToFollowSpecs[1]),
                Float.parseFloat(carToFollowSpecs[3]) , st);
        carAgentSpecial = new CarAgent(carSpecial);
        carAgents.add(carAgentSpecial);

        try {
            mainContainer.acceptNewAgent(carSpecial.getName(), carAgentSpecial).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void createPriorityCarSpecial(Graph graph) {
        if(this.getPriorityCarToFollow() == null) return;
        String [] carToFollowSpecs = this.getPriorityCarToFollow().split(" ");
        PriorityCar priorityCar = new PriorityCar("priorityCarSpecial", Integer.parseInt(carToFollowSpecs[0]), Integer.parseInt( carToFollowSpecs[1]));
        priorityCarAgentSpecial = new PriorityCarAgent(priorityCar, graph);
        priorityCarAgents.add(priorityCarAgentSpecial);

        try {
            mainContainer.acceptNewAgent(priorityCar.getName(), priorityCarAgentSpecial).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void launchAgents() throws FileNotFoundException {
        // viz
        Random random = new Random(System.currentTimeMillis());
        nodesViz = new ArrayList<DefaultDrawableNode>();

        // read input
        GraphReader gr = new GraphReader(this.getFileOfCity());
        gr.readFile();
        graph = gr.getInfo();

        CarReader r = new CarReader(this.getFileOfCars());
        r.readFile();
        HashSet<Car> cars = r.getInfo();

        PriorityCarReader pr = new PriorityCarReader(this.getFileOfPriorityCars());
        pr.readFile();
        HashSet<PriorityCar> priorityCars = pr.getInfo();

        TypeWeatherReader twr = new TypeWeatherReader(this.getFileOfTypeWeather());
        twr.readFile();
        HashMap<String, Float> weatherVelocityRestriction = twr.getInfo();

        WeatherStation weatherStation = new WeatherStation(weatherVelocityRestriction);
        cityAgent = new CityAgent(weatherStation, graph);
        try {
            mainContainer.acceptNewAgent("city", cityAgent).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }


        HashSet<Integer> nodesSet = new HashSet<>();
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : graph.getEdges().entrySet()) {
            Integer src = entry.getKey();
            nodesSet.add(src);
            Map<Integer, RoadInfo> value = entry.getValue();
            nodestoDraw.putIfAbsent(src, generateNode( src + "", Color.WHITE,
                    random.nextInt(WIDTH),random.nextInt(HEIGHT)));

            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                nodesSet.add(dest);
                nodestoDraw.putIfAbsent(dest,generateNode( dest + "", Color.WHITE,
                        random.nextInt(WIDTH),random.nextInt(HEIGHT)));

                DefaultDrawableNode srcNode = nodestoDraw.get(src);
                DefaultDrawableNode destNode = nodestoDraw.get(dest);

                nodesViz.add(srcNode);
                nodesViz.add(destNode);

                EdgeDrawable edge = new EdgeDrawable(srcNode,  destNode);
                edge.setColor(Color.WHITE);
                srcNode.addOutEdge(edge);
                RoadAgent roadAgent = new RoadAgent(adj.getValue(), edge);
                roadAgents.add(roadAgent);

                try {
                    mainContainer.acceptNewAgent("road" + src + "-" + dest, roadAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }

        Launcher.nodes = new ArrayList<>(nodesSet);
        createSpecialCar();
        createPriorityCarSpecial(graph);

        for(Car car : cars) {
            CarAgent carAgent = new CarAgent(car);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
                carAgents.add(carAgent);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }

        for(PriorityCar car : priorityCars) {
            PriorityCarAgent carAgent = new PriorityCarAgent(car, this.graph);
            try {
                mainContainer.acceptNewAgent(car.getName(), carAgent).start();
                priorityCarAgents.add(carAgent);
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Stream<CarAgent> getDrivingCars() {
        return carAgents.stream().filter(c -> c.getCar().getCurrentNode() != c.getCar().getDestNode());
    }

    private Stream<PriorityCarAgent> getDrivingPriorityCars() {
        return priorityCarAgents.stream().filter(c -> c.getCar().getCurrentNode() != c.getCar().getDestNode());
    }

    /**
     * todo : se calhar por a guardar de mais em mais tempo
     */
    public void trackVelocity() {
        DataRecorder recorder= new DataRecorder("./velocity.csv", this, "A comment");
        recorder.setDelimeter(";");

        recorder.addNumericDataSource("Avg velocity allowed",
                () -> cityAgent.getMaxVelocity().entrySet().size() > 0 ? Launcher.round(cityAgent.getMaxVelocity().values().stream().mapToDouble(f->f).sum() / cityAgent.getMaxVelocity().entrySet().size(), 3) : 0);

        recorder.addNumericDataSource("Avg velocity of normal cars",
                () -> getDrivingCars().count() > 0 ? Launcher.round( getDrivingCars().mapToDouble(f -> f.getCar().getCurrentVelocity()).sum() / getDrivingCars().count(), 3) : 0);

        recorder.addNumericDataSource("Avg velocity of priority cars",
                () -> getDrivingPriorityCars().count() > 0 ? Launcher.round(getDrivingPriorityCars().mapToDouble(f ->f.getCar().getCurrentVelocity()).sum() / getDrivingPriorityCars().count(), 3) : 0);

        recorder.addNumericDataSource("Weather factor velocity",
                () -> Launcher.round(cityAgent.getWeatherStation().getVelocity(cityAgent.getWeatherStation().getCurrentWeather()), 3));

        getSchedule().scheduleActionBeginning(0, new BasicAction() {
            public void execute() {
                recorder.record();
            }
        });
        getSchedule().scheduleActionAtEnd(recorder, "writeToFile");
    }

    public void trackOccupationOfRoads() {
        DataRecorder recorder= new DataRecorder("./roadOccupation.csv", this);
        recorder.setDelimeter(";");
        recorder.addNumericDataSource("AvgOccupancy",
                () -> roadAgents.size() > 0 ? Launcher.round(
                        roadAgents.stream().mapToDouble(f->f.getSpaceOccupied()/f.getRoadInfo().getDistance()).sum() / roadAgents.size()
                        , 3) : 0);

        getSchedule().scheduleActionBeginning(0, new BasicAction() {
            public void execute() {
                recorder.record();
            }
        });
        getSchedule().scheduleActionAtEnd(recorder, "writeToFile");
    }


    @Override
    public void begin() {
        super.begin();
        if(!runInBatchMode) {
            trackVelocity();
            trackOccupationOfRoads();
            
            if (surf != null) surf.dispose();
            surf = new DisplaySurface(this, "City Display");
            registerDisplaySurface("City Display", surf);
            Network2DDisplay display = new Network2DDisplay(nodesViz, WIDTH,HEIGHT);
            surf.addDisplayableProbeable(display, "Network Display");
            surf.addZoomable(display);
            addSimEventListener(surf); // why this????

            surf.display(); 
            getSchedule().scheduleActionAtInterval(25, surf, "updateDisplay", Schedule.LAST);

            buildAndScheduleDisplayDistanceTraveled();
            buildAndScheduleDisplayIntersections();
            buildAndScheduleDisplayNumberCars();
        }

        buildSchedule();
    }

    private void printAndSchedule(double v, OpenSequenceGraph o , String s, ScheduleBase.Order order) {
        o.display();
        getSchedule().scheduleActionAtInterval(v, o, s, order);
    }

    private void buildSchedule() {
        CreateCars createCars = new CreateCars(this.getNumberMinIntersectionCar(), this.getNumberShortestTimeCar(), this.getNumberShortestPathCar(), this);
        CreatePriorityCars createPriorityCars = new CreatePriorityCars(this.getNumberPriorityCars(), this);
        ChangeWeather changeWeather = new ChangeWeather(cityAgent, getProbabilityChangeWeather());
        boolean scheduleCars = false;

        ActionGroup group = new ActionGroup(ActionGroup.SEQUENTIAL);

        if(this.getNumberMinIntersectionCar() + this.getNumberShortestPathCar() + this.getNumberShortestTimeCar() != 0) {
            group.addAction(createCars);
            scheduleCars = true;
        }
        if(this.getNumberPriorityCars() != 0) {
            group.addAction(createPriorityCars);
            scheduleCars = true;
        }

        if(this.getTickIntervalMakeCar() < 200) {
            System.out.println("Please insert a tick interval greater or equal than 200. Using default 200 ticks interval.");
            this.setTickIntervalMakeCar(200);
        }
        if(this.getTickIntervalChangeWeather() < 200) {
            System.out.println("Please insert a tick interval greater or equal than 200. Using default 200 ticks interval.");
            this.setTickIntervalChangeWeather(200);
        }

        if(scheduleCars) {
            getSchedule().scheduleActionAtInterval(this.getTickIntervalMakeCar(), group);
        }
        if(getProbabilityChangeWeather() != 0d) {
            getSchedule().scheduleActionAtInterval(this.getTickIntervalChangeWeather(), changeWeather);
        }
    }

    private void addAverageCarsDistanceSequences() {
        plotDistanceTraveled.addSequence("Min intersection strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count() > 0) {
                    value = carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).mapToDouble(f ->f.getCar().getDistanceUntilNow() + f.getCar().getCurrentDistanceTravelled()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count();
                }
                return value;
            }
        });
        plotDistanceTraveled.addSequence("Min distance strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).count() > 0) {
                    value = carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).mapToDouble(f ->f.getCar().getDistanceUntilNow() + f.getCar().getCurrentDistanceTravelled()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).count();
                }
                return value;
            }
        });
        plotDistanceTraveled.addSequence("Min time strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).count() > 0) {
                    value = carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).mapToDouble(f ->f.getCar().getDistanceUntilNow() + f.getCar().getCurrentDistanceTravelled()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).count();
                }
                return value;
            }
        });
    }

    private void addSpecialCarDistanceSequence() {
        plotDistanceTraveled.addSequence("Special Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgentSpecial != null) {
                    value = carAgentSpecial.getCar().getDistanceUntilNow() + carAgentSpecial.getCar().getCurrentDistanceTravelled();
                }
                return value;
            }
        });
        plotDistanceTraveled.addSequence("Special Priority Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(priorityCarAgentSpecial != null) {
                    value = priorityCarAgentSpecial.getCar().getDistanceUntilNow() + priorityCarAgentSpecial.getCar().getCurrentDistanceTravelled();
                }
                return value;
            }
        });
    }

    private void buildAndScheduleDisplayDistanceTraveled() {
        if (plotDistanceTraveled != null) plotDistanceTraveled.dispose();
        plotDistanceTraveled = new OpenSequenceGraph("Avg distance travelled", this);
        if(!specialCars) {
            addAverageCarsDistanceSequences();
        } else {
            addSpecialCarDistanceSequence();
        }

        getSchedule().scheduleActionAtEnd(new BasicAction() {
            @Override
            public void execute() {
                plotDistanceTraveled.writeToFile();
            }
        });

        printAndSchedule(100, plotDistanceTraveled, "step", Schedule.LAST);
    }

    private void addAverageCarsIntersectionsSequences() {
        plotNrIntersections.addSequence("Min intersection strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count() > 0) {
                    value = (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS)).count();
                }
                return value;
            }
        });
        plotNrIntersections.addSequence("Min distance strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).count() > 0) {
                    value = (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH)).count();
                }
                return value;
            }
        });
        plotNrIntersections.addSequence("Min time strategy", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).count() > 0) {
                    value = (double )carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).mapToInt(f ->f.getCar().getNumberIntersections()).sum()
                            / carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME)).count();
                }
                return value;
            }
        });
    }

    private void addSpecialCarIntersectionsSequence() {
        plotNrIntersections.addSequence("Special Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgentSpecial != null) {
                    value = carAgentSpecial.getCar().getNumberIntersections();
                }
                return value;
            }
        });
        plotNrIntersections.addSequence("Special Priority Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(priorityCarAgentSpecial != null) {
                    value = priorityCarAgentSpecial.getCar().getNumberIntersections();
                }
                return value;
            }
        });
    }

    private void buildAndScheduleDisplayIntersections() {
        if (plotNrIntersections != null) plotNrIntersections.dispose();
        plotNrIntersections = new OpenSequenceGraph("Avg Nr Intersections", this);

        if(!specialCars) {
            addAverageCarsIntersectionsSequences();
        } else {
            addSpecialCarIntersectionsSequence();
        }

        getSchedule().scheduleActionAtEnd(new BasicAction() {
            @Override
            public void execute() {
                plotNrIntersections.writeToFile();
            }
        });

        printAndSchedule(100, plotNrIntersections, "step", Schedule.LAST);
    }

    private void addAverageCarsNumberSequences() {
        plotNumberCars.addSequence("Min intersection strategy", new Sequence() {
            @Override
            public double getSValue() {
                return carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.MINIMUM_INTERSECTIONS) && c.getCar().getCurrentNode() == c.getCar().getDestNode()).count();
            }
        });
        plotNumberCars.addSequence("Min distance strategy", new Sequence() {
            @Override
            public double getSValue() {
                return carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_PATH) && c.getCar().getCurrentNode() == c.getCar().getDestNode()).count();
            }
        });

        plotNumberCars.addSequence("Min time strategy", new Sequence() {
            @Override
            public double getSValue() {
                return carAgents.stream().filter(c -> c.getCar().getStrategy().equals(Car.Strategy.SHORTEST_TIME) && c.getCar().getCurrentNode() == c.getCar().getDestNode()).count();
            }
        });
    }

    private void addSpecialCarNumberSequence() {
        plotNumberCars.addSequence("Special Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(carAgentSpecial != null) {
                    value = carAgentSpecial.getCar().getCurrentNode() == carAgentSpecial.getCar().getDestNode() ? 1d : 0d;
                }
                return value;
            }
        });
        plotNumberCars.addSequence("Special Priority Car", new Sequence() {
            @Override
            public double getSValue() {
                double value = 0d;
                if(priorityCarAgentSpecial != null) {
                    value = priorityCarAgentSpecial.getCar().getCurrentNode() == priorityCarAgentSpecial.getCar().getDestNode() ? 1d : 0d;
                }
                return value;
            }
        });
    }

    private void buildAndScheduleDisplayNumberCars() {
        if (plotNumberCars != null) plotNumberCars.dispose();
        plotNumberCars = new OpenSequenceGraph("Cars Information", this);
        plotNumberCars.setAxisTitles("time", "Nº Cars");

        plotNumberCars.addSequence("Number Stopped Cars", new Sequence() {
            public double getSValue() {
                return getDrivingCars().filter(c -> c.getCar().getCurrentVelocity() == 0).count() +
                        getDrivingPriorityCars().filter(c -> c.getCar().getCurrentVelocity() == 0).count();

            }
        });

        if(!specialCars) {
            addAverageCarsNumberSequences();
        } else {
            addSpecialCarNumberSequence();
        }

        getSchedule().scheduleActionAtEnd(new BasicAction() {
            @Override
            public void execute() {
                plotNumberCars.writeToFile();
            }
        });

        printAndSchedule(100, plotNumberCars, "step", Schedule.LAST);
    }

    private static boolean checkFile(String f) {
        File file = new File(f);
        if (!file.exists()){
            System.out.println("File does not exist");
            return false;
        }
        return true;
    }

    /**
     * Launching Repast3
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException {
        boolean specialCars = false;
        if (args.length == 1) {
            if(!Launcher.checkFile(args[0])) return;
        } else if(args.length == 2) {
            if(!Launcher.checkFile(args[0])) return;
            if(!args[1].equals("-s")) {
                System.out.println("Invalid command line flag!");
                System.out.println("Flags:");
                System.out.println("-s: Display Special Cars data");
                return;
            } else {
                specialCars = true;
            }
        } else {
            System.out.println("Invalid number of arguments");
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
        init.loadModel(new Launcher(args[0], config, config.getBatchMode(), specialCars), null, false);
    }
}

