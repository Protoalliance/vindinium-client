package com.protoalliance.vindiniumclient.bot.proto;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class Task {
    protected ProtoGameState state;
    protected Blackboard bb;

    public Task(ProtoGameState state, Blackboard bb) {
        this.state = state;
        this.bb = bb;
    }

    public abstract boolean canBeUpdated();

    public abstract void start();

    public abstract void end();

    public abstract void perform();

    public abstract TaskController getController();
}
