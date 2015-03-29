package com.protoalliance.vindiniumclient.bot.advanced.astar;

/**
 * Created by Matthew on 3/29/2015.
 */
public interface Heuristic {
    public double estimate(int node);
}
