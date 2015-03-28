package com.brianstempin.vindiniumclient.bot.advanced.partybot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.LeafTask;

/**
 * Created by Joseph on 3/27/2015.
 */
public class RandomMovementTask extends LeafTask{
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
                return BotMove.NORTH;
            case 2:
                return BotMove.SOUTH;
            case 3:
                return BotMove.EAST;
            case 4:
                return BotMove.WEST;
            default:
                return BotMove.STAY;
        }
    }
}
