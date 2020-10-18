package com.CityManager;

import com.Data.Car;
import com.Data.Graph;
import com.utils.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;

public class cityManager {

    private Graph graph;
    private HashSet<Car> cars;
    private HashMap<String, Float> weatherVelocityRestriction;
    private HashMap<Integer, String> weather = new HashMap<>();

    public cityManager() throws FileNotFoundException {
        CarReader r = new CarReader("src/car.txt");
        r.readFile();
        this.cars = r.getInfo();

        GraphReader gr = new GraphReader("src/city.txt");
        gr.readFile();
        this.graph = gr.getInfo();

        ShortestPath sp = new ShortestPath();
        sp.buildRoute(1,4, this.graph);
        // initialize the rest

    }

    public static void main(String[] args) throws FileNotFoundException {
        cityManager cityManager = new cityManager();
    }

}
