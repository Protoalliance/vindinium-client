package com.protoalliance.vindiniumclient.bot.advanced;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class Task {
    protected AdvancedGameState state;
    protected Blackboard bb;

    public Task(AdvancedGameState state, Blackboard bb) {
        this.state = state;
        this.bb = bb;
    }

    public Task(Blackboard bb) {

    }

    public abstract boolean canBeUpdated();

    public abstract void start();

    public abstract void end();

    public abstract void perform();

    public abstract TaskController getController();
}
