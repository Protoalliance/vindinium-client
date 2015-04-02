package com.protoalliance.vindiniumclient.bot.proto.drunkminebot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.drunkbot.DoIHaveCashSelector;
import com.protoalliance.vindiniumclient.bot.proto.minebot.ChaseToMineSequence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Joseph on 4/2/2015.
 */
public class DrunkMineBot implements ProtoBot{
    private static final Logger logger = LogManager.getLogger(DrunkMineBot.class);

    private Blackboard bb;

    private GetMinesOrGetDrunkSelector getMinesOrGetDrunkSelector;

    public DrunkMineBot() {
        bb = new Blackboard();
        bb.move = null;
        getMinesOrGetDrunkSelector = new GetMinesOrGetDrunkSelector(bb);
    }
    @Override
    public BotMove move(ProtoGameState state) {

        if (getMinesOrGetDrunkSelector.getController().finished()
                || !getMinesOrGetDrunkSelector.getController().started()) {
            getMinesOrGetDrunkSelector = new GetMinesOrGetDrunkSelector(bb);
            getMinesOrGetDrunkSelector.getController().safeStart();
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
            while (bb.move == null && !getMinesOrGetDrunkSelector.getController().finished()) {
                getMinesOrGetDrunkSelector.perform();
            }
            if(getMinesOrGetDrunkSelector.getController().succeeded()){
                getMinesOrGetDrunkSelector = new GetMinesOrGetDrunkSelector(bb);
                getMinesOrGetDrunkSelector.getController().safeStart();
            }
            if(getMinesOrGetDrunkSelector.getController().failed()){
                //logger.info("The sequence failed!  This shouldn't happen!");
                break;
            }
        }

        // logger.info("We returned a move of " + bb.move);

        return bb.move;
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {

    }

}
