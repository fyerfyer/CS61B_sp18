package lab11.graphs;

import java.util.Random;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] pathTo;
    private boolean isCircle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        pathTo = new int[maze.V()];
        Random rand = new Random();
        int startX = rand.nextInt(maze.N());
        int startY = rand.nextInt(maze.N());
        int s = maze.xyTo1D(startX, startY);
        pathTo[s] = s;
        dfs(s);
        announce();
    }

    private void dfs(int v) {
        marked[v] = true;
        for (int w : maze.adj(v)) {
            if (isCircle) {
                return;
            }

            if (!marked[w]) {
                pathTo[w] = v;
                dfs(w);
            } else if (w != pathTo[v]) {
                //u is already visited and u is not parent of v.
                isCircle = true;
                pathTo[w] = v;//connect the cycle.

                int cur = v;
                while (cur != w) {
                    edgeTo[cur] = pathTo[cur];
                    cur = pathTo[cur];
                }
                return;
            }
        }
    }

    // Helper methods go here
}

