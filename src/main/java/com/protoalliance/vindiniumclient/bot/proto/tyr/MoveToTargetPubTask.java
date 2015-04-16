package com.protoalliance.vindiniumclient.bot.proto.tyr;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.BotUtils;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Matthew on 4/15/2015.
 */
public class MoveToTargetPubTask extends LeafTask {
    private BotMove retMove;
    private Path path;
    private int curPathIdx;
    private static final Logger logger = LogManager.getLogger(com.protoalliance.vindiniumclient.bot.proto.kronos.MoveToTargetTask.class);

    public MoveToTargetPubTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        if(bb.getPath() == null || bb.getGameState() == null || bb.getTarget() == null){
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        logger.info("Setting next move.");
        this.path = bb.getPath();
        curPathIdx = 1;
    }

    @Override
    public void end() {
        logger.info("Moving " + retMove);
    }

    /**
     * Basic function here is that we check to see if the hero,
     * or a hero is on the target position.  This method and the
     * GetClosestPubTask don't take into account the actual identity
     * of the bot.  If no hero is on the target, then we bail out from
     * the task since we need to go back and pathfind again.  Otherwise
     * we move to the next position on the path and increment the
     * path index value.
     *
     * Side Effects:  On success the bot moves one position forward and
     * the next target on the path is the curPathIdx, additionally data
     * is logged.
     */
    public void perform() {

        if(curPathIdx > path.getVertices().size() - 1){
            control.finishWithFailure();
            return;
        }
        //A short block to check whether the target has moved.
        //We actually don't need to check this since the pub is never
        //going to move.  The better idea is to actually check that
        //we're still where we expected to be.  We could have been
        //killed between last turn and this turn, so it may be
        //necessary to start pathfinding all over again.  If it's
        //the first move on the path though it's not a problem.
        Vertex target = bb.getTarget();
        if(curPathIdx == 0){
            //Do nothing
            //we haven't started
            //the path yet!
        }else{
            Vertex whereWeShouldBe = bb.getPath().getVertices().get(curPathIdx - 1);
            if(whereWeShouldBe.getPosition().getY() == bb.getGameState().getMe().getPos().getY() && whereWeShouldBe.getPosition().getX() == bb.getGameState().getMe().getPos().getX()){
                //Do nothing since we're where we should be
            }else{
                //We finish with failure since we
                //aren't on our original path for
                //some reason.
                control.finishWithFailure();
                return;
            }
            //A check to make sure that there is no bot
            //near the pub we are seeking
            Map<GameState.Position, GameState.Hero> heroMap = bb.getGameState().getHeroesByPosition();
            Vertex v = new Vertex(bb.getTarget().getPosition(), null);
            List<Vertex> adjVert = v.getAdjacentVertices();
            for(Vertex v2 : adjVert){
                GameState.Hero checkHero = heroMap.get(v2.getPosition());
                if(checkHero != null &&
                        checkHero.getPos().getX() != bb.getGameState().getMe().getPos().getX() &&
                        checkHero.getPos().getX() != bb.getGameState().getMe().getPos().getX()){
                    logger.info("A hero moved next to the pub we targeted his name is " + checkHero.getName());
                    control.finishWithFailure();
                    return;
                }
            }



        }

        //If we're here we need to figure out where to move based on
        //current position and the next path vertex.
        retMove = BotUtils.directionTowards(bb.getGameState().getMe().getPos(), path.getVertices().get(curPathIdx).getPosition());
        //Hopefully this works and we target the correct direction, if not we're screwed.
        bb.move = retMove;
        curPathIdx++;
        control.finishWithSuccess();
        return;
    }
}
