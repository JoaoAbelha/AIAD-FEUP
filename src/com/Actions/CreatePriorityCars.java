package com.Actions;

import com.Agent.PriorityCarAgent;
import com.CityManager.Launcher;
import com.Data.PriorityCar;
import com.utils.CarFactory;
import jade.wrapper.StaleProxyException;
import uchicago.src.sim.engine.BasicAction;

public class CreatePriorityCars extends BasicAction {
    private final Launcher launcher;
    private int numberPriorityCars;

    public CreatePriorityCars(int numberPriorityCars, Launcher launcher) {
        this.numberPriorityCars = numberPriorityCars;
        this.launcher = launcher;
    }

    @Override
    public void execute() {
        for(int i = 0; i< this.numberPriorityCars; i++) {
            PriorityCar car = CarFactory.buildPriorityCar();
            PriorityCarAgent carAgent = new PriorityCarAgent(car, launcher.getGraph());
            launcher.addPriorityCarAgent(carAgent);
            try {
                launcher.getMainContainer().acceptNewAgent(car.getName(), carAgent).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }
}
