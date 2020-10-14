package com.Agent;
import com.Data.Graph;
import jade.core.Agent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelloAgent extends Agent {

    final int SRC_NODE = 0;
    final int DEST_NODE = 1;
    final int DIST = 2;
    final int MAX_VELOCITY = 3;
    private Graph city = new Graph();


    @Override
    protected void setup() {
        System.out.println("First agent");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/city.txt"));
            String line = reader.readLine();


            while(line != null) {
                String [] elements = line.split(" ");
                city.addEdges(Integer.parseInt(elements[SRC_NODE]),
                        Integer.parseInt(elements[DEST_NODE]),
                        Integer.parseInt(elements[DIST]),
                        Integer.parseInt(elements[MAX_VELOCITY]));
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        city.print();
    }
}
