package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Task;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.TaskDecorator;

/**
 * Created by Matthew on 3/29/2015.
 * The TaskDecorator actually extends task!!!  God help us all.
 */
public class RunEndlessDecorator extends TaskDecorator{
    public RunEndlessDecorator(Blackboard bb, Task task) {
        super(bb, task);
    }

    @Override
    public boolean checkConditions() {
        return false;
    }

    @Override
    public void perform() {
        while(true){

        }
    }
}
