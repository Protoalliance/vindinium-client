package com.protoalliance.vindiniumclient.bot.proto;

import com.protoalliance.vindiniumclient.bot.BotMove;

public interface ProtoBot {

    public BotMove move(ProtoGameState gameState);

    /**
     * Called before the game is started
     */
    public void setup();

    /**
     * Called after the game
     */
    public void shutdown();

}
