package com.protoalliance.vindiniumclient.bot.proto;

import com.protoalliance.vindiniumclient.dto.GameState;

import java.util.LinkedList;

/**
 * Represents a pub (tavern) on the map
 */
public class Pub {
    private final GameState.Position position;
    private LinkedList<Vertex> adjacentVertices;

    public Pub(GameState.Position position) {
        this.position = position;
    }

    public GameState.Position getPosition() {
        return position;
    }
    
    public void setAdjacentVertices(LinkedList<Vertex> adjacentVertices){
        this.adjacentVertices = adjacentVertices;
    }


    public LinkedList<Vertex> getAdjacentVertices(){
        return adjacentVertices;
    }
}
