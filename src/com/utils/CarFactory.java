package com.utils;

import com.CityManager.Launcher;
import com.Data.Car;
import com.Data.PriorityCar;

import java.util.ArrayList;
import java.util.Collections;

public class CarFactory {
    private static int id = 0;

    public Car buildCar(Car.Strategy strategy) {
        ArrayList<Integer> shuffled = Launcher.nodes;
        Collections.shuffle(shuffled);
        return new Car("car" + (id++),
                shuffled.get(0),
                shuffled.get(1) ,
                (float)(Math.random() * 4) + 1,
                strategy);
    }

    public PriorityCar buildPriorityCar() {
        ArrayList<Integer> shuffled = Launcher.nodes;
        Collections.shuffle(shuffled);
        return new PriorityCar("priority_car" + (id++),
                shuffled.get(0),
                shuffled.get(1));
    }
}
