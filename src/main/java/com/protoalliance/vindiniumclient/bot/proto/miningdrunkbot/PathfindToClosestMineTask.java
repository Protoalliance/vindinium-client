package com.protoalliance.vindiniumclient.bot.proto.miningdrunkbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.AStar;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot.MoveToTargetTask;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by Joseph on 3/30/2015.
 */
public class PathfindToClosestMineTask extends LeafTask{
    private static final Logger logger = LogManager.getLogger(MoveToTargetTask.class);

    public PathfindToClosestMineTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        if(bb.getGameState() == null || bb.getTarget() == null){
            return false;
        }
        return true;
    }

    @Override
    public void start() {

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


        AStar a = new AStar(bb.getGameState(), myVert, bb.getTarget());
        Path p = a.getPath();
        if(p == null){
            control.finishWithFailure();
            return;
        }
        bb.setPath(p);

        control.finishWithSuccess();
        return;
    }
}
