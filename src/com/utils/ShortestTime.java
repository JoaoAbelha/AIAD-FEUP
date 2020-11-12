package com.utils;

import com.Data.Graph;
import com.Data.RoadInfo;

import java.util.*;

public class ShortestTime implements RouteStrategy {
    
    private static class Pair {
        double first; // time
        int second; // node

        Pair(double first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    PriorityQueue<Pair> heap = new PriorityQueue<>(Comparator.comparingDouble(a -> a.first));

    private final HashMap<Integer, Double> distances = new HashMap<>(); // todo: times
    private final HashMap<Integer, Integer> path = new HashMap<>();

    @Override
    public ArrayList<Integer> buildRoute(final int src, final int dest, Graph city) {
        //System.out.println("building route...");
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : city.getEdges().entrySet()) {
            distances.put(entry.getKey(), Double.MAX_VALUE);
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                distances.put(adj.getKey(), Double.MAX_VALUE);
            }
        }


       heap.add(new Pair(0, src));
        distances.put(src, 0.0);
        while(heap.size() > 0) {
            Pair Atop = heap.remove();

            Double currentDistance = Atop.first;
            int currentNode = Atop.second;
            //System.out.println();
            //System.out.print("exploring " +  Atop[NODE] + "-> ");

            Map<Integer, RoadInfo> adjacently = city.getAdjacent(currentNode);

            if (adjacently == null) continue;

            if (distances.get(currentNode) < currentDistance) continue;

            //distances.putIfAbsent(Atop[NODE], currentDistance);
            for(Map.Entry<Integer, RoadInfo> adj : adjacently.entrySet()) {
                Integer nodeDest = adj.getKey();
                double distance = adj.getValue().getDistance() * 1.0 / adj.getValue().getMaxVelocity();
                System.out.print(nodeDest + " ");

                if (distances.get(nodeDest) > distances.get(currentNode) + distance) {
                    distances.put(nodeDest , distances.get(currentNode) + distance);
                    heap.add(new Pair(distances.get(nodeDest), nodeDest));
                    path.put(nodeDest, currentNode);
                }

            }
        }

        ArrayList<Integer> solution = new ArrayList<>();

        int current = dest;

        if (path.get(current) == null) {
            //System.out.println("No available path");
            return solution;
        }
        solution.add(dest);

        while(path.get(current) != src) {
            solution.add(path.get(current));
            current = path.get(current);
        }

        solution.add(src);
        Collections.reverse(solution);
        /*System.out.println("shortest time: ");
        for (Integer integer : solution) {
            System.out.print(integer + " ");
        }
        System.out.println();*/
        return solution;
    }

}
