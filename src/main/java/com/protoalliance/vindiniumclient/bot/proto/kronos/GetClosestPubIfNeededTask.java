package com.protoalliance.vindiniumclient.bot.proto.kronos;

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

import java.util.HashMap;
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
public class GetClosestPubIfNeededTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(GetClosestPubIfNeededTask.class);
    //This sets the life amount we have when we start going
    //towards the pub.
    private static final int BOOZE_THRESHOLD =  30;
    //This sets the amount of life we want to have when we
    //leave the pub
    private static final int SECONDARY_BOOZE_THRESHOLD = 80;
    private ProtoGameState state;
    private Vertex target;
    public GetClosestPubIfNeededTask(Blackboard bb) {
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
        if(bb.getGameState().getMe().getGold() < 2){
            //We first check to see if we have money
            //for booze!
            //If we don't we failed.
            //logger.info("We don't have any cash!");
            control.finishWithFailure();
            return;
        }
//        if(bb.getFailedCount() > 3){
//            //We've already failed a large
//            //number of times
//            bb.setFailedCount(0);
//        }

        //An additional check to see if we actually need booze
        //for health, if not we might as well go on a killing
        //spree.
        if(bb.getGameState().getMe().getLife() > BOOZE_THRESHOLD){
            Vertex tar = checkForAdjacentPubs();
            if(tar != null && bb.getGameState().getMe().getLife() < SECONDARY_BOOZE_THRESHOLD){
                //Basically we set everything in checkForAdjacentPubs, and if
                //our life is still below 100 after our first visit and we
                //have the cash, we need to visit again.
                bb.setTarget(tar);
                control.finishWithSuccess();
                return;
            }
            //logger.info("We don't need booze yet!");
            control.finishWithFailure();
            return;
        }

        //logger.info("We need booze and we will get it!");
        Vertex target = null;
        int minDist = Integer.MAX_VALUE;
        Vertex cur = null;
        Map<GameState.Position, GameState.Hero> heroMap = bb.getGameState().getHeroesByPosition();
        GameState.Position myPos = bb.getGameState().getMe().getPos();
        Vertex myVert = new Vertex(myPos, null);
        Manhattan man = new Manhattan(null);
        Pub defaultTarget = null;
        Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();
        for(GameState.Position pos : pubMap.keySet()){
            Pub p = pubMap.get(pos);
            defaultTarget = p;
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
                    //logger.info("There is a hero next to this pub his name is " + checkHero.getName());
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


        if(target == null){
            Vertex def = new Vertex(defaultTarget.getPosition(), null);
            //logger.info("setting default target pub");
            bb.setTarget(def);
            control.finishWithSuccess();
            return;
        }


        this.target = target;
        bb.setTarget(target);
        control.finishWithSuccess();
        return;
    }



    public Vertex checkForAdjacentPubs(){
        GameState.Position myPos = bb.getGameState().getMe().getPos();
        Vertex myVert = bb.getGameState().getBoardGraph().get(myPos);
        Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();

        for(Vertex neighbor : myVert.getAdjacentVertices()){
            if(pubMap.get(neighbor.getPosition()) != null){
                Vertex targetVertex = new Vertex(neighbor.getPosition(), null);
                this.target = targetVertex;
                return targetVertex;
            }
        }
        return null;
    }
}
