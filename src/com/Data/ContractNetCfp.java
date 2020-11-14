package com.Data;

import java.io.Serializable;

public class ContractNetCfp implements Serializable {
    private Car.Strategy strategy;
    private double length;

    public ContractNetCfp(Car.Strategy strategy, double length) {
        this.strategy = strategy;
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public Car.Strategy getStrategy() {
        return strategy;
    }
}
