package com.Actions;

import com.Agent.CarAgent;
import com.CityManager.Launcher;
import com.Data.Car;
import com.utils.CarFactory;
import jade.wrapper.StaleProxyException;
import uchicago.src.sim.engine.BasicAction;

import java.util.Random;

public class CreateCars extends BasicAction {
    private final Launcher launcher;
    private int numberMinIntersections;
    private int numberShortestTime;
    private int numberShortestPath;

    public CreateCars(int numberMinIntersections, int numberShortestTime, int numberShortestPath, Launcher launcher) {
        this.numberMinIntersections =numberMinIntersections;
        this.numberShortestTime = numberShortestTime;
        this.numberShortestPath = numberShortestPath;
        this.launcher = launcher;
    }

    @Override
    public void execute() {
        if(new Random().nextDouble() < launcher.getProbabilityAddCar()) {
            for (int i = 0; i < this.numberShortestTime; i++) {
                Car car = CarFactory.buildCar(Car.Strategy.SHORTEST_TIME);
                CarAgent carAgent = new CarAgent(car);
                launcher.addCarAgent(carAgent);
                try {
                    launcher.getMainContainer().acceptNewAgent(car.getName(), carAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < this.numberShortestPath; i++) {
                Car car = CarFactory.buildCar(Car.Strategy.SHORTEST_PATH);
                CarAgent carAgent = new CarAgent(car);
                launcher.addCarAgent(carAgent);
                try {
                    launcher.getMainContainer().acceptNewAgent(car.getName(), carAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < this.numberMinIntersections; i++) {
                Car car = CarFactory.buildCar(Car.Strategy.MINIMUM_INTERSECTIONS);
                CarAgent carAgent = new CarAgent(car);
                launcher.addCarAgent(carAgent);
                try {
                    launcher.getMainContainer().acceptNewAgent(car.getName(), carAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
