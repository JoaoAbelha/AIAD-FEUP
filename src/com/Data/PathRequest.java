package com.Data;

import java.io.Serializable;

public class PathRequest implements Serializable {
    private final Car.Strategy strategy;
    private int startNode;
    private int endNode;

    public PathRequest(int startNode, int endNode, Car.Strategy strategy) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.strategy = strategy;
    }

    public int getEndNode() {
        return endNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public Car.Strategy getStrategy() {
        return strategy;
    }
}
