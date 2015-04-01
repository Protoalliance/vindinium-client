package com.protoalliance.vindiniumclient.bot.proto.bloodandgoldthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.Manhattan;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


/**
 * Created by Matthew on 3/29/2015.
 */
public class GetBotWithMostMinesTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(GetBotWithMostMinesTask.class);
    private ProtoGameState state;
    private Vertex target;
    public GetBotWithMostMinesTask(Blackboard bb) {
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
        this.state = bb.getGameState();
        int maxMineCount = Integer.MIN_VALUE;
        GameState.Hero tar = null;


        Map<GameState.Hero, Integer> heroPos = state.getHeroesByMineCount();
        for(GameState.Hero hero : heroPos.keySet()){
            if(hero == state.getMe()){
                //Don't target yourself!
                continue;
            }

            int val = hero.getMineCount();

            if(val > maxMineCount){
                tar = hero;
                maxMineCount = hero.getMineCount();
            }
        }
        bb.setTargetHero(tar);
        control.finishWithSuccess();
        return;
    }
}
