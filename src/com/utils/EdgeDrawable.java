package com.utils;

import java.awt.Color;
import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.network.DefaultEdge;
import uchicago.src.sim.network.Node;

public class EdgeDrawable extends DefaultEdge implements DrawableEdge {
    private Color color = Color.WHITE;
    private static final float DEFAULT_STRENGTH = 1;
    private final Color LOW = Color.green;
    private final Color MEDIUM = Color.orange;
    private final Color HIGH = Color.red;

    public EdgeDrawable() { }

    public EdgeDrawable(Node from, Node to) {
        super(from, to, "", DEFAULT_STRENGTH);
    }

    public EdgeDrawable(Node from, Node to, float strength) {
        super(from, to, "", strength);
    }

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public void draw(SimGraphics g, int fromX, int toX, int fromY, int toY) {
        g.drawDirectedLink(color, fromX, toX, fromY, toY);
    }

    public void changeColor (double capacity_percentage) {
        if (capacity_percentage <= 0.2) {
            this.setColor(LOW);
        } else if (capacity_percentage <= 0.70) {
            this.setColor(MEDIUM);
        } else {
            this.setColor(HIGH);
        }
    }
}
