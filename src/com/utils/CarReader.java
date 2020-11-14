package com.utils;
import com.Data.Car;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class CarReader extends FileReader {

    final int NAME_NODE = 0;
    final int SRC_NODE = 1;
    final int DEST_NODE = 2;
    final int STRATEGY = 3;
    final int LENGTH = 4;


    HashSet<Car> cars = new HashSet<>();

    public CarReader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readLine(String[] line) {

        String strategy = line[STRATEGY];
        Car.Strategy routeStrategy = null;
        if (strategy.equalsIgnoreCase("DIJKSTRA")) {
            routeStrategy = Car.Strategy.SHORTEST_PATH;
        } else if (strategy.equalsIgnoreCase("shortestTime")) {
            routeStrategy = Car.Strategy.SHORTEST_TIME;
        } else if (strategy.equalsIgnoreCase("minIntersection")){
            routeStrategy = Car.Strategy.MINIMUM_INTERSECTIONS;
        }
        else {
            System.out.println("Unknown strategy " +  strategy + ". Not creating the agent...");
        }


        cars.add(new Car(line[NAME_NODE],
                Integer.parseInt(line[SRC_NODE]),
                Integer.parseInt(line[DEST_NODE]),
                Float.parseFloat(line[LENGTH]),
                routeStrategy));
    }

    @Override
    public HashSet<Car> getInfo() {
        for(Car car : this.cars) {
            car.print();
        }
        return cars;
    }
}
