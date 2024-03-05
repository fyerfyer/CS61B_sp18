package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private int targetX;
    private int targetY;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        this.targetX = targetX;
        this.targetY = targetY;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    private class Node {
        private int v;
        private int priority;

        public Node(int v) {
            this.v = v;
            this.priority = distTo[v] + h(v);
        }
    }

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node p, Node q) {
            return p.priority - q.priority;
        }
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int dx = maze.toX(v);
        int dy = maze.toY(v);
        return Math.abs(dx - targetX) + Math.abs(dy - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // TODO
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());
        Node curNode = new Node(s);
        marked[s] = true;
        pq.add(curNode);

        while (!pq.isEmpty()) {
            Node top = pq.poll();
            for (int w : maze.adj(top.v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = top.v;
                    distTo[w] = distTo[top.v] + 1;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        pq.add(new Node(w));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

