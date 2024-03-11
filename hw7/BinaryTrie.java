import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private Node root;

    private static class Node {
        private char character;
        private int frequency;
        private Node left;
        private Node right;

        private Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        root = buildTrie(frequencyTable);
    }

    private Node buildTrie(Map<Character, Integer> table) {
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);
        for (Map.Entry<Character, Integer> entry : table.entrySet()) {
            Node node = new Node(entry.getKey(), entry.getValue());
            pq.add(node);
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parant = new Node('\0', left.frequency + right.frequency);
            parant.left = left;
            parant.right = right;
            pq.add(parant);
        }

        return pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node cur = root;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < querySequence.length(); i += 1) {
            int bit = querySequence.bitAt(i);
            if (bit == 0) {
                if (cur.left == null) break;
                cur = cur.left;
                sb.append('0');
            }
            else {
                if (cur.right == null)  break;
                cur = cur.right;
                sb.append('1');
            }
        }

        return new Match(new BitSequence(sb.toString()), cur.character);
    }

    private boolean isLeaf(Node node) {
        return node.right == null && node.left == null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> table = new HashMap<>();
        buildLookupTableHelper(root, "", table);
        return table;
    }

    private void buildLookupTableHelper(Node node, String s, Map<Character, BitSequence> table) {
        if (isLeaf(node)) {
            table.put(node.character, new BitSequence(s));
        }
        if (node.left != null) buildLookupTableHelper(node.left, s + '0', table);
        if (node.right != null) buildLookupTableHelper(node.right, s + '1', table);
    }
}