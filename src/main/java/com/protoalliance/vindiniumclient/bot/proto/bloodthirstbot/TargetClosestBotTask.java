package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;

/**
 * Created by Matthew on 3/29/2015.
 */
public class TargetClosestBotTask extends LeafTask {
    public TargetClosestBotTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean canBeUpdated() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {

    }
}
