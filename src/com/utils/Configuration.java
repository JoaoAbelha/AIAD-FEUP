package com.utils;

import java.io.File;

public class Configuration {

    private boolean batchMode = true;
    private int numberCars = 5;
    private int priorityCars = 1;

    public Configuration(String[] args) {
        if (args.length == 0) {
            System.out.println("Using default parameters");
        } else {
            // ler do file em configurations
        }
    }

    public int getNumberCars() {
        return numberCars;
    }

    public void setNumberCars(int nr) {
        this.numberCars = nr;
    }


    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }
    public boolean getBatchMode() {
        return batchMode;
    }


    public void setNumberPriorityCars(int cars) {
        this.priorityCars = cars;
    }

    public int getPriorityNumberCars() {
        return priorityCars;
    }
}
