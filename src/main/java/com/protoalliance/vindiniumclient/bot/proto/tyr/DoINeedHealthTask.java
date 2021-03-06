package com.protoalliance.vindiniumclient.bot.proto.tyr;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

/**
 * This version of the behavior is a bit more advanced,
 * we're at least checking to see if we need to go
 * to the bar or not additionally to our check on
 * whether we have cash for the bar or not.
 *
 * Created by Matthew on 3/29/2015.
 */
public class DoINeedHealthTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(GetPubTarget.class);
    //This sets the life amount we have when we start going
    //towards the pub.
    private static final int BOOZE_THRESHOLD =  25;
    //This sets the amount of life we want to have when we
    //leave the pub
    private static final int SECONDARY_BOOZE_THRESHOLD = 80;
    private ProtoGameState state;
    private Vertex target;
    public DoINeedHealthTask(Blackboard bb) {
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
        //An additional check to see if we actually need booze
        //for health, if not we might as well go on a killing
        //spree.
        if(bb.getGameState().getMe().getLife() > BOOZE_THRESHOLD){
            Vertex tar = checkForAdjacentPubs();
            if(tar != null && bb.getGameState().getMe().getLife() < SECONDARY_BOOZE_THRESHOLD) {
                //Basically we set everything in checkForAdjacentPubs, and if
                //our life is still below 100 after our first visit and we
                //have the cash, we need to visit again.
                //bb.setTarget(tar);
                control.finishWithSuccess();
                return;
            }
            //If we're here then our life was higher than the booze
            //threshold and we don't need to hit the pub again.
            control.finishWithFailure();
            return;
        }
        //logger.info("We don't need booze yet!");
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
