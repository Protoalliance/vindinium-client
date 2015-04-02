package com.protoalliance.vindiniumclient.bot.proto.drunkminebot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Task;
import com.protoalliance.vindiniumclient.bot.proto.drunkbot.DoIHaveCashSelector;
import com.protoalliance.vindiniumclient.bot.proto.minebot.ChaseToMineSequence;

/**
 * Created by Joseph on 4/2/2015.
 * Designed to fix a flaw in MineBot where the bot will crash if it owns all the mines.
 * With this bot, it will try to get all the mines. If it does so, it will try to get drunk
 * by visiting the nearest pub if it has cash. Otherwise, it will move randomly.
 */
public class GetMinesOrGetDrunkSelector extends ParentTask {

    public GetMinesOrGetDrunkSelector(Blackboard bb) {
        super(bb);
        control.subTasks.add(new ChaseToMineSequence(bb));
        control.subTasks.add(new DoIHaveCashSelector(bb));
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
