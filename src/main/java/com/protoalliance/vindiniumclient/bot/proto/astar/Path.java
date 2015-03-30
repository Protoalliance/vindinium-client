package com.protoalliance.vindiniumclient.bot.proto.astar;

import com.protoalliance.vindiniumclient.bot.proto.Vertex;

import java.util.LinkedList;

/**
 * Created by Joseph on 3/27/2015.
 */
public class Path {
    private LinkedList<Vertex> vertices;

    public Path() {
        vertices = new LinkedList<>();
    }

    public void addToFront(Vertex v) {
        vertices.addFirst(v);
    }

    public void add(Vertex v){
        vertices.add(v);
    }

    public LinkedList<Vertex> getVertices() {
        return vertices;
    }
}
