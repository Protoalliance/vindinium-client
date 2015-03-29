package com.brianstempin.vindiniumclient.bot.advanced.partybot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Blackboard;
import com.brianstempin.vindiniumclient.bot.advanced.LeafTask;

/**
 * Created by Joseph on 3/27/2015.
 */
public class RandomMovementTask extends LeafTask{

    public RandomMovementTask(AdvancedGameState state, Blackboard bb) {
        super(state, bb);
    }

    @Override
    public boolean canBeUpdated() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {
        int randomNumber = (int)(Math.random() * 4);

        switch(randomNumber) {
            case 1:
                bb.move = BotMove.NORTH;
                break;
            case 2:
                bb.move = BotMove.SOUTH;
                break;
            case 3:
                bb.move = BotMove.EAST;
                break;
            case 4:
                bb.move = BotMove.WEST;
                break;
            default:
                bb.move = BotMove.STAY;
                break;
        }
    }
}
