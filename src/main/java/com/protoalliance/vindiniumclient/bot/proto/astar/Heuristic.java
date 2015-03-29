package com.protoalliance.vindiniumclient.bot.proto.astar;

import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.dto.GameState;

/**
 * Created by Matthew on 3/29/2015.
 */
public interface Heuristic {
    public int estimate(Vertex node);
}
