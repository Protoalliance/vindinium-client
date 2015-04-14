package com.protoalliance.vindiniumclient.bot.proto.kronos;

import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.Blackboard;
import com.protoalliance.vindiniumclient.bot.proto.BehaviorTreeBase.LeafTask;
import com.protoalliance.vindiniumclient.bot.proto.Pub;
import com.protoalliance.vindiniumclient.bot.proto.Vertex;
import com.protoalliance.vindiniumclient.bot.proto.astar.AStar;
import com.protoalliance.vindiniumclient.bot.proto.astar.Path;
import com.protoalliance.vindiniumclient.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * This is actually a pretty complex implementation of pathfind to closest pub
 * we check the closest pub first to see if there's a bot in the way, if not we
 * just go to the next until we run out of pubs.  Worst case we just run into
 * the bot and die.  Best case we pick the right pub and live.
 */
public class PathfindToClosestPubTask extends LeafTask {
    private static final Logger logger = LogManager.getLogger(PathfindToClosestPubTask.class);

    public PathfindToClosestPubTask(Blackboard bb) {
        super(bb);
    }

    @Override
    public boolean checkConditions() {
        if(bb.getGameState() == null || bb.getTarget() == null){
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        //logger.info("Calculating Path");
    }

    @Override
    public void end() {

    }

    @Override
    public void perform() {
        GameState.Position myPos = bb.getGameState().getMe().getPos();
        //Need to actually get vertex in graph rather than
        //make up a vertex.
        Map graph = bb.getGameState().getBoardGraph();
        Vertex myVert = (Vertex) graph.get(myPos);
        Map<GameState.Position, Pub> pubMap = bb.getGameState().getPubs();
        //pubMap.remove(bb.getTarget().getPosition());
        boolean safeTarget = false;
        Path p = null;
        Vertex tar = bb.getTarget();

        Stack<Vertex> vertStack = new Stack<Vertex>();
        for(GameState.Position pos : pubMap.keySet()){
            vertStack.push(new Vertex(pos, null));
        }
        String myName = bb.getGameState().getMe().getName();
        Map<GameState.Position, GameState.Hero> m = bb.getGameState().getHeroesByPosition();

        //Loop through mines looking for a safeTarget
        while(!vertStack.empty() && !safeTarget) {
            AStar a = new AStar(bb.getGameState(), myVert, tar);
            p = a.getPath();
            if (p == null) {
                logger.info("There's no path!");
                control.finishWithFailure();
                return;
            }


            LinkedList<Vertex> vertList = p.getVertices();
            Iterator<Vertex> it = vertList.iterator();

            //Throw away the first vertex since that's where we are
            //removes the need for that error check.
            //The idea with this is that if we set the safe target
            //tag to true and we make it all the way through without
            //resetting then we have a good path.
            //Even better is to check and make sure there are
            //no bots adjacent to the path.
            safeTarget = true;
            while (it.hasNext()) {
                Vertex v = it.next();
                List<Vertex> adjVert = v.getAdjacentVertices();
                GameState.Hero h1 = m.get(v.getPosition());
                if (h1 != null && h1.getName() != myName) {
                    logger.info("Bot on path square!");
                    safeTarget = false;
                    break;
                }
                boolean innerLoopFlag = true;
                for(Vertex v2 : adjVert){
                    GameState.Hero h = m.get(v2.getPosition());
                    if(h != null && h.getName() != myName){

                        logger.info("Bot on square adjacent to path!");
                        safeTarget = false;
                        innerLoopFlag=false;
                        break;
                    }
                }
                if(!innerLoopFlag){
                    break;
                }
            }

            tar = vertStack.pop();
        }

        if(safeTarget){
            logger.info("safetarget true!!!");
        }


        //If we're out here then we have either run out of pubs
        //or we actually found the best target.  If we didn't
        //find a safe path then we just go to the last path we
        //checked.  This is really heavyweight.
        bb.setPath(p);
        //logger.info("Path found is " + p);
        if(p == null){
            //do nothing
            logger.info("BAD PATH!");
        }
        control.finishWithSuccess();
        return;
    }
}
