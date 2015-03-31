package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Matthew on 3/29/2015.
 * The TaskDecorator actually extends task!!!  God help us all.
 * This should actually decorate a parent task/sequence I think.
 */
public class RunUntilFailureDecorator extends ParentTaskDecorator {
    private static final Logger logger = LogManager.getLogger(RunUntilFailureDecorator.class);

    public RunUntilFailureDecorator(Blackboard bb, Task task) {
        super(bb, task);
    }

    @Override
    public void perform() {
        logger.info("Doing Action");

        if(control.finished() && control.succeeded()) {
            //This decorator wrecks shop here,
            //It says to reset and redo the task.
            //The sort of insane thing here is that we
            //don't want to actually call the constructor
            //again.  We just want the loop to keep running.
            //Otherwise we just bail.
            if(control.succeeded()) {
                //This resets the parent task
                control.reset();
                //This resets the child task
                control.currentTask.getController().reset();
                control.safeStart();
                return;
            }else{
                //if we're here we failed.  bail out.
                return;
            }
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
        } else if(control.currentTask.getController().finished()){
            //If we are here the task is finished.
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
            //THIS IS WHERE THE DOING GETS DONE!
        } else {
            control.currentTask.perform();
        }
    }
}
