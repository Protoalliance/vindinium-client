package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;


/**
 * Created by Matthew on 3/29/2015.
 */
public class ChaseToKillSequence extends ParentTask {

    public ChaseToKillSequence(Blackboard bb) {
        super(bb);
        control.subTasks.add(new GetClosestBotTask(bb));
        control.subTasks.add(new PathfindToClosestBotTask(bb));
        control.subTasks.add(new RunUntilFailureDecorator(bb, new MoveToTargetTask(bb)));

    }

    @Override
    public void childFailed()
    {
        control.finishWithFailure();
    }
    /**
     * A child has finished with success
     * Select the next one to update. If
     * it's the last, we have finished with
     * success.
     */
    @Override
    public void childSucceeded() {
        int curPos = control.subTasks.indexOf(control.currentTask);
        if( curPos == (control.subTasks.size() - 1)) {
            control.finishWithSuccess();
        } else {
            //Pre increment the pointer to the next position.
            control.currentTask = control.subTasks.get(++curPos);
            if(!control.currentTask.checkConditions()) {
                control.finishWithFailure();
            }
        }
    }
}
