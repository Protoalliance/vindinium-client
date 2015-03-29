package com.protoalliance.vindiniumclient.bot.advanced.astar;

import java.awt.*;

/**
 * Created by Matthew on 3/29/2015.
 */
public class Manhattan implements Heuristic {
    private static int MIN_COST_ONE_SPACE = 1;
    private Point gRowCol;
    private TileGraph hGraph;

    public Manhattan(int goal, TileGraph hGraph){
        this.hGraph = hGraph;
        this.gRowCol = hGraph.getRowCol(goal);
    }

    public double estimate(int start){
        Point sRowCol = hGraph.getRowCol(start);
        double xDist = Math.abs(gRowCol.x - sRowCol.x);
        double yDist = Math.abs(gRowCol.y - sRowCol.y);
        return ((xDist + yDist) * MIN_COST_ONE_SPACE);
    }
}
