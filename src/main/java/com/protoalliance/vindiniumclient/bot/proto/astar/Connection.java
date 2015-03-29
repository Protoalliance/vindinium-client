package com.protoalliance.vindiniumclient.bot.proto.astar;

import com.protoalliance.vindiniumclient.bot.proto.Vertex;

/**
 * Created by Matthew on 3/29/2015.
 */
public class Connection {
    private Vertex sourceNode;
    private Vertex sinkNode;
    private int weight;

    public Connection(Vertex sourceNode, Vertex sinkNode){
        this.sourceNode = sourceNode;
        this.sinkNode = sinkNode;
        this.weight = 1;
    }

    public Vertex getSourceNode(){
        return this.sourceNode;
    }

    public Vertex getSinkNode(){
        return this.sinkNode;
    }




}
