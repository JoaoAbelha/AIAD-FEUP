package com.Agent;


import com.Behaviour.CityRequestResponder;
import com.Behaviour.CitySubscriptionResponder;
import com.Behaviour.CityWeatherChange;
import com.Data.*;
import com.Manager.WeatherSubscriptionManager;
import com.utils.MinInterseptions;
import com.utils.Pair;
import com.utils.ShortestPath;
import com.utils.ShortestTime;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CityAgent extends AgentRegister {
    WeatherSubscriptionManager manager;
    WeatherStation weatherStation;
    Graph city;
    HashMap<String, Double> maxVelocity = new HashMap<>(); // p.e road23 ->50
    HashMap<String, Integer> distances = new HashMap<>() ; // p.e road32 -> 100
    HashMap<Integer, ArrayList<Integer>> adjacentRoads = new HashMap<>();

    public CityAgent(WeatherStation weatherStation, Graph city) {
        this.manager = new WeatherSubscriptionManager();
        this.weatherStation = weatherStation;
        this.city = city;
        for (Map.Entry<Integer, Map<Integer, RoadInfo>> entry : city.getEdges().entrySet()) {
            ArrayList<Integer> adjacent = new ArrayList<>();
            Integer src = entry.getKey();
            Map<Integer, RoadInfo> value = entry.getValue();
            for(Map.Entry<Integer,RoadInfo> adj : value.entrySet()) {
                Integer dest= adj.getKey();
                RoadInfo roadInfo = adj.getValue();
                System.out.println(src + " " + dest);
                this.maxVelocity.put("road" + src + dest, roadInfo.getMaxVelocity());
                this.distances.put("road" + src + dest, roadInfo.getDistance());
                adjacent.add(dest);
            }
            adjacentRoads.put(src, adjacent);
        }
    }

    public Set<String> getRoads() {
        return maxVelocity.keySet();
    }

    public WeatherStation getWeatherStation() {
        return weatherStation;
    }

    public WeatherSubscriptionManager getManager() {
        return manager;
    }

    public HashMap<String, Double> getMaxVelocity() {
        return maxVelocity;
    }

    @Override
    protected void setup() {
        register("city");
        System.out.println("city agent");

        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );

        addBehaviour(new CityRequestResponder(this, template));
        addBehaviour(new CitySubscriptionResponder(this, this.manager));
        addBehaviour(new CityWeatherChange(this, 300));
    }

    public void updateRoadMaxVelocity(String name, double maxVelocity) {
        this.maxVelocity.put(name, maxVelocity);
    }

    public PathResponse calculatePath(PathRequest request) {
        switch (request.getStrategy()) {
            case SHORTEST_PATH:
                return this.calculateShortestPath(request);
            case SHORTEST_TIME:
                return this.calculateShortestTime(request);
            case MINIMUM_INTERSECTIONS:
                return this.calculateMinIntersection(request);
            default:
                return null;
        }
    }

    private PathResponse calculateShortestPath(PathRequest request) {
        PathResponse response = new PathResponse();
        ArrayList<Integer> adjacent = adjacentRoads.get(request.getStartNode());
        for(Integer node : adjacent) {
            ArrayList<Integer> path = new ShortestPath().buildRoute(node, request.getEndNode(), this.city);
            String roadName = "road" + request.getStartNode() + node;
            response.addPath(roadName, getRoadInfo(path));
        }
        return response;
    }

    private PathResponse calculateShortestTime(PathRequest request) {
        PathResponse response = new PathResponse();
        ArrayList<Integer> adjacent = adjacentRoads.get(request.getStartNode());
        for(Integer node : adjacent) {
            ArrayList<Integer> path = new ShortestTime().buildRoute(node, request.getEndNode(), this.city);
            String roadName = "road" + request.getStartNode() + node;
            response.addPath(roadName, getRoadInfo(path));
        }
        return response;
    }

    private PathResponse calculateMinIntersection(PathRequest request) {
        PathResponse response = new PathResponse();
        ArrayList<Integer> adjacent = adjacentRoads.get(request.getStartNode());
        for(Integer node : adjacent) {
            ArrayList<Integer> path = new MinInterseptions().buildRoute(node, request.getEndNode(), this.city);
            String roadName = "road" + request.getStartNode() + node;
            response.addPath(roadName, getRoadInfo(path));
        }
        return response;
    }

    private ArrayList<Pair<Double, Integer>> getRoadInfo(ArrayList<Integer> path) {
        ArrayList<Pair<Double, Integer>> roadsInfos = new ArrayList<>();

        for(int i = 0; i < path.size() - 1; i++) {
            String name = "road" + path.get(i) + path.get(i + 1);
            double maxVelocity = this.maxVelocity.get(name);
            int distance = this.distances.get(name);
            roadsInfos.add(new Pair<>(maxVelocity, distance));
        }

        return roadsInfos;
    }

    public boolean existsNode(Integer startNode) {
        int size = this.adjacentRoads.get(startNode).size();
        return size > 0;
    }
}
