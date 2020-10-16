package com.utils;

import com.Data.Graph;
import jade.core.Agent;

import java.io.FileNotFoundException;

public class GraphReader extends FileReader {

    final int SRC_NODE = 0;
    final int DEST_NODE = 1;
    final int DIST = 2;
    final int MAX_VELOCITY = 3;
    final private Graph city = new Graph();

    public GraphReader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public Agent createAgent() {
        city.print(); // just for debug sake
        return null;
    }

    @Override
    public void readLine(String [] line) {
        city.addEdges(Integer.parseInt(line[SRC_NODE]),
                Integer.parseInt(line[DEST_NODE]),
                Integer.parseInt(line[DIST]),
                Integer.parseInt(line[MAX_VELOCITY]));
    }
}
