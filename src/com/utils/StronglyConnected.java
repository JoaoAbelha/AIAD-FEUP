package com.utils;

import com.Data.RoadInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StronglyConnected {

    HashSet<Integer> nodes = new HashSet<>();
    HashSet<Integer> visitedNodes = new HashSet<>();
    Map<Integer, Map<Integer, RoadInfo>> graph;


    public StronglyConnected(Map<Integer, Map<Integer, RoadInfo>> graph) {
        this.graph = graph;

        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : graph.entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for (Map.Entry<Integer, RoadInfo> adj : value.entrySet()) {
                Integer dest = adj.getKey();
                nodes.add(src);
                nodes.add(dest);
            }
        }
    }

    public boolean validateNodeNr() {
        Integer min = Collections.min(nodes);
        if (min <= 0) {
            System.out.println("There is a node with a invalid id "+ min  + ". It should be greater than zero");
            return false;
        }

        for(int i = 1; i <= nodes.size(); i++) {
            if (!nodes.contains(i)) {
                System.out.println("The nodes' identifier should have a range between 1 and <number of nodes> but " + i + " is missing");
                return false;
            }
        }
        return true;
    }

    private Map<Integer, Map<Integer, RoadInfo>> getTranspose() {
        HashMap<Integer, Map<Integer, RoadInfo>> transposed = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : graph.entrySet()) {
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for (Map.Entry<Integer, RoadInfo> adj : value.entrySet()) {
                Integer dest = adj.getKey();
                if (transposed.get(dest) == null) {
                    HashMap<Integer, RoadInfo> helper = new HashMap<>();
                    helper.put(src, adj.getValue());
                    transposed.put(dest, helper);
                } else {
                    transposed.get(dest).put(src, adj.getValue());
                }
            }
        }
        return transposed;
    }

    private void dfs(int v) {
        visitedNodes.add(v);
        int n;
        boolean explore = graph.get(v) != null;
        if(!explore) return;
        for (Integer integer : graph.get(v).keySet()) {
            n = integer;
            if (!visitedNodes.contains(n)) {
                dfs(n);
            }
        }
    }

    public boolean check() {
        visitedNodes.clear();
        dfs(1);
        if (visitedNodes.size() != nodes.size()) {
            System.out.println("Invalid graph");
            return false;
        }


        this.graph =  getTranspose();

        visitedNodes.clear();


        dfs(1);
        if (visitedNodes.size() != nodes.size()) {
            System.out.println("Invalid graph");
            return false;
        }

        return true;

    }
}
