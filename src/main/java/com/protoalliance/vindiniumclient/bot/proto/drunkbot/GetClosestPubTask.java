package com.protoalliance.vindiniumclient.bot.proto.drunkbot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Manhattan;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


/**
 * This is the simplest version of this behavior.
 * There's no checking to make sure that we actually need
 * health, but to even show what the behavior is, we need
 * to make sure we have gold to actually get into the pub.
 *
 * Created by Matthew on 3/29/2015.
 */
public class GetClosestPubTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(GetClosestPubTask.class);
    private ProtoGameState state;
    private Vertex target;
    public GetClosestPubTask(Blackboard bb) {
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
        logger.info("Getting target.");

    }

    @Override
    public void end() {
        logger.info("Target at " + target);
    }

    /**
     * Perform assumes that it will always
     * succeed currently.  The only way it would
     * fail is if there were no heroes on the
     * board.  That would be a surprise!
     * Additionally the distances it uses
     * are informed by the Manhattan heuristic,
     * the idea is that most of the time, we're
     * going to find the closest bot.
     *
     * Side Effects:  The target on the blackboard is set.
     */
    @Override
    public void perform() {
        if(bb.getGameState().getMe().getGold() < 2){
            //We first check to see if we have money
            //for booze!
            //If we don't we failed.
            logger.info("We don't have any cash!");
            control.finishWithFailure();
            return;
        }

        this.state = bb.getGameState();
        Vertex target = null;
        int minDist = Integer.MAX_VALUE;
        Vertex cur = null;
        GameState.Position myPos = state.getMe().getPos();
        Vertex myVert = new Vertex(myPos, null);
        Manhattan man = new Manhattan(null);
        Map<GameState.Position, Pub> pubMap = state.getPubs();
        for(GameState.Position pos : pubMap.keySet()){
            Pub p = pubMap.get(pos);
            if(p.getPosition().getX() == myPos.getX() && p.getPosition().getY() == myPos.getY()){
                //If we're here we're already at a pub so we just return a move of
                //This could cause problems at the sequence level.
                //Likely the next state will fail on check conditions, but bot itself
                //should notice that there has been a move returned.
                //Additionally I'm not totally sure that this will even ever
                //run.
                logger.info("We are already at the pub!");
                bb.move = BotMove.STAY;
                control.finishWithSuccess();
                return;
            }
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
