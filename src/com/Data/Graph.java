package com.Data;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<Integer, Map<Integer, RoadInfo>> edges = new HashMap<>();

    public void addEdges(int src, int dest, int distance, double maxVelocity) {
            RoadInfo road = new RoadInfo(distance, maxVelocity, src, dest);
            if (edges.get(src) == null) {
                HashMap<Integer, RoadInfo> adjacents = new HashMap<>();
                adjacents.put(dest, road);
                edges.put(src, adjacents);
            }
            else {
                edges.get(src).put(dest, road);
            }
    }

    public boolean edgeExists(int src, int dest) {
        return this.edges.get(src).get(dest) != null;
    }

    public RoadInfo getEdgeInfo(int src, int dest) {
        return this.edges.get(src).get(dest);
    }

    /**
     *
     * @param src
     * @return null or adjacent edges
     */
    public Map<Integer, RoadInfo> getAdjacent(int src) {
        return this.edges.get(src);
    }

    public Map<Integer, Map<Integer, RoadInfo>> getEdges() {
        return edges;
    }

    public void print() {
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : edges.entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();

            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest = adj.getKey();
            }
        }
    }
}
