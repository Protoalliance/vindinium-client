package com.protoalliance.vindiniumclient.bot.proto;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class LeafTask extends Task {
    protected TaskController controller;

    public LeafTask(ProtoGameState state, Blackboard bb) {
        super(state, bb);
        createController();
    }

    private void createController() {
        controller = new TaskController(this);
    }

    public TaskController getController() {
        return controller;
    }
}
