package com.utils;

import com.CityManager.Launcher;
import com.Data.Car;
import com.Data.PriorityCar;

import java.util.ArrayList;
import java.util.Collections;

public class CarFactory {
    private static int carId = 0;
    private static int priorityCarId = 0;


    public static Car buildCar(Car.Strategy strategy) {
        ArrayList<Integer> shuffled = Launcher.nodes;
        Collections.shuffle(shuffled);
        return new Car("car" + (carId++),
                shuffled.get(0),
                shuffled.get(1) ,
                (float)(Math.random() * 4) + 1,
                strategy);
    }

    public static PriorityCar buildPriorityCar() {
        ArrayList<Integer> shuffled = Launcher.nodes;
        Collections.shuffle(shuffled);
        return new PriorityCar("priority_car" + (priorityCarId++),
                shuffled.get(0),
                shuffled.get(1));
    }
}
