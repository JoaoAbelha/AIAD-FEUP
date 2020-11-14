package com.Data;


import com.utils.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PathResponse implements Serializable {
    private HashMap<String, ArrayList<Pair<Double, Integer>>> paths;

    public PathResponse() {
        this.paths = new HashMap<>();
    }

    public void addPath(String road, ArrayList<Pair<Double, Integer>> path) {
        paths.put(road, path);
    }

    public HashMap<String, ArrayList<Pair<Double, Integer>>> getPaths() {
        return paths;
    }
}
