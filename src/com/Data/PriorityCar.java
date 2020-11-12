package com.Data;

import com.utils.RouteStrategy;
import com.utils.ShortestPath;

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
    private int currentVelocity;

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
        this.carPath = new ShortestPath().buildRoute(this.currentNode, this.destNode, city);

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

    public String getName() {
        return name;
    }
}
