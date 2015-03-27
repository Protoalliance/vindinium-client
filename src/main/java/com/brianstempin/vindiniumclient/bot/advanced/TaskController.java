package com.brianstempin.vindiniumclient.bot.advanced;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public class TaskController {
    private boolean done;
    private boolean success;
    private boolean started;
    private Task task;

    public TaskController(Task task) {
        this.done = false;
        this.success = true;
        this.started = false;
        this.task = task;
    }

    public void safeStart() {
        started = true;
        task.start();
    }

    public void safeEnd() {
        this.done = false;
        this.started = false;
        task.end();
    }

    protected void finishWithSuccess() {
        success = true;
        done = true;
    }

    protected void finishWithFailure() {
        success = false;
        done = true;
    }

    public boolean succeeded() {
        return success;
    }

    public boolean failed() {
        return !success;
    }

    public boolean finished() {
        return done;
    }

    public boolean started() {
        return started;
    }

    public void reset() {
        done = false;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
