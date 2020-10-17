package com.utils;
import com.Data.Car;
import java.io.FileNotFoundException;
import java.util.HashSet;

public class CarReader extends FileReader {

    final int NAME_NODE = 0;
    final int SRC_NODE = 1;
    final int DEST_NODE = 2;
    final int STRATEGY = 3;

    HashSet<Car> cars = new HashSet<>();

    public CarReader(String filename) throws FileNotFoundException {
        super(filename);
    }

    @Override
    public void readLine(String[] line) {
        cars.add(new Car(line[NAME_NODE],
                Integer.parseInt(line[SRC_NODE]),
                Integer.parseInt(line[DEST_NODE]),
                line[STRATEGY]));
    }

    @Override
    public HashSet<Car> getInfo() {
        for(Car car : this.cars) {
            car.print();
        }
        return cars;
    }
}
