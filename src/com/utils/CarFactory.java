package com.utils;

import com.Data.Car;

import java.util.ArrayList;
import java.util.Random;

public class CarFactory {
    private static int id = 0;
    private static int minNode;
    private static int maxNode;
    private static ArrayList<Car.Strategy> possibleStrategy;
    private static final Random rand = new Random();

    public Car build() {
        return new Car("car" + (id++),
                rand.nextInt(maxNode+ 1 - minNode) + minNode ,
                rand.nextInt(maxNode+ 1  - minNode) + minNode ,
                (float)(Math.random() * 4) + 1,
                possibleStrategy.get(rand.nextInt(possibleStrategy.size())));
    }

}
