package com.Agent;
import jade.core.Agent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelloAgent extends Agent {

    final int SRC_NODE = 0;
    final int DEST_NODE = 1;
    final int DIST = 2;
    final int MAX_VELOCITY = 3;


    @Override
    protected void setup() {
        System.out.println("First agent");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/city.txt"));
            String line = reader.readLine();
            String [] elements = line.split(" ");


            while(line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
