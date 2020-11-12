package com.utils;

import com.Data.Graph;
import com.Data.RoadInfo;

import java.util.*;

public class MinInterseptions implements  RouteStrategy {


    private static class Pair {
        int first; // current number of intersections
        int second; // node

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    PriorityQueue<Pair> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a.first));
    private final HashMap<Integer, Integer> intersectionCounter = new HashMap<>(); // nr intersections
    private HashMap<Integer, Integer> path = new HashMap<>();

    @Override
    public ArrayList<Integer> buildRoute(final int src, final int dest, Graph city) {
        //System.out.println("building route...");
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : city.getEdges().entrySet()) {
            intersectionCounter.put(entry.getKey(), Integer.MAX_VALUE);
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                intersectionCounter.put(adj.getKey(), Integer.MAX_VALUE);
            }
        }

        //System.out.println("distances:");
       // for(Map.Entry<Integer, Integer> e : distances.entrySet()) {
            //System.out.print(e.getKey() + " ");
        //}

        heap.add(new Pair(0, src));
        intersectionCounter.put(src, 0);
        while(heap.size() > 0) {
            Pair Atop = heap.remove();

            int currentNrIntersections = Atop.first;
            //System.out.println();
            //System.out.print("exploring " +  Atop[NODE] + "-> ");

            Map<Integer, RoadInfo> adjacently = city.getAdjacent(Atop.second);

            if (adjacently == null) continue;

            if (intersectionCounter.get(Atop.second) < currentNrIntersections) continue;

            //distances.putIfAbsent(Atop[NODE], currentDistance);
            for(Map.Entry<Integer, RoadInfo> adj : adjacently.entrySet()) {
                Integer nodeDest = adj.getKey();
                System.out.print(nodeDest + " ");

                if (intersectionCounter.get(nodeDest) > intersectionCounter.get(Atop.second) + 1) {
                    intersectionCounter.put(nodeDest , intersectionCounter.get(Atop.second) + 1);
                    heap.add(new Pair(intersectionCounter.get(nodeDest), nodeDest));
                    path.put(nodeDest, Atop.second);
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
        //System.out.println("\npath");

        while(path.get(current) != src) {
            solution.add(path.get(current));
            current = path.get(current);
        }

        solution.add(src);
        Collections.reverse(solution);
        /*System.out.println("min intersections path: ");
        for (Integer integer : solution) {
            System.out.print(integer + " ");
        }
        System.out.println();*/

        return solution;
    }
}
