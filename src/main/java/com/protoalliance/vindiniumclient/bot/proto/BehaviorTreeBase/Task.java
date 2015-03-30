package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class Task {
    public Blackboard bb;

    /**
     * Creates a new instance of the Task class
     * @param bb Reference to the
     * AI Blackboard data
     */
    public Task(Blackboard bb) {
        this.bb = bb;
    }

    /**
     * Override to do a pre-conditions check to
     * see if the task can be updated.
     * @return True if it can, false if it can't
     */
    public abstract boolean checkConditions();

    /**
     * Override to add startup logic to the task
     */
    public abstract void start();

    /**
     * Override to add ending logic to the task
     * Currently end is called every time that a
     * task finishes.
     */
    public abstract void end();

    /**
     * Override to specify the logic the task
     * must update each cycle
     */
    public abstract void perform();

    /**
     * Override to specify the controller the
     * task has
     * @return The specific task controller.
     */
    public abstract TaskController getController();
}
