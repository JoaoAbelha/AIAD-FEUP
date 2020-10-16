package com.Agent;
import com.utils.CarReader;
import com.utils.TypeWeatherReader;
import com.utils.WeatherReader;
import jade.core.Agent;
import java.io.FileNotFoundException;

public class StarterAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("First agent");

        try {
            //GraphReader reader = new GraphReader("src/city.txt");
            //reader.readFile();
            //reader.createAgent();
            //CarReader reader = new CarReader("src/car.txt");
            //reader.readFile();
            //reader.createAgent();
            //TypeWeatherReader reader = new TypeWeatherReader("src/typeWeather.txt");
            WeatherReader reader = new WeatherReader("src/weather.txt");
            reader.readFile();
            reader.createAgent();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            //e.printStackTrace();
        }

    }

}
