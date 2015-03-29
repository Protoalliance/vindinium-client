package com.protoalliance.vindiniumclient.bot.proto.astar;

import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.dto.GameState;

import java.awt.*;

/**
 * Created by Matthew on 3/29/2015.
 */
public class Manhattan implements Heuristic {
    private static int MIN_COST_ONE_SPACE = 1;
    private Vertex goalVertex;

    public Manhattan(Vertex goal){
        this.goalVertex = goal;
    }

    public int estimate(Vertex start){
        int xdist = Math.abs(goalVertex.getPosition().getX() - start.getPosition().getX());
        int ydist = Math.abs(goalVertex.getPosition().getY() - start.getPosition().getY());
        return ((xdist + ydist) * MIN_COST_ONE_SPACE);
    }
}
