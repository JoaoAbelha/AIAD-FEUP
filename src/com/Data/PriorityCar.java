package com.Data;

import com.utils.ShortestPath;
import com.utils.ShortestTime;

import java.util.ArrayList;
import java.util.Map;

/**
 * The velocity will always be bounded by the road
 * It will always take the path that takes less time
 */
public class PriorityCar {

    private final String name;
    private int currentNode;
    private final int destNode;
    private ArrayList<Integer> carPath;
    private RoadInfo currentRoad;
    private double currentDistanceTravelled;
    private double currentVelocity;

    public PriorityCar(String name, int src, int dest) {
        this.name = name;
        this.currentNode = src;
        this.destNode= dest;
        this.carPath = new ArrayList<>();
        this.currentVelocity = 0;
    }

    /**
     *
     * todo: mudar para shortest time depois
     */
    public void calculateCarPath(Graph city) {
        this.carPath = new ShortestTime().buildRoute(this.currentNode, this.destNode, city);

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

    public String getName() {
        return name;
    }

    public void addDistanceTravelled(double distanceTravelled) {
        this.currentDistanceTravelled += distanceTravelled;
    }

    public void setCurrentVelocity(double currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public int getNextNode() {
        if (this.carPath.size() <= 1) return -1;
        return this.carPath.get(1);
    }

    public void updateCarPath(Graph city, RoadInfo roadInfo) {
        if(carPath.size() == 1)
            return;

        this.currentRoad = roadInfo;
        this.currentVelocity = currentRoad.getMaxVelocity();
        this.currentDistanceTravelled = 0;

        System.out.println(this.name + " driving on road " + this.currentNode + "->" + this.carPath.get(1));
    }
}
