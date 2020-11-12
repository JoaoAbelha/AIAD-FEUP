package com.Data;

import com.utils.RouteStrategy;

import java.util.ArrayList;

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

    public PriorityCar(String name, int src, int dest) {
        this.name = name;
        this.currentNode = src;
        this.destNode= dest;
        this.carPath = new ArrayList<>();
    }

}
