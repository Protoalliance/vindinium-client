package com.protoalliance.vindiniumclient.bot.proto.minebot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTaskController;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Joseph on 3/30/2015.
 */
public class MineBot implements ProtoBot{
    private static final Logger logger = LogManager.getLogger(MineBot.class);

    private Blackboard bb;
    private ChaseToMineSequence mineSequence;

    public MineBot() {
        bb = new Blackboard();
        mineSequence = new ChaseToMineSequence(bb);
        logger.info("Initializing MineBot");
    }

    @Override
    public BotMove move(ProtoGameState gameState) {
        if (mineSequence.getController().finished() || !mineSequence.getController().started()) {
            bb.setGameState(gameState);
            mineSequence = new ChaseToMineSequence(bb);
            mineSequence.getController().safeStart();
            logger.info("Task is finished or first run in MineBot");
        } else {
            {
                //if we're here we haven't just started at the beginning
                //and we haven't just started a new run of the entire tree
                //so just reset the blackboard to the current state
                bb.setGameState(gameState);
            }
        }


        while (bb.move == null && !mineSequence.getController().finished()) {
            mineSequence.perform();
            logger.info("Performing MineBot");
        }

        if(mineSequence.getController().finished() && bb.move == null) {
            logger.info("We finished the entire tree and returned null!");
        }else{
            logger.info("We retunred a move of " + bb.move);
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
