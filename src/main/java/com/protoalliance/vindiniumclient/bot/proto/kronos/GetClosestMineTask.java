package com.protoalliance.vindiniumclient.bot.proto.kronos;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Mine;
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
    public GetClosestMineTask(Blackboard bb) {
        super(bb);
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
        //logger.info("Setting Mine target.");
    }

    @Override
    public void end() {

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
        Vertex cur;
        if (bb.getGameState() == null) {
            //logger.error("State is null");
        }
        GameState.Position myPos = bb.getGameState().getMe().getPos();
        Vertex myVert = new Vertex(myPos, null);
        Manhattan man = new Manhattan(null);

        Map<GameState.Position, Mine> minePos = bb.getGameState().getMines();
        for(GameState.Position pos : minePos.keySet()){
            //If I already own the mine skip it.
            //needs short circuit execution since it's
            //possible that the owner key is null at the
            //beginning of the game.
            if(minePos.get(pos).getOwner() != null &&
                    minePos.get(pos).getOwner().getName().equals(bb.getGameState().getMe().getName())){
                continue;
            }
            cur = new Vertex(pos, null);

            man.setGoalVertex(cur);
            int est = man.estimate(myVert);
            if(est < minDist){
                target = cur;
                minDist = est;
            }
        }
        if (target == null) {
            control.finishWithFailure();
        }
        bb.setTarget(target);
        control.finishWithSuccess();
        return;
    }

}