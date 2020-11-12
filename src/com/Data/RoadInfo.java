package com.Data;

import java.io.Serializable;

public class RoadInfo implements Serializable {
    private final int distance;
    private int roadInitialVelocity;
    private int maxVelocity;
    private int startNode;
    private int endNode;

    RoadInfo(int distance, int maxVelocity, int startNode, int endNode) {
        this.distance = distance;
        this.roadInitialVelocity = maxVelocity;
        this.maxVelocity = maxVelocity;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public int getRoadInitialVelocity() {
        return roadInitialVelocity;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }
}
