package com.Data;
import com.utils.RouteStrategy;

public class Car {

    private int currentVelocity;
    private final String name;
    private int currentNode;
    private final int destNode;
    private final RouteStrategy strategy;

    public Car(String name, int src, int dest, RouteStrategy strategy) {
        this.name = name;
        this.currentVelocity = 0;
        this.currentNode = src;
        this.destNode= dest;
        this.strategy = strategy;
    }

    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }

    public void setCurrentVelocity(int currentVelocity) {
        this.currentVelocity = currentVelocity;
    }

    public void print(){
        System.out.println("[" + name + " " +  currentNode + " " + strategy + "]");
    }

    public String getName() {
        return name;
    }
}
