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

    public int getNextNode() {
        if (this.carPath.size() <= 1) return -1;
        return this.carPath.get(1);
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

    public void updateCurrentNode() {
        this.carPath.remove(0);
        this.currentNode = this.carPath.get(0);
        this.currentRoad = null;
    }

    public void updateCarPath(Graph city, RoadInfo roadInfo) {
        if(carPath.size() == 1)
            return;

        int nextNode = carPath.get(1);
        this.currentRoad = roadInfo;
        this.currentVelocity = currentRoad.getMaxVelocity();
        this.currentDistanceTravelled = 0;

        if(nextNode != roadInfo.getEndNode()) {
            this.carPath = strategy.buildRoute(roadInfo.getEndNode(), this.destNode, city);
            this.carPath.add(0, this.currentNode);
        }

        System.out.println(this.name + " driving on road " + this.currentNode + "->" + nextNode);
    }

    public void addDistanceTravelled(double distanceTravelled) {
        this.currentDistanceTravelled += distanceTravelled;
    }
}
