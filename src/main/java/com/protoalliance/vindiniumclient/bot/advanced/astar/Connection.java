package com.protoalliance.vindiniumclient.bot.advanced.astar;

/**
 * Created by Matthew on 3/29/2015.
 */
public class Connection {
    private int sourceNode;
    private int sinkNode;
    private double weight;

    public Connection(int sourceNode, int sinkNode, double weight){
        this.sourceNode = sourceNode;
        this.sinkNode = sinkNode;
        this.weight = weight;
    }

    public int getSourceNode(){
        return this.sourceNode;
    }

    public int getSinkNode(){
        return this.sinkNode;
    }

    public double getWeight(){
        return this.weight;
    }



}
