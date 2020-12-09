package com.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Configuration {
    private double probabilityAddPriorityCar;
    private double probabilityAddCar;
    private double probabilityChangeWeather;
    private boolean batchMode = true;
    private int priorityCars = 1;
    private int numberShortestTimeCar =  1;
    private int numberShortestPathCar =  1;
    private int numberMinIntersectionCar = 2;
    private String fileofCity = null;
    private String fileOfTypeWeather = null;
    private String carToFollow = null;
    private String prioritycarToFollow = null;
    private String priorityCarsFile = null;
    private String cars = null;
    private int tickInterval = 500;

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
                        case "graph" -> {
                            this.fileofCity = data[1];
                        }
                        case "typeweather" -> {
                            this.fileOfTypeWeather = data[1];
                        }
                        case "cars" -> {
                            this.cars = data[1];
                        }
                        case "prioritycars" -> {
                            this.priorityCarsFile = data[1];
                        }
                        case "cartofollow" -> {
                            String [] carInfo = data[1].split(" ");
                            Arrays.parallelSetAll(carInfo, (i) -> carInfo[i].trim());
                            if (carInfo.length != 4) {
                                System.out.println("car to follow configuration invalid. Ignoring it");
                                break;
                            }
                            this.carToFollow = String.join (" ",carInfo);
                        }
                        case "prioritycartofollow" -> {
                            String [] carInfo = data[1].split(" ");
                            Arrays.parallelSetAll(carInfo, (i) -> carInfo[i].trim());
                            if (carInfo.length != 2) {
                                System.out.println("Priority car to follow configuration invalid. Ignoring it");
                                break;
                            }
                            this.prioritycarToFollow = String.join (" ",carInfo);
                        }
                        case "tickinterval" -> {
                            this.tickInterval = Integer.parseInt(data[1]);
                        }
                        case "probabilitychangeweather" -> {
                            this.probabilityChangeWeather = Double.parseDouble(data[1]);
                        }
                        case "probabilityaddcar" -> {
                            this.probabilityAddCar = Double.parseDouble(data[1]);
                        }
                        case "probabilityaddprioritycar" -> {
                            this.probabilityAddPriorityCar = Double.parseDouble(data[1]);
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

    public String getPrioritycarToFollow() {
        return prioritycarToFollow;
    }

    public void setPrioritycarToFollow(String prioritycarToFollow) {
        this.prioritycarToFollow = prioritycarToFollow;
    }

    public String getCarToFollow() {
        return carToFollow;
    }

    public void setCarToFollow(String carToFollow) {
        this.carToFollow = carToFollow;
    }

    public String getFileOfTypeWeather() {
        return this.fileOfTypeWeather;
    }

    public void setFileOfTypeWeather(String fileOfTypeWeather) {
        this.fileOfTypeWeather = fileOfTypeWeather;
    }

    public String getFileofCity() {
        return fileofCity;
    }

    public void setFileofCity(String fileofCity) {
        this.fileofCity = fileofCity;
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

    public int getTickInterval() {
        return tickInterval;
    }

    public void setTickInterval(int tickInterval) {
        this.tickInterval = tickInterval;
    }

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public String getPriorityCars() {
        return priorityCarsFile;
    }

    public void setPriorityCars(String fileOfPriorityCars) {
        this.priorityCarsFile = fileOfPriorityCars;
    }

    public double getProbabilityChangeWeather() {
        return probabilityChangeWeather;
    }

    public void setProbabilityChangeWeather(double probabilityChangeWeather) {
        this.probabilityChangeWeather = probabilityChangeWeather;
    }

    public double getProbabilityAddCar() {
        return probabilityAddCar;
    }

    public void setProbabilityAddCar(double probabilityAddCar) {
        this.probabilityAddCar = probabilityAddCar;
    }

    public double getProbabilityAddPriorityCar() {
        return probabilityAddPriorityCar;
    }

    public void setProbabilityAddPriorityCar(double probabilityAddPriorityCar) {
        this.probabilityAddPriorityCar = probabilityAddPriorityCar;
    }
}
