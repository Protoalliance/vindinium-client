package com.protoalliance.vindiniumclient.bot.proto.bloodandgoldthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.AStar;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by Matthew on 3/29/2015.
 */
public class PathfindToTargetHeroTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(PathfindToTargetHeroTask.class);

    public PathfindToTargetHeroTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        if(bb.getGameState() == null || bb.getTargetHero() == null){
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        //logger.info("Calculating Path");
    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {
        GameState.Position myPos = bb.getGameState().getMe().getPos();
        //Need to actually get vertex in graph rather than
        //make up a vertex.
        Map graph = bb.getGameState().getBoardGraph();
        Vertex myVert = (Vertex) graph.get(myPos);
        Vertex targetHeroPosition = new Vertex(bb.getTargetHero().getPos(), null);

        AStar a = new AStar(bb.getGameState(), myVert, targetHeroPosition);
        Path p = a.getPath();
        if(p == null){
            //logger.info("There's no path!");
            control.finishWithFailure();
            return;
        }
        bb.setPath(p);
        //logger.info("Path found is " + p);
        control.finishWithSuccess();
        return;
    }
}
