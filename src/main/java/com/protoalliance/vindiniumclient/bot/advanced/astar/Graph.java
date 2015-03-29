package com.protoalliance.vindiniumclient.bot.advanced.astar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew on 3/29/2015.
 */
public class Graph {
    private int numNodes;
    private final float LOAD_FACTOR = (float) 0.75;
    private double maxWeight;
    private Map<Integer, List<Connection>> nodeList;

    public Graph(float numNodes){
        int initCapacity = (int) (numNodes/LOAD_FACTOR);
        nodeList = new HashMap<Integer, List<Connection>>(initCapacity, LOAD_FACTOR);
    }

    public void addConnection(int sourceNode, int sinkNode, double weight){
        if(!nodeList.containsKey(sourceNode))
            nodeList.put(sourceNode, new LinkedList<Connection>());

        LinkedList<Connection> ll = (LinkedList<Connection>) nodeList.get(sourceNode);
        ll.add(new Connection(sourceNode, sinkNode, weight));
    }

    public LinkedList<Connection> getConnections(int sourceNode){
        if(!nodeList.containsKey(sourceNode))
            return null;
        LinkedList<Connection> ll = (LinkedList<Connection>) nodeList.get(sourceNode);
        return ll;
    }

    public double getMaxWeight(){
        return maxWeight;
    }

    public void setMaxWeight(double mw){
        this.maxWeight = mw;
    }

    public void setNumNodes(int numNodes){
        this.numNodes = numNodes;
    }

    public int getNumNodes(){
        return numNodes;
    }
}

