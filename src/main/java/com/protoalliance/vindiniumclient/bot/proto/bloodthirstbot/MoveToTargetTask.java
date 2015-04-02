package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;
import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.BotUtils;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by Matthew on 3/29/2015.
 */
public class MoveToTargetTask extends LeafTask {
    private BotMove retMove;
    private Path path;
    private int curPathIdx;
    private static final Logger logger = LogManager.getLogger(MoveToTargetTask.class);

    public MoveToTargetTask(Blackboard bb) {
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
        //logger.info("Setting next move.");
        this.path = bb.getPath();
        curPathIdx = 1;
    }

    @Override
    public void end() {
        //logger.info("Moving " + retMove);
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
    @Override
    public void perform() {

        if(curPathIdx > path.getVertices().size() - 1){
            control.finishWithFailure();
            return;
        }
        //A short block to check whether the target has moved.  He likely has and we'll fail,
        //if not though we'll keep going after him.
        Vertex target = bb.getTarget();
        boolean flag = false;
        Map<GameState.Position, GameState.Hero> heroPos = bb.getGameState().getHeroesByPosition();
        for(GameState.Position pos : heroPos.keySet()){
            Vertex cur = new Vertex(pos, null);
            //Done because I don't want to override equals, call me lazy its ok.
            if(cur.getPosition().getY() == target.getPosition().getY() && cur.getPosition().getX() == target.getPosition().getX()){
                //If we're in here then the target hasn't moved
                flag = true;
            }
        }
        //If the flag is false then there is no hero at our target position
        //therefore we fail and go out to look for another path.
        if(!flag){
            control.finishWithFailure();
            return;
        }
        //If we're here we need to figure out where to move based on
        //current position and the next path vertex.
        retMove = BotUtils.directionTowards(bb.getGameState().getMe().getPos(), path.getVertices().get(curPathIdx).getPosition());
        //Hopefully this works and we target the correct direction, if not we're screwed.
       // logger.info("Move direction " + retMove + " target vertex (" + target.getPosition().getX() + "," + target.getPosition().getY() + ")");
        bb.move = retMove;
        curPathIdx++;
        control.finishWithSuccess();
        return;
    }
}
