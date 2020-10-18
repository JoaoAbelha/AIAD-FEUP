package com.utils;
import com.Data.Graph;
import com.Data.RoadInfo;

import java.util.*;

public class ShortestPath implements RouteStrategy {

    PriorityQueue<Integer[]> heap = new PriorityQueue<>((a, b) -> {
        return Integer.compare(a[0], b[0]);
    });
    private final HashMap<Integer, Integer> distances = new HashMap<>();
    private HashMap<Integer, Integer> path = new HashMap<>();

    @Override
    public void buildRoute(final int src, final int dest, Graph city) {
        System.out.println("building route...");
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : city.getEdges().entrySet()) {
            distances.put(entry.getKey(), Integer.MAX_VALUE);
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                distances.put(adj.getKey(), Integer.MAX_VALUE);
            }
        }

        System.out.println("distances:");
        for(Map.Entry<Integer, Integer> e : distances.entrySet()) {
            System.out.print(e.getKey() + " ");
        }

        heap.add(new Integer[]{0, src});
        distances.put(src, 0);
        while(heap.size() > 0) {
            Integer [] Atop = heap.remove();
            int DISTANCE = 0, NODE = 1;

            int currentDistance = Atop[DISTANCE];
            System.out.println();
            System.out.print("exploring " +  Atop[NODE] + "-> ");

            Map<Integer, RoadInfo> adjacently = city.getAdjacent(Atop[NODE]);

            if (adjacently == null) continue;

            if (distances.get(Atop[NODE]) < currentDistance) continue;

            //distances.putIfAbsent(Atop[NODE], currentDistance);
            for(Map.Entry<Integer, RoadInfo> adj : adjacently.entrySet()) {
                Integer nodeDest = adj.getKey();
                int distance = adj.getValue().getDistance();
                System.out.print(nodeDest + " ");

                if (distances.get(nodeDest) > distances.get(Atop[NODE]) + distance) {
                    distances.put(nodeDest , distances.get(Atop[NODE]) + distance);
                    heap.add(new Integer[]{distances.get(nodeDest), nodeDest});
                    path.put(nodeDest,Atop[NODE]);
                }

            }
        }

        ArrayList<Integer> solution = new ArrayList<>();

        int current = dest;

        if (path.get(current) == null) {
            System.out.println("No available path");
            return;
        }
        solution.add(dest);
        System.out.println("\npath");

        while(path.get(current) != src) {
            solution.add(path.get(current));
            current = path.get(current);
        }

        solution.add(src);
        Collections.reverse(solution);

        for(Integer x : solution) {
            System.out.print( x + " ");
        }



    }
}
