package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    private class SearchNode {
        private WorldState state;
        private int moves;
        private SearchNode prev;

        public SearchNode(WorldState state, int moves, SearchNode prev) {
            this.state = state;
            this.moves = moves;
            this.prev = prev;
        }
    }

    private class NodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode p, SearchNode q) {
            int pPriority = p.moves + p.state.estimatedDistanceToGoal();
            int qPriority = q.moves + q.state.estimatedDistanceToGoal();
            return pPriority - qPriority;
        }
    }

    private List<WorldState> lst = new ArrayList<>();

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>(new NodeComparator());
        SearchNode cur = new SearchNode(initial, 0, null);
        pq.insert(cur);
        while (!pq.isEmpty()) {
            cur = pq.delMin();
            if (cur.state.isGoal()) break;
            for (WorldState wd : cur.state.neighbors()) {
                SearchNode node = new SearchNode(wd, cur.moves + 1, cur);
                if (cur.prev != null && wd.equals(cur.prev.state)) continue;
                pq.insert(node);
            }
        }

        Stack<WorldState> st = new Stack<>();
        SearchNode node = cur;
        while (node != null) {
            st.push(node.state);
            node = node.prev;
        }

        while (!st.isEmpty()) {
            lst.add(st.pop());
        }
    }

    public int moves() {
        return lst.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return lst;
    }
}
