package com.utils;

import com.Data.Graph;

public interface RouteStrategy {
    void buildRoute(int src, int dest, Graph city);
}
