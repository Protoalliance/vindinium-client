package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;
/**
 * Created by Joseph on 3/26/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public abstract class TaskDecorator extends Task {
    private Task task;

    public TaskDecorator(Blackboard bb, Task task) {
        super(bb);
        initTask(task);
    }

    private void initTask(Task task) {
        this.task = task;
        task.getController().setTask(this);
    }

    public boolean checkConditions() {
        return task.checkConditions();
    }

    public void start() {
        task.start();
    }

    public void end() {
        task.end();
    }

    public TaskController getController() {
        return task.getController();
    }

}
