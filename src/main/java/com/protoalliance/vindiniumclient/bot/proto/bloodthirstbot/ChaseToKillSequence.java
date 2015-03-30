package com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Sequence;

/**
 * Created by Matthew on 3/29/2015.
 */
public class ChaseToKillSequence extends Sequence {

    public ChaseToKillSequence(Blackboard bb) {
        super(bb);
        control.subTasks.add(new GetClosestBotTask(bb));
        control.subTasks.add(new PathfindToClosestBotTask(bb));
        control.subTasks.add(new MoveToTargetTask(bb));

    }
}
