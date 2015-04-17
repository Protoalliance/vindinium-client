package com.protoalliance.vindiniumclient.bot.proto.kronos;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


/**
 * Created by Matthew on 3/29/2015.
 */
public class ToKillOrNotToKillTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(ToKillOrNotToKillTask.class);
    public static final double MINE_THRESHOLD = 0.25;
    private ProtoGameState state;
    private GameState.Hero targetHero;
    public ToKillOrNotToKillTask(Blackboard bb) {
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
        //logger.info("Target hero " + targetHero.getName());
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
        GameState.Hero finTar = null;
        String myName = state.getMe().getName();
        String myId = state.getMe().getUserId();
        GameState.Position myPos = state.getMe().getPos();

        Map<GameState.Position, GameState.Hero> heroPos = state.getHeroesByPosition();
        for(GameState.Position pos : heroPos.keySet()){
            GameState.Hero tar = heroPos.get(pos);
            if(pos.getX() == myPos.getX() && pos.getY() == myPos.getY()){
                continue;
            }

            boolean continueFlag = false;
            Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();
            Vertex heroVert = bb.getGameState().getBoardGraph().get(tar.getPos());
            List<Vertex> adjVert = heroVert.getAdjacentVertices();
            for(GameState.Position pubPos : pubMap.keySet()){
                for(Vertex v : adjVert){
                    if(v.getPosition().getX() == pubPos.getX() && v.getPosition().getY() == pubPos.getX()){
                        //logger.info("hero is right next to a pub!");
                        continueFlag = true;
                        break;
                    }
                }
                break;
            }
            if(continueFlag){
                continue;
            }

            int mineCount = tar.getMineCount();

            if(mineCount >= maxMineCount){
                finTar = tar;
                maxMineCount = tar.getMineCount();
            }

        }
        int numMines = bb.getGameState().getMines().size();
        double ratio = ((double) finTar.getMineCount()) / ((double) numMines);
        if(ratio < MINE_THRESHOLD){
           // logger.info("Mine ratio is " + ratio);
            control.finishWithFailure();
            return;
        }


        bb.setTargetHero(finTar);
        targetHero = finTar;
        String tarName = finTar.getName();
        control.finishWithSuccess();
        return;
    }
}
