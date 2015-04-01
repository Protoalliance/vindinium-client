package com.protoalliance.vindiniumclient.bot.proto.bloodandgolddrunkbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Task;


/**
 * This parent task selects one of it's
 * children to update.
 *
 * To select a child, it starts from the
 * beginning of it's children vector
 * and goes one by one until it finds one
 * that passes the CheckCondition test.
 * It then updates that child until its
 * finished.
 * If the child finishes with failure,
 * it continues down the list looking another
 * candidate to update, and if it doesn't
 * find it, it finishes with failure.
 * If the child finishes with success, the
 * Selector considers it's task done and
 * bails with success.
 *
 * @author Matthew Neal
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 *
 */
public class DrinkOrKillSelector extends ParentTask{
    public DrinkOrKillSelector(Blackboard bb) {
        super(bb);
        control.subTasks.add(new ChaseToDrinkSequence(bb));
        control.subTasks.add(new ChaseToKillForGoldSequence(bb));
    }

    /**
     * In case of child finishing with
     * sucess, our job here is done, finish
     * with sucess
     * as well
     */
    @Override
    public void childSucceeded() {
        control.finishWithSuccess();
    }

    /**
     * In case of child finishing with
     * failure we find a new one to update,
     * or fail if none is to be found
     */
    @Override
    public void childFailed() {
        control.currentTask = chooseNewTask();
        if(control.currentTask == null) {
            control.finishWithFailure();
        }
    }

    /**
     * Chooses the new task to update.
     * @return The new task, or null
     * if none was found
     */
    public Task chooseNewTask()
    {
        Task task = null;
        boolean done = false;
        int curPos  = control.subTasks.indexOf(control.currentTask);
        while(!done)
        {
            if(curPos == (control.subTasks.size() - 1)) {
                done = true;
                task = null;
                break;
            }
            curPos++;
            task = control.subTasks.get(curPos);
            if(task.checkConditions()) {
                done = true;
            }
        }
        return task;
    }
}
