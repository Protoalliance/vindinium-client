package com.protoalliance.vindiniumclient.bot.advanced.astar;

/**
 * Created by Matthew on 3/29/2015.
 */
public class NodeRecord {
    private int node;
    private Connection connection;
    private double costSoFar;
    private double estimatedCostSoFar;

    public NodeRecord(int node){
        this.node = node;
        this.connection = null;
        this.costSoFar = 0.0;
    }

    public NodeRecord(int node, Connection connection, double costSoFar){
        this.node = node;
        this.connection = connection;
        this.costSoFar = costSoFar;
    }

    public NodeRecord(int node, Connection connection, double costSoFar, double estimatedCostSoFar){
        this.node = node;
        this.connection = connection;
        this.costSoFar = costSoFar;
        this.estimatedCostSoFar = estimatedCostSoFar;
    }

    public int getNode(){
        return this.node;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void setConnection(Connection c){
        this.connection = c;
    }


    public double getCostSoFar(){
        return this.costSoFar;
    }

    public void setCostSoFar(double csf){
        this.costSoFar = csf;
    }

    public double getEstimatedCostSoFar(){
        return this.estimatedCostSoFar;
    }

    public void setEstimatedCostSoFar(double etcf){
        this.estimatedCostSoFar = etcf;
    }
}

