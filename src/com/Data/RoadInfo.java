package com.Data;

public class RoadInfo {
    private final int distance;
    private int maxVelocity;

    RoadInfo(int distance, int maxVelocity) {
        this.distance = distance;
        this.maxVelocity = maxVelocity;
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
}
