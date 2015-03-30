package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

/**
 * Created by Matthew on 3/29/2015.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Inner node of the behavior tree, a
 * flow director node that selects the
 * child to be executed next.
 * (Sounds macabre, hu?)
 *
 * Sets a specific kind of TaskController for
 * these kinds of tasks.
 *
 * @author Ying
 *
 */
public abstract class ParentTask extends Task {
    /**
     * TaskController for the parent task
     */
    private static final Logger logger = LogManager.getLogger(ParentTask.class);
    public ParentTaskController control;
    public ParentTask(Blackboard bb) {
        super(bb);
        createController();
    }

    /**
     * Creates the TaskController.
     */
    private void createController() {
        this.control = new ParentTaskController(this);
    }
    /**
     * Gets the control reference
     */

    @Override
    public ParentTaskController getController() {
        return control;
    }
    /**
     * Basically we're checking to make sure that
     * we actually have some leaf tasks to execute.
     */

    public boolean checkConditions() {
        logger.info("Checking Conditions");
        return control.subTasks.size() > 0;
    }
    /**
     * Abstract to be overridden in child
     * classes. Called when a child finishes
     * with success.
     */
    public abstract void childSucceeded();
    /**
     * Abstract to be overridden in child
     * classes. Called when a child finishes
     * with failure.
     */
    public abstract void childFailed();
    /**
     * Checks whether the child has started,
     * finished or needs updating, and takes
     * the needed measures in each case
     *
     * "curTask" is the current selected task,
     * a member of our ParentTaskController
     */

    public void perform() {
        logger.info("Doing Action");
        if(control.finished()) {
        // If this parent task is finished
        // return without doing naught.
            return;
        }
        if(control.currentTask == null) {
        // If there is a null child task
        // selected we've done something wrong
            return;
        }
        // If we do have a curTask...
        //and it's not started yet, start it.
        if( !control.currentTask.getController().started()){
            control.currentTask.getController().safeStart();
        }
        //Task is finished
        else if(control.currentTask.getController().finished()){
            //end the task
            control.currentTask.getController().safeEnd();
            //Check success or failure
            if(control.currentTask.getController().succeeded()){
                this.childSucceeded();
            }
            if(control.currentTask.getController().failed()){
                this.childFailed();
            }
        //Task is ready, so update
        } else {
            control.currentTask.perform();
        }
    }

    /**
     * Ends the task
     */
    public void end() {
        logger.info("Ending");
    }
    /**
     * Starts the task, and points the
     * current task to the first one
     * of the available child tasks.
     */
    public void start() {
        logger.info("Starting");
        control.currentTask = control.subTasks.get(0);
        if(control.currentTask == null) {
            logger.info("Current task has a null action");
        }
    }
}
