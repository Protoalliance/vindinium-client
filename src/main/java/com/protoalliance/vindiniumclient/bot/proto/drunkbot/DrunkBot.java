package com.protoalliance.vindiniumclient.bot.proto.drunkbot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Matthew on 3/29/2015.
 */
public class DrunkBot implements ProtoBot{
    private Blackboard bb;
    private ChaseToDrinkSequence seq;
    private static final Logger logger = LogManager.getLogger(DrunkBot.class);

    public DrunkBot(){
        bb = new Blackboard();
        bb.move = null;
        seq = new ChaseToDrinkSequence(bb);
    }

    @Override
    public BotMove move(ProtoGameState state) {

        if (seq.getController().finished() || !seq.getController().started()) {
            seq = new ChaseToDrinkSequence(bb);
            seq.getController().safeStart();
            bb.setGameState(state);
            bb.move = null;
        } else {
            //if we're here we haven't just started at the beginning
            //and we haven't just started a new run of the entire tree
            //so just reset the blackboard to the current state
            bb.setGameState(state);
            bb.move = null;
        }

        //The idea here is that we keep calling until bb.move is a real
        //move or we just finish for some reason without that happening.
        while(bb.move == null) {
            while (bb.move == null && !seq.getController().finished()) {
                seq.perform();
            }
            if(seq.getController().succeeded()){
                seq = new ChaseToDrinkSequence(bb);
                seq.getController().safeStart();
            }
            if(seq.getController().failed()){
                logger.info("The sequence failed!  This shouldn't happen!");
                break;
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
