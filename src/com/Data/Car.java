package com.Data;

public class Car {

    private int currentVelocity;
    private String name;
    private int currentNode;
    private int destNode;
    private String strategy;

    public Car(String name, int src, int dest, String strategy) {
        this.name = name;
        this.currentVelocity = 0;
        this.currentNode = src;
        this.destNode= dest;
        this.strategy = strategy;
    }

    public void print(){
        System.out.println("[" + name + " " +  currentNode + " " + strategy + "]");
    }
}
