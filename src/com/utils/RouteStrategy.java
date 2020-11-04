package com.utils;

import com.Data.Graph;

import java.util.ArrayList;

public interface RouteStrategy {
    ArrayList<Integer> buildRoute(int src, int dest, Graph city);
}
