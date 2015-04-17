package com.protoalliance.vindiniumclient.bot.proto.miningdrunkbot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Joseph on 3/27/2015.
 */
public class RandomMovementTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(RandomMovementTask.class);
    public RandomMovementTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        return true;
    }


    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {

       // logger.info("Deciding which direction to go.");
        int randomNumber = (int)(Math.random() * 4);

        switch(randomNumber) {
            case 1:
              //  logger.info("Going North.");
                bb.move = BotMove.NORTH;
                break;
            case 2:
               // logger.info("Going south.");
                bb.move = BotMove.SOUTH;
                break;
            case 3:
               // logger.info("Going east.");
                bb.move = BotMove.EAST;
                break;
            case 4:
              //  logger.info("Going west.");
                bb.move = BotMove.WEST;
                break;
            default:
               // logger.info("Going nowhere!");
                bb.move = BotMove.STAY;
                break;
        }
    }
}
