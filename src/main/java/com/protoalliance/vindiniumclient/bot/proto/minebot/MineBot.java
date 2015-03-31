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

    private ChaseToMineSequence mineSequence;
    private ParentTaskController mineSequenceController;

    public MineBot() {
        mineSequence = null;
        mineSequenceController = null;
        logger.info("Initializing MineBot");
    }

    @Override
    public BotMove move(ProtoGameState gameState) {
        Blackboard bb = new Blackboard();
        bb.setGameState(gameState);


        if (mineSequence == null || mineSequenceController.finished()) {
            mineSequence = new ChaseToMineSequence(bb);
            mineSequenceController = mineSequence.getController();
            mineSequenceController.safeStart();
            logger.info("Task is finished or first run in MineBot");
        }


        while (bb.move == null) {
            mineSequence.perform();
            logger.info("Performing MineBot");
        }

        if (mineSequenceController.finished()) {
            mineSequenceController.safeEnd();
            logger.info("MineBot finished");
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
