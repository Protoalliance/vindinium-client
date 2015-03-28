package com.brianstempin.vindiniumclient.bot.advanced;

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

    public LinkedList<Vertex> getVertices() {
        return vertices;
    }
}
