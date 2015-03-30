package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public class ParentTaskController extends TaskController{

    /**
    * List of child tasks
    */
    public ArrayList<Task> subTasks;
    /**
     * Current updating task
     */
    public Task currentTask;

    /**
     * Creates a new instance of the
     * ParentTaskController class
     * @param task
     */
    public ParentTaskController(Task task) {
        super(task);
        subTasks = new ArrayList<>();
        currentTask = null;
    }

    /**
     * Adds a new subtask to the end
     * of the subtask list.
     * @param task Task to add
     */
    public void addTask(Task task) {
        subTasks.add(task);
    }

    /**
     * Resets the task as if it had
     * just started.
     */
    public void reset() {
        super.reset();
        currentTask = subTasks.get(0);
    }
}
