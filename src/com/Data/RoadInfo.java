package com.Data;

import java.io.Serializable;

public class RoadInfo implements Serializable {
    private final int distance;
    private double roadInitialVelocity;
    private double maxVelocity;
    private int startNode;
    private int endNode;

    RoadInfo(int distance, double maxVelocity, int startNode, int endNode) {
        this.distance = distance;
        this.roadInitialVelocity = maxVelocity;
        this.maxVelocity = maxVelocity;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getDistance() {
        return distance;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getRoadInitialVelocity() {
        return roadInitialVelocity;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }
}
