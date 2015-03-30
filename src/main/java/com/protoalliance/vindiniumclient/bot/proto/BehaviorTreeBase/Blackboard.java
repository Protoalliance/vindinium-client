package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;

/**
 * Created by Joseph on 3/27/2015.
 */
public class Blackboard {
    public BotMove move;
    public Path path;
    public Vertex astarTarget;
    public ProtoGameState state;

    public Blackboard(){
        //null constructor since we probably won't want anything here yet.
    }

    public void setTarget(Vertex target){
        this.astarTarget = target;
    }

    public Vertex getTarget(){
        return this.astarTarget;
    }

    public void setGameState(ProtoGameState state){
        this.state = state;
    }

    public ProtoGameState getGameState(){
        return state;
    }

    public Path getPath(){
        return path;
    }

}


