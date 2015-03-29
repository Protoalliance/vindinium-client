package com.protoalliance.vindiniumclient.bot.advanced.astar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class AStar {
    private static final int NO_HEURISTIC = -1;
    private static final boolean RECORD_NODES = true;
    private int start;
    private int end;
    private Graph aGraph;
    private NodeRecord startRecord;
    ArrayList<NodeRecord> openList;
    ArrayList<NodeRecord> closedList;
    Stack<NodeRecord> recordingStack;
    Heuristic hFunc;
    int numNodesVisited;
    long before;
    long after;


    public AStar(Graph aGraph, int start, int end,TileGraph t){
        this.aGraph = aGraph;
        this.hFunc = new Manhattan(end, t);
        this.numNodesVisited = 0;
        int numNodes = aGraph.getNumNodes();
        this.start = start;
        this.end = end;
        this.startRecord = new NodeRecord(start, null, 0, hFunc.estimate(start));
        openList = new ArrayList<NodeRecord>(numNodes);
        openList.add(startRecord);
        closedList = new ArrayList<NodeRecord>(numNodes);
        recordingStack = new Stack<NodeRecord>();
    }



    public Connection[] getPath(){
        before = calculateMemoryUsageBefore();
        NodeRecord cur = null;
        LinkedList<Connection> connections = null;
        double endNodeHeuristic = 0.0;
        NodeRecord endNodeRecord = null;
        NodeRecord closedRes = null;
        NodeRecord openRes = null;
        Connection curConnection = null;
        while(openList.size() > 0){
            cur = getSmallestElement();
            if(RECORD_NODES)
                recordingStack.push(cur);
            numNodesVisited++;
            //If smallest element is the goal stop
            if(cur.getNode() == end)
                break;
            connections = aGraph.getConnections(cur.getNode());
            if(connections == null){
                return null;
            }
            for(int i = 0; i < connections.size(); i++){
                curConnection = connections.get(i);
                int sink = curConnection.getSinkNode();
                double endNodeCost = cur.getCostSoFar() + curConnection.getWeight();
                closedRes = findRecord(sink, closedList);
                openRes = findRecord(sink, openList);
                if(closedRes != null){
                    //If we're here were on the closed list
                    if(closedRes.getCostSoFar() <= endNodeCost){
                        continue;
                    }else{
                        closedList.remove(cur);
                        //This gets us back the original heuristic
                        endNodeHeuristic = closedRes.getEstimatedCostSoFar() - closedRes.getCostSoFar();
                    }
                }else if(openRes != null){
                    //If we're here were on the open list
                    //If route is no better skip it
                    if(openRes.getCostSoFar() <=  endNodeCost)
                        continue;
                    //if route is better grab old heuristic
                    endNodeHeuristic = openRes.getEstimatedCostSoFar() - openRes.getCostSoFar();
                }else{
                    //If we're here we have an unvisited node
                    endNodeHeuristic = hFunc.estimate(sink);
                    endNodeRecord = new NodeRecord(sink);
                    endNodeRecord.setConnection(curConnection);
                    endNodeRecord.setCostSoFar(endNodeCost);
                    endNodeRecord.setEstimatedCostSoFar(endNodeCost + endNodeHeuristic);
                }

                //If openRes is null then the node wasn't on the openList
                //Add it to the list.
                if(openRes == null){
                    openList.add(endNodeRecord);
                }else{
                    //If we are here we need to update the node that is on the open list.
                    openRes.setConnection(curConnection);
                    openRes.setCostSoFar(endNodeCost);
                    openRes.setEstimatedCostSoFar(endNodeCost + endNodeHeuristic);
                }
            }
            openList.remove(cur);
            closedList.add(cur);
        }
        after = calculateMemoryUsageAfter();

        if(cur.getNode() != end){
            return null;
        }else{
            //build path back through nodes.

            Stack<Connection> path = new Stack<Connection>();
            while(cur.getNode() != start){
                path.push(cur.getConnection());
                NodeRecord val = findRecord(cur.getConnection().getSourceNode(), openList);
                NodeRecord val2 = findRecord(cur.getConnection().getSourceNode(), closedList);
                if(val == null && val2 == null){
                    System.out.println("error occurred");
                    return null;
                }else if(val != null){
                    cur = val;
                }else{
                    cur = val2;
                }
            }
            Connection[] retPath = new Connection[path.size()];
            int i = 0;
            while(!path.empty()){
                retPath[i++] = path.pop();
            }
            return retPath;
        }

    }


    private NodeRecord findRecord(int node, ArrayList<NodeRecord> list) {
        Iterator<NodeRecord> it = list.iterator();
        while(it.hasNext()){
            NodeRecord val = it.next();
            if(val.getNode() == node)
                return val;
        }
        return null;
    }

    public long getNumNodesVisited(){
        return numNodesVisited;
    }

    public long calculateMemoryUsageBefore(){
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        //long memory = runtime.totalMemory() - runtime.freeMemory();
        long m2 = Runtime.getRuntime().freeMemory();
        return m2;
    }

    public long calculateMemoryUsageAfter(){
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        //runtime.gc();
        // Calculate the used memory
        //long memory = runtime.totalMemory() - runtime.freeMemory();
        long m2 = Runtime.getRuntime().freeMemory();
        return m2;
    }

    public void setEndNode(int node){
        end = node;
    }

    public long getMemoryUsage(){
        return before - after;
    }

    public Stack<NodeRecord> getRecordStack(){
        return recordingStack;
    }

    public NodeRecord getSmallestElement(){
        Iterator<NodeRecord> it = openList.iterator();
        NodeRecord target = null;
        double smallest = Double.MAX_VALUE;
        while(it.hasNext()){
            NodeRecord val = it.next();
            if(val.getEstimatedCostSoFar() < smallest){
                smallest = val.getEstimatedCostSoFar();
                target = val;
            }
        }
        return target;
    }
}
