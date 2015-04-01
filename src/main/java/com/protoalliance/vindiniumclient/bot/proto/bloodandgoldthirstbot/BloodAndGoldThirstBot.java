package com.protoalliance.vindiniumclient.bot.proto.bloodandgoldthirstbot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Matthew on 3/29/2015.
 */
public class BloodAndGoldThirstBot implements ProtoBot{
    private Blackboard bb;
    private ChaseToKillForGoldSequence seq;
    private static final Logger logger = LogManager.getLogger(BloodAndGoldThirstBot.class);

    public BloodAndGoldThirstBot(){
        bb = new Blackboard();
        seq = new ChaseToKillForGoldSequence(bb);
    }

    @Override
    public BotMove move(ProtoGameState state) {

        if (seq.getController().finished() || !seq.getController().started()) {
            bb.setGameState(state);
            seq = new ChaseToKillForGoldSequence(bb);
            seq.getController().safeStart();
            bb.move = null;
        } else {
            //if we're here we haven't just started at the beginning
            //and we haven't just started a new run of the entire tree
            //so just reset the blackboard to the current state
            bb.setGameState(state);
            //We also need to set the move to null!
            //Otherwise the bot thinks it's already assigned
            //a move.
            bb.move = null;
        }

        //The idea here is that we keep calling until bb.move is a real
        //move or we just finish for some reason without that happening.
        while(bb.move == null) {
            while (bb.move == null && !seq.getController().finished()) {
                seq.perform();
            }
            if (seq.getController().finished() && bb.move == null) {
                if (seq.getController().succeeded()) {
                    //If we're here the tree completed, so we need
                    //Since the tree completed, either we
                    //actually finished the path or the target bot
                    //moved.
                    seq = new ChaseToKillForGoldSequence(bb);
                    seq.getController().safeStart();
                    bb.move = null;
                }
            }
        }

        logger.info("We returned a move of " + bb.move);
        return bb.move;
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {

    }
}
