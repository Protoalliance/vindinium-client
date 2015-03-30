package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;
/**
 * Created by Joseph on 3/26/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class TaskDecorator extends Task {
    public Task task;

    public TaskDecorator(Blackboard bb, Task task) {
        super(bb);
        initTask(task);
    }

    /**
     * Initializes the task reference
     * @param task Task to decorate
     */
    private void initTask(Task task) {
        this.task = task;
        task.getController().setTask(this);
    }
    /**
     * Decorate the CheckConditions no decoration
     * really needed here usually.
     */
    public boolean checkConditions() {
        return task.checkConditions();
    }

    /**
     * Decorate the start
     */
    public void start() {
        task.start();
    }

    /**
     * Decorate the end, also not usually decorated.
     */
    public void end() {
        task.end();
    }

    /**
     * Decorate the request for the
     * Control reference
     */
    public TaskController getController() {
        return task.getController();
    }

}
