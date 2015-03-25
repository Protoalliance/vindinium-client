package com.brianstempin.vindiniumclient.bot.advanced;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/24/2015.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 */
public class ParentTaskController extends TaskController{
    public ArrayList<Task> subTasks;
    public Task currentTask;

    public ParentTaskController(Task task) {
        super(task);

        subTasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        subTasks.add(task);
    }

    public void reset() {
        super.reset();

        currentTask = subTasks.get(0);
    }



}
