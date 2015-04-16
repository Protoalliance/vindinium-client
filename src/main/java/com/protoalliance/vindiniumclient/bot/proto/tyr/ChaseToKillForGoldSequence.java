package com.protoalliance.vindiniumclient.bot.proto.tyr;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;


/**
 * Created by Matthew on 3/29/2015.
 */
public class ChaseToKillForGoldSequence extends ParentTask {

    public ChaseToKillForGoldSequence(Blackboard bb) {
        super(bb);
        control.subTasks.add(new GetBotWithMostMinesTask(bb));
        control.subTasks.add(new PathfindToTargetHeroTask(bb));
        control.subTasks.add(new RunUntilFailureDecorator(bb, new MoveToTargetHeroTask(bb)));

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
