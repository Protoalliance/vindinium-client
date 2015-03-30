package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;

/**
 * This ParentTask executes each of it's children
 * in turn until he has finished all of them.
 *
 * It always starts by the first child,
 * updating each one.
 * If any child finishes with failure, the
 * Sequence fails, and we finish with failure.
 * When a child finishes with success, we
 * select the next child as the update victim.
 * If we have finished updating the last child,
 * the Sequence returns with success.
 * Adapted from http://magicscrollsofcode.blogspot.com/2010/12/behavior-trees-by-example-ai-in-android.html
 *
 * @author Ying, Joseph Sankar
 *
 */
public class Sequence extends ParentTask {
    /**
     * Creates a new instance of the
     * Sequence class
     * @param bb Reference to
     * the AI Blackboard data
     */
    public Sequence(Blackboard bb)
    {
        super(bb);
    }
    /**
     * A child finished with failure.
     * We failed to update the whole sequence.
     * Bail with failure.
     */
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
