package com.protoalliance.vindiniumclient.bot.proto.drunkbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.ParentTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Matthew on 3/29/2015.
 * The TaskDecorator actually extends task!!!  God help us all.
 * This should actually decorate a parent task/sequence I think.
 */
public class RunUntilFailureDecorator extends ParentTask {
    private static final Logger logger = LogManager.getLogger(RunUntilFailureDecorator.class);
    private Task task;

    /**
     * When instantiating RunUntilFailureDecorator
     * objects keep in mind that the task passed in
     * is the task that will continue running until
     * the Decorator fails.
     *
     * @param bb
     * @param task
     */
    public RunUntilFailureDecorator(Blackboard bb, Task task) {
        super(bb);
        this.task = task;
        control.subTasks.add(task);
    }

    /**
     * This is normally where we would actually return
     * success, but we don't want to return success
     * until there's a failure, so this is called when
     * the child task actually fails.  When whatever
     * is over this task calls it we will return
     * that the task is finished and succeeded.
     */
    @Override
    public void childSucceeded() {
        control.finishWithSuccess();
    }

    /**
     * This should never happen.  When the child fails
     * we report success.
     */
    @Override
    public void childFailed() {
        control.finishWithFailure();
    }

    /**
     * Ends the task
     */
    public void end() {
        logger.info("Run until failure complete.");
    }

    /**
     * This is also overridden to keep the
     * task that is started as always the one
     * that is passed in.
     */

    @Override
    public void start() {
        logger.info("Starting run until failure.");
        control.currentTask = task;
        control.currentTask.getController().safeStart();
        control.currentTask.perform();
    }

    /**
     * This the part that makes this a decorator.
     * We override the perform method here to
     * set up the repetition.
     */
    @Override
    public void perform() {
        logger.info("Doing Action");

        if (control.currentTask == null) {
            // If there is a null child task
            // selected we've done something wrong
            logger.info("Current task was null!");
            return;
        }

        if (control.currentTask.getController().finished()) {
            //This decorator wrecks shop here,
            //It says to reset and redo the task.
            //The sort of insane thing here is that we
            //don't want to actually call the constructor
            //again.  We just want the loop to keep running.
            //If there's a failure we actually call success!
            if (control.currentTask.getController().succeeded()) {
                //This resets the child task
                control.currentTask.getController().reset();
                //No need to start again, that would only
                //reset the index!
                //No need to reset the Decorator's control
                //either since it never succeeded or failed.
                return;
            } else {
                //If we're here we finally failed
                //We return success!
                childSucceeded();
                return;
            }
            //If we're not done yet we should just keep performing.
        } else {
            control.currentTask.perform();
        }
    }
}
