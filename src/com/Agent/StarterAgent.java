package com.Agent;
import com.Data.Graph;
import com.utils.CarReader;
import com.utils.GraphReader;
import jade.core.Agent;
import java.io.FileNotFoundException;

public class StarterAgent extends Agent {

    final private Graph city = new Graph();

    @Override
    protected void setup() {
        System.out.println("First agent");

        try {
            //GraphReader reader = new GraphReader("src/city.txt");
            //reader.readFile();
            //reader.createAgent();
            CarReader reader = new CarReader("src/car.txt");
            reader.readFile();
            reader.createAgent();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            //e.printStackTrace();
        }

    }

}
