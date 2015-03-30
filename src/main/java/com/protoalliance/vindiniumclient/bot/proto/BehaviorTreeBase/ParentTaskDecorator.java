package com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase;

/**
 * Created by Matthew on 3/30/2015.
 */
public class ParentTaskDecorator extends ParentTask{
    public ParentTaskDecorator(Blackboard bb, Task task) {
        super(bb);
    }

    @Override
    public void childSucceeded() {

    }

    @Override
    public void childFailed() {

    }
}
