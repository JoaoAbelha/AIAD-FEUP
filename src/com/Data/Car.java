package com.Data;
import com.utils.RouteStrategy;

import java.util.ArrayList;
import java.util.Map;

public class Car {

    private int currentVelocity;
    private final String name;
    private int currentNode;
    private final int destNode;
    private final RouteStrategy strategy;
    private ArrayList<Integer> carPath;
    private double currentDistanceTravelled;
    private RoadInfo currentRoad;

    public Car(String name, int src, int dest, float length, RouteStrategy strategy) {
        this.name = name;
        this.currentVelocity = 0;
        this.currentNode = src;
        this.destNode= dest;
        this.strategy = strategy;
        this.carPath = new ArrayList<>();
        this.currentDistanceTravelled = 0;
    }

    public String getName() {
        return name;
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

    public int getCurrentVelocity() {
        return currentVelocity;
    }

    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }

    public void setCurrentVelocity(int currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void print(){
        System.out.println("[" + name + " " +  currentNode + " " + strategy+ "]");
    }

    public void calculateCarPath(Graph city) {
        this.carPath = strategy.buildRoute(this.currentNode, this.destNode, city);

        if(carPath.size() == 0)
            return;

        Map<Integer, RoadInfo> roads = city.getEdges().get(this.currentNode);
        int nextNode = carPath.get(1);
        this.currentRoad = roads.get(nextNode);
        this.currentVelocity = currentRoad.getMaxVelocity();
    }

    public void updateCarPath(Graph city) {
        this.carPath.remove(0);
        this.currentNode = this.carPath.get(0);

        if(carPath.size() == 1)
            return;

        Map<Integer, RoadInfo> roads = city.getEdges().get(this.currentNode);
        int nextNode = carPath.get(1);
        this.currentRoad = roads.get(nextNode);
        this.currentVelocity = currentRoad.getMaxVelocity();
        System.out.println(this.name + " driving on road " + this.currentNode + "->" + nextNode);
    }

    public void addDistanceTravelled(double distanceTravelled) {
        this.currentDistanceTravelled += distanceTravelled;
    }
}
