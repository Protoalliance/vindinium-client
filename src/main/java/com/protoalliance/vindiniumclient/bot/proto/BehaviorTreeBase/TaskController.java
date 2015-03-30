package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

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

    /**
     * Starts the monitored class
     */
    public void safeStart() {
        started = true;
        task.start();
    }

    /**
     * Ends the monitored task
     */
    public void safeEnd() {
        this.done = false;
        this.started = false;
        task.end();
    }

    /**
     * Ends the monitored class, with success
     */
    public void finishWithSuccess() {
        success = true;
        done = true;
    }

    /**
     * Ends the monitored class, with failure
     */
    public void finishWithFailure() {
        success = false;
        done = true;
    }

    /**
     * Indicates whether the task
     * finished successfully
     * @return True if it did, false if it didn't
     */
    public boolean succeeded() {
        return success;
    }

    /**
     * Indicates whether the task
     * finished with failure
     * @return True if it did, false if it didn't
     */
    public boolean failed() {
        return !success;
    }

    /**
     * Indicates whether the task finished
     * @return True if it did, false if it didn't
     */
    public boolean finished() {
        return done;
    }

    /**
     * Indicates whether the class
     * has started or not
     * @return True if it has, false if it hasn't
     */
    public boolean started() {
        return started;
    }

    /**
     * Marks the class as just started.
     */
    public void reset() {
        done = false;
    }

    /**
     * Sets the task reference
     * @param task Task to monitor
     */
    public void  setTask(Task task) {
        this.task = task;
    }
}
