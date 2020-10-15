package com.utils;

import jade.core.Agent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

public abstract class FileReader implements IFileReader {

    BufferedReader reader;

    FileReader(String filename) throws FileNotFoundException {
        this.reader = new BufferedReader(new java.io.FileReader(filename));
    }

    @Override
    public void readFile() {
        String line;
        try {
            line = reader.readLine();
            while(line != null) {
                String[] elements = line.split(" ");
                    line = reader.readLine();
                    readLine(elements);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
