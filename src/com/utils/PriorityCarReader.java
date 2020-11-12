package com.utils;

import com.Data.PriorityCar;

import java.io.FileNotFoundException;
import java.util.HashSet;

public class PriorityCarReader extends FileReader {

    final int NAME_NODE = 0;
    final int SRC_NODE = 1;
    final int DEST_NODE = 2;

    HashSet<PriorityCar> priorityCars = new HashSet<PriorityCar>();

    public PriorityCarReader(String filename) throws FileNotFoundException {
        super(filename);
    }


    @Override
    public void readLine(String[] line) {
        priorityCars.add(new PriorityCar(line[NAME_NODE],
                Integer.parseInt(line[SRC_NODE]),
                Integer.parseInt(line[DEST_NODE])));
    }

    @Override
    public HashSet<PriorityCar> getInfo() {
        return priorityCars;
    }
}
