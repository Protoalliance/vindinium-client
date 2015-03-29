package com.protoalliance.vindiniumclient.bot.advanced.astar;

import java.awt.*;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Matthew on 3/29/2015.
 */
public class TileGraph{
    private int width;
    private int height;
    private int divisor;
    private int[][] tileGrid;
    private int numNodes;
    private int numRows;
    private int numCols;

    public TileGraph(int width, int height, int divisor){
        this.width = width;
        this.height = height;
        this.divisor = divisor;
        numNodes = 0;
        if(width % divisor != 0 || width % divisor !=0){
            System.out.println("Board cannot be divided evenly!");
        }

        this.numRows = width/divisor;
        this.numCols = height/divisor;
        tileGrid = new int[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                tileGrid[i][j] = 0;
            }
        }
    }

    public void toggleGridPos(int x, int y){
        tileGrid[x][y] = 1;
    }


    public Graph genGraph(){
        Graph tileGraph = new Graph(divisor*divisor);
        for(int i = 0; i < width/divisor; i++){
            for(int j = 0; j < height/divisor; j++){
                if(tileGrid[i][j] == 0){
                    numNodes++;
                    Stack<Point> neighbors = getNeighbors(i,j);
                    int source = getVertexNumber(i,j);
                    Iterator<Point> it = neighbors.iterator();
                    while(it.hasNext()){
                        Point p = it.next();
                        int sink = getVertexNumber(p.x, p.y);
                        tileGraph.addConnection(source, sink, 1);
                    }
                }
            }
        }
        return tileGraph;
    }

    public int getNumNodes(){
        return numNodes;
    }


    public void printGraph(){
        System.out.println("Grid is ");
        for(int i = 0; i < width/divisor; i++){
            for(int j = 0; j < height/divisor; j++){
                System.out.print(tileGrid[i][j]);
            }
            System.out.println();
        }
    }

    public int getVertexNumber(int row, int col){
        if(row == 0){
            return col;
        }else{
            return col + (row * 10);
        }
    }

    public Point getRowCol(int vertNum){
        int row = 0;
        if(vertNum == 0)
            return new Point(0,0);
        //This might not be quite right
        if(vertNum >= numCols)
            row = vertNum / numRows;
        int col = vertNum % numCols;
        return new Point(row,col);
    }

    public Point localize(int vertNum){
        Point p = getRowCol(vertNum);
        Point retPt = new Point();
        retPt.x = (p.x * divisor) + divisor/2;
        retPt.y = (p.y * divisor) + divisor/2;
        return retPt;
    }

    //Returns col = x, row = y
    public Point quantize(int x, int y){
        int row = Math.floorDiv(x, divisor);
        int col = Math.floorDiv(y, divisor);
        return new Point(row, col);
    }


    public Stack<Point> getNeighbors(int x, int y){
        Stack<Point> pts = new Stack<Point>();
        int east = x - 1;
        int west = x + 1;
        int north = y - 1;
        int south = y + 1;

        if(east >= 0 && tileGrid[east][y] == 0 )
            pts.push(new Point(east,y));

        if(west <= ((width/divisor) - 1) && tileGrid[west][y] == 0)
            pts.push(new Point(west,y));

        if(south <= ((height/divisor - 1)) && tileGrid[x][south] == 0)
            pts.push(new Point(x, south));

        if(north >= 0 && tileGrid[x][north] == 0)
            pts.push(new Point(x, north));

        return pts;

    }


}
