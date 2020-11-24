package com.Data;
import com.utils.RouteStrategy;

import java.util.ArrayList;
import java.util.Map;

public class Car {
    public enum Status {ROAD, INTERSECTION};
    public enum Strategy {SHORTEST_TIME, SHORTEST_PATH, MINIMUM_INTERSECTIONS}

    private double currentVelocity;
    private final String name;
    private int currentNode;
    private final int destNode;
    private final Strategy strategy;
    private ArrayList<Integer> carPath;
    private double currentDistanceTravelled;
    private RoadInfo currentRoad;
    private Status carStatus;
    private double length;

    public Car(String name, int src, int dest, float length, Strategy strategy) {
        this.name = name;
        this.currentVelocity = 0;
        this.currentNode = src;
        this.destNode= dest;
        this.strategy = strategy;
        this.carPath = new ArrayList<>();
        this.carPath.add(src);
        this.currentDistanceTravelled = 0;
        this.length = length;
        this.carStatus = Status.INTERSECTION;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public Status getCarStatus() {
        return carStatus;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setCarStatus(Status carStatus) {
        this.carStatus = carStatus;
    }

    public int getCurrentNode() {
        return currentNode;
    }

    public int getDestNode() {
        return destNode;
    }

    public RoadInfo getCurrentRoad() {
        return currentRoad;
    }

    public double getCurrentDistanceTravelled() {
        return currentDistanceTravelled;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }

    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }

    public void setCurrentVelocity(double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void print(){
    }

    public int getNextNode() {
        if (this.carPath.size() <= 1) return -1;
        return this.carPath.get(1);
    }

    public void updateCurrentNode() {
        this.carPath.remove(0);
        this.currentNode = this.carPath.get(0);
        this.currentRoad = null;
    }

    public void updateCarPath(RoadInfo roadInfo) {
        this.currentRoad = roadInfo;
        this.currentVelocity = currentRoad.getMaxVelocity();
        this.currentDistanceTravelled = 0;
        this.carPath.add(roadInfo.getEndNode());
    }

    public void addDistanceTravelled(double distanceTravelled) {
        this.currentDistanceTravelled += distanceTravelled;
    }
}
