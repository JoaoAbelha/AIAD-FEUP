package com.Data;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<Integer, Map<Integer, RoadInfo>> edges = new HashMap<>();

    void addEdges(int src, int dest, int distance, int maxVelocity) {
            RoadInfo road = new RoadInfo(distance, maxVelocity);
            if (edges.get(src) == null) {
                HashMap<Integer, RoadInfo> adjacents = new HashMap<>();
                adjacents.put(dest, road);
                edges.put(src, adjacents);
            }
            else {
                edges.get(src).put(dest, road);
            }
    }

    boolean edgeExists(int src, int dest) {
        return this.edges.get(src).get(dest) != null;
    }

    RoadInfo getEdgeInfo(int src, int dest) {
        return this.edges.get(src).get(dest);
    }

    /**
     *
     * @param src
     * @return null or adjacent edges
     */
    Map<Integer, RoadInfo> getAdjacents(int src) {
        return this.edges.get(src);
    }



}
