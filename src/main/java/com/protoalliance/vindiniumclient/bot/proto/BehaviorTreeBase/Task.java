package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class Task {
    protected Blackboard bb;

    public Task(Blackboard bb) {
        this.bb = bb;
    }

    public boolean checkConditions() {
        return true;
    }

    public abstract boolean canBeUpdated();

    public abstract void start();

    public abstract void end();

    public abstract void perform();

    public abstract TaskController getController();
}
