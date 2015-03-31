package com.protoalliance.vindiniumclient.bot.proto.minebot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Mine;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Manhattan;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by Joseph on 3/30/2015.
 */
public class GetClosestMineTask extends LeafTask{
    private static final Logger logger = LogManager.getLogger(GetClosestMineTask.class);
    private ProtoGameState state;
    private Vertex target;
    public GetClosestMineTask(Blackboard bb) {
        super(bb);
        this.state = bb.getGameState();
    }

    @Override
    public boolean checkConditions(){
        if(bb.getGameState() == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void start() {
        logger.info("Getting target.");
    }

    @Override
    public void end() {
        logger.info("Target at " + target);
    }
    /**
     * Perform assumes that it will always
     * succeed currently.  The only way it would
     * fail is if there were no mines on the
     * board.  That would be a surprise!
     * Additionally the distances it uses
     * are informed by the Manhattan heuristic,
     * the idea is that most of the time, we're
     * going to find the closest mine.
     *
     * Side Effects:  The target on the blackboard is set.
     */
    @Override
    public void perform() {
        Vertex target = null;
        int minDist = Integer.MAX_VALUE;
        Vertex cur = null;
        if (state == null) {
            logger.error("State is null");
        }
        GameState.Position myPos =state.getMe().getPos();
        Vertex myVert = new Vertex(myPos, null);
        Manhattan man = new Manhattan(null);
        Map<GameState.Position, Mine> minePos = state.getMines();
        for(GameState.Position pos : minePos.keySet()){
            cur = new Vertex(pos, null);
            man.setGoalVertex(cur);
            int est = man.estimate(myVert);
            if(est < minDist){
                target = cur;
            }
        }
        bb.setTarget(target);
        control.finishWithSuccess();
        return;
    }

}
