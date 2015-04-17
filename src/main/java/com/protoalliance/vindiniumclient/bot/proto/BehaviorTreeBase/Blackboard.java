package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;

import java.util.LinkedList;

/**
 * Created by Joseph on 3/27/2015.
 */
public class Blackboard {
    public BotMove move;
    public Path path;
    public Vertex astarTarget;
    public ProtoGameState state;
    public GameState.Hero targetHero;
    public LinkedList<Pub> checkedPubList;

    public Blackboard(){
        //null constructor since we probably won't want anything here yet.
        checkedPubList = new LinkedList<Pub>();
    }

    public void setTargetHero(GameState.Hero hero){
        this.targetHero = hero;
    }

    public GameState.Hero getTargetHero(){
        return targetHero;
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

    public void setPath(Path p ){
        this.path = p;
    }
    public Path getPath(){
        return path;
    }

    public void setCheckedPubList(LinkedList<Pub> checkedPubList){
        this.checkedPubList = checkedPubList;
    }

    public LinkedList<Pub>  getCheckedPubList(){
        return checkedPubList;
    }

}


