package com.Actions;

import com.Agent.PriorityCarAgent;
import com.CityManager.Launcher;
import com.Data.PriorityCar;
import com.utils.CarFactory;
import jade.wrapper.StaleProxyException;
import uchicago.src.sim.engine.BasicAction;

import java.util.Random;

public class CreatePriorityCars extends BasicAction {
    private final Launcher launcher;
    private int numberPriorityCars;
    private boolean addCars = true;


    public CreatePriorityCars(int numberPriorityCars, Launcher launcher, boolean addCars) {
        this.numberPriorityCars = numberPriorityCars;
        this.launcher = launcher;
        this.addCars = addCars;
    }

    @Override
    public void execute() {
        if(new Random().nextDouble() < launcher.getProbabilityAddPriorityCar()) {
            for (int i = 0; i < this.numberPriorityCars; i++) {
                PriorityCar car = CarFactory.buildPriorityCar();
                PriorityCarAgent carAgent = new PriorityCarAgent(car, launcher.getGraph());
                if (addCars)
                 launcher.addPriorityCarAgent(carAgent);
                try {
                    launcher.getMainContainer().acceptNewAgent(car.getName(), carAgent).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
