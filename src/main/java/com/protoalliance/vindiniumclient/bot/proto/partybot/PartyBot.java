package com.protoalliance.vindiniumclient.bot.proto.partybot;

import com.protoalliance.vindiniumclient.bot.BotMove;
import com.protoalliance.vindiniumclient.bot.proto.ProtoBot;
import com.protoalliance.vindiniumclient.bot.proto.ProtoGameState;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;

/**
 * Created by Joseph on 3/23/2015.
 */
public class PartyBot implements ProtoBot {
    @Override
    public BotMove move(ProtoGameState gameState) {
        Blackboard bb = new Blackboard();
        RandomMovementTask randomMovementTask = new RandomMovementTask(bb);
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
