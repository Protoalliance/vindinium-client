package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTaskController;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.TaskController;

/**
 * Created by Matthew on 3/29/2015.
 */
public class ChaseToKillSequence extends ParentTask {

    public ChaseToKillSequence(Blackboard bb) {
        super(bb);
    }

    @Override
    public void childSucceeded() {
        int curPos = control.subTasks.indexOf(control.currentTask);
        if(curPos == control.subTasks.size() - 1){
            control.finishWithSuccess();
        }else{
            control.currentTask = control.subTasks.get(++curPos);
            if(!control.currentTask.checkConditions())
                control.finishWithFailure();
        }
    }

    @Override
    public void childFailed() {
        control.finishWithFailure();
    }

    @Override
    public boolean canBeUpdated() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {

    }

    @Override
    public ParentTaskController getController() {
        return control;
    }
}
