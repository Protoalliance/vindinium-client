package com.protoalliance.vindiniumclient.bot.proto.tyr;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;

/**
 * Created by Matthew on 4/15/2015.
 */
public class AmILowOnHealthSequence extends ParentTask {

    public AmILowOnHealthSequence(Blackboard bb) {
        super(bb);
        control.subTasks.add(new DoINeedHealthTask(bb));
        control.subTasks.add(new RunUntilSuccessDecorator(bb, new ChaseToDrinkSequence(bb)));
    }

    @Override
    public void childFailed() {
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
