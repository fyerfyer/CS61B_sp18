package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q =new LinkedList<>();
        q.add(s);
        marked[s] = true;

        while (!q.isEmpty()) {
            int top = q.poll();
            for (int w : maze.adj(top)) {

                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = top;
                    distTo[w] = distTo[top] + 1;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        q.add(w);
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}
