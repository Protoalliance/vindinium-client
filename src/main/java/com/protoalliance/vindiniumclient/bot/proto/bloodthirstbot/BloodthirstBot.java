package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;
import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Matthew on 3/29/2015.
 */
public class BloodthirstBot implements ProtoBot{
    private Blackboard bb;
    private ChaseToKillSequence seq;
    private static final Logger logger = LogManager.getLogger(BloodthirstBot.class);

    public BloodthirstBot(){
        bb = new Blackboard();
        seq = new ChaseToKillSequence(bb);
    }

    @Override
    public BotMove move(ProtoGameState state) {

        if (seq.getController().finished() || !seq.getController().started()) {
            seq = new ChaseToKillSequence(bb);
            seq.getController().safeStart();
            bb.setGameState(state);
        } else {
            //if we're here we haven't just started at the beginning
            //and we haven't just started a new run of the entire tree
            //so just reset the blackboard to the current state
            bb.setGameState(state);
        }

        //The idea here is that we keep calling until bb.move is a real
        //move or we just finish for some reason without that happening.
        while (bb.move == null && !seq.getController().finished()){
            seq.perform();
        }
        if(seq.getController().finished() && bb.move == null) {
            logger.info("We finished the entire tree and returned null!");
        }else{
            logger.info("We returned a move of " + bb.move);
        }
        return bb.move;
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {

    }
}
