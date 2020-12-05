package com.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Configuration {

    private boolean batchMode = true;
    private int priorityCars = 1;
    private int numberShortestTimeCar =  1;
    private int numberShortestPathCar =  1;
    private int numberMinIntersectionCar = 2;

    public Configuration(String[] args) {
        if (args.length == 0) {
            System.out.println("Using default parameters");
        } else {
            // ler do file em configurations
            String filenameConfiguration = args[0];
            try {
                File myObj = new File(filenameConfiguration);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String [] data= myReader.nextLine().split(":");
                    Arrays.parallelSetAll(data, (i) -> data[i].trim());
                    switch (data[0].toLowerCase()) {
                        case "batchmode" -> {
                            this.batchMode = Boolean.parseBoolean(data[1]);
                        }
                        case "numbershortesttimecar" -> {
                            this.numberShortestTimeCar = Integer.parseInt(data[1]);
                        }
                        case "numberprioritycar" -> {
                            this.priorityCars = Integer.parseInt(data[1]);
                        }
                        case "numbershortestpathcar" -> {
                            this.numberShortestPathCar = Integer.parseInt(data[1]);
                        }
                        case "numberminintersectioncar" -> {
                            this.numberMinIntersectionCar = Integer.parseInt(data[1]);
                        }
                        default -> {
                            System.out.println("Unknown key in configuration file " + data[0]);
                        }

                    }

                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred while trying to read the configuration file");
                e.printStackTrace();
            }
        }
    }

    public int getNumberShortestTimeCar() {
        return numberShortestTimeCar;
    }

    public void setNumberShortestTimeCar(int nr) {
        this.numberShortestTimeCar = nr;
    }

    public int getNumberShortestPathCar() {
        return numberShortestPathCar;
    }

    public void setNumberShortestPathCar(int numberShortestPathCar) {
        this.numberShortestPathCar = numberShortestPathCar;
    }

    public int getNumberMinIntersectionCar() {
        return numberMinIntersectionCar;
    }

    public void setNumberMinIntersectionCar(int numberMinIntersectionCar) {
        this.numberMinIntersectionCar = numberMinIntersectionCar;
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
