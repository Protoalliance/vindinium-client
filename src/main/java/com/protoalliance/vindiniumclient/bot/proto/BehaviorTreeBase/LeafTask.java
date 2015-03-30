package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;
/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class LeafTask extends Task {

    public TaskController control;

    /**
     * Creates a new instance of the
     * LeafTask class
     * @param bb Reference to the
     * AI Blackboard data
     */
    public LeafTask(Blackboard bb) {
        super(bb);
        createController();
    }

    /**
     * Creates the controller for the class
     */
    private void createController() {
        control = new TaskController(this);
    }

    /**
     * Gets the controller reference.
     */
    public TaskController getController() {
        return control;
    }

}
