package com.protoalliance.vindiniumclient.bot.proto.tyr;

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

import java.util.List;
import java.util.Map;


/**
 * This version of the behavior is a bit more advanced,
 * we're at least checking to see if we need to go
 * to the bar or not additionally to our check on
 * whether we have cash for the bar or not.
 *
 * Created by Matthew on 3/29/2015.
 */
public class GetPubTarget extends LeafTask {
    private static final Logger logger = LogManager.getLogger(GetPubTarget.class);
    //This sets the life amount we have when we start going
    //towards the pub.
    private static final int BOOZE_THRESHOLD =  20;
    //This sets the amount of life we want to have when we
    //leave the pub
    private static final int SECONDARY_BOOZE_THRESHOLD = 80;
    private ProtoGameState state;
    private Vertex target;
    public GetPubTarget(Blackboard bb) {
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
        //logger.info("Getting target.");

    }

    @Override
    public void end() {
        if(target != null) {
            //logger.info("Target pub at (" + target.getPosition().getX() + "," + target.getPosition().getY() + ")");
        }
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
        //logger.info("We need booze and we will get it!");
        Vertex target = null;
        int minDist = Integer.MAX_VALUE;
        Vertex cur = null;
        Map<GameState.Position, GameState.Hero> heroMap = bb.getGameState().getHeroesByPosition();


        GameState.Position myPos = bb.getGameState().getMe().getPos();
        Vertex myVert = new Vertex(myPos, null);
        Manhattan man = new Manhattan(null);
        Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();
        for(GameState.Position pos : pubMap.keySet()){
            Pub p = pubMap.get(pos);
            if(p.getPosition().getX() == myPos.getX() && p.getPosition().getY() == myPos.getY()){
                //This really shouldn't happen.
                //logger.info("We are already at the pub!");
                bb.move = BotMove.STAY;
                control.finishWithSuccess();
                return;
            }

            /**
             * This is a really ugly block of code to check something at least somewhat
             * complicated.  We check the adjacent vertices to the pub and if they are
             * occupied we break so we can pick another pub.
             */
            boolean breakFlag = false;
            List<Vertex> adjVert = p.getAdjacentVertices();
            if(adjVert == null){
                //If we're in here then there are for
                //some reason no vertices adjacent
                //to the pub.  So we just break
                break;
            }
            for(Vertex v2 : adjVert){
                GameState.Hero checkHero = heroMap.get(v2.getPosition());
                if(checkHero != null &&
                        checkHero.getPos().getX() != bb.getGameState().getMe().getPos().getX() &&
                        checkHero.getPos().getX() != bb.getGameState().getMe().getPos().getX()){
                    logger.info("There is a hero next to this pub his name is " + checkHero.getName());
                    breakFlag = true;
                    break;
                }
            }
            if(breakFlag){
                continue;
            }



            cur = new Vertex(pos, null);
            man.setGoalVertex(cur);
            int est = man.estimate(myVert);
            if(est < minDist){
                minDist = est;
                target = cur;
            }
        }

        //If all of the pubs are occupied just stay still
        //and wait until one is unoccupied.
        if(target == null){
            bb.move = BotMove.STAY;
            control.finishWithSuccess();
            return;
        }


        this.target = target;
        bb.setTarget(target);
        control.finishWithSuccess();
        return;
    }
}
