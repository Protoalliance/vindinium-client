package com.protoalliance.vindiniumclient.bot.proto.tyr;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.BotUtils;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Matthew on 3/29/2015.
 */
public class MoveToTargetHeroTask extends LeafTask {
    private BotMove retMove;
    private Path path;
    private int curPathIdx;
    private static final Logger logger = LogManager.getLogger(MoveToTargetHeroTask.class);

    public MoveToTargetHeroTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        if(bb.getPath() == null || bb.getGameState() == null || bb.getTargetHero() == null){
            return false;
        }
        return true;
    }

    @Override
    public void start() {
       // logger.info("Setting next move.");
        this.path = bb.getPath();
        curPathIdx = 1;
    }

    @Override
    public void end() {
        //logger.info("Moving " + retMove);
    }

    /**
     * Basic function here is that we check to see if the hero,
     * or a hero is on the target position.  This method actually
     * takes account of whether the path leads to the bot we want
     * or not.  If it doesn't we have to rerun everything, to
     * continue to target the correct bot.  The alternative to
     * this is in the MoveToTargetTask class.
     *
     *
     * Side Effects:  On success the bot moves one position forward and
     * the next target on the path is the curPathIdx, additionally data
     * is logged.
     */
    @Override
    public void perform() {

        if(curPathIdx > bb.getPath().getVertices().size() - 1){
            control.finishWithFailure();
            return;
        }
        //A short block to check whether the target has moved.  He likely has and we'll fail,
        //if not though we'll keep going after him.

        //Since there are a bunch of changes here we need to actually pull the last
        //node of the path and make sure that node is correct at this point.
        Vertex target = bb.path.getVertices().getLast();
        boolean flag = false;
        //What we're actually doing here is checking that the target is in the same position that we pathfound to.
        //There's no need to do any looping.
        if(bb.targetHero.getPos().getY() == target.getPosition().getY() && bb.targetHero.getPos().getX() == target.getPosition().getX()){
            //If we're in here then the target hasn't moved
            flag = true;
        }

        //If the flag is false then there is no hero at our target position
        //therefore we fail and go out to look for another path.
        if(!flag){
            control.finishWithFailure();
            return;
        }

        //A block to check to see if the target has moved next to a pub.
        Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();
        Vertex heroVert = bb.getGameState().getBoardGraph().get(bb.targetHero.getPos());
        List<Vertex> adjVert = heroVert.getAdjacentVertices();
        for(GameState.Position pubPos : pubMap.keySet()){
            for(Vertex v : adjVert){
                if(v.getPosition().getX() == pubPos.getX() && v.getPosition().getY() == pubPos.getX()){
                    logger.info("hero is right next to a pub!");
                    control.finishWithFailure();
                    return;
                }
            }
        }



        //If we're here we need to figure out where to move based on
        //current position and the next path vertex.
        retMove = BotUtils.directionTowards(bb.getGameState().getMe().getPos(), path.getVertices().get(curPathIdx).getPosition());
        //Hopefully this works and we target the correct direction, if not we're screwed.
        //logger.info("Move direction " + retMove + " target vertex (" + target.getPosition().getX() + "," + target.getPosition().getY() + ")");
        bb.move = retMove;
        curPathIdx++;
        control.finishWithSuccess();
        return;
    }
}
