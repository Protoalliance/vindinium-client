package com.protoalliance.vindiniumclient.bot.proto.minebot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;

/**
 * Created by Joseph on 3/30/2015.
 */
public class ChaseToMineSequence extends ParentTask{
    public ChaseToMineSequence(Blackboard bb) {
        super(bb);
        control.subTasks.add(new GetClosestMineTask(bb));
        control.subTasks.add(new PathfindToClosestMineTask(bb));
        control.subTasks.add(new MoveToTargetTask(bb));

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
    public void childSucceeded()
    {
        int curPos =
                control.subTasks.indexOf(control.currentTask);
        if( curPos ==
                (control.subTasks.size() - 1))
        {
            control.finishWithSuccess();
        }
        else
        {
            control.currentTask =
                    control.subTasks.get(curPos+1);
            if(!control.currentTask.checkConditions())
            {
                control.finishWithFailure();
            }
        }
    }
}
