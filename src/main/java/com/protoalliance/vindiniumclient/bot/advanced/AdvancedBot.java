package com.protoalliance.vindiniumclient.bot.advanced;

import com.protoalliance.vindiniumclient.bot.BotMove;

public interface AdvancedBot {

    public BotMove move(AdvancedGameState gameState);

    /**
     * Called before the game is started
     */
    public void setup();

    /**
     * Called after the game
     */
    public void shutdown();

}
