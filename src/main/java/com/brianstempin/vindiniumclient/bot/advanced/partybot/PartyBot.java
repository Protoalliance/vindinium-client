package com.brianstempin.vindiniumclient.bot.advanced.partybot;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedBot;
import com.brianstempin.vindiniumclient.bot.advanced.AdvancedGameState;
import com.brianstempin.vindiniumclient.bot.advanced.Blackboard;
import com.brianstempin.vindiniumclient.bot.advanced.TaskController;

/**
 * Created by Joseph on 3/23/2015.
 */
public class PartyBot implements AdvancedBot{
    @Override
    public BotMove move(AdvancedGameState gameState) {
        Blackboard bb = new Blackboard();
        RandomMovementTask randomMovementTask = new RandomMovementTask(gameState, bb);
        //TODO: Add controller as wrapper for task

        //Perform any initialization
        randomMovementTask.start();

        //Run the task
        randomMovementTask.perform();

        //Shutdown the task
        randomMovementTask.end();

        return bb.move;
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {

    }
}
