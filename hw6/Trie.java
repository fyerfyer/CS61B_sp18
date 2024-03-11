public class Trie {
    private TrieNode root;

    // TrieNode
    private static class TrieNode {
        private TrieNode[] children;
        private boolean isEndOfWord;

        TrieNode() {
            children = new TrieNode[26]; // Assuming only lowercase English letters
            isEndOfWord = false;
        }
    }

    // constructor
    public Trie() {
        root = new TrieNode();
    }

    // insert into trie
    public void insert(String word) {
        // YOUR CODE HERE
        TrieNode node = root;
        for (int i = 0; i < word.length(); i += 1) {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }

            node = node.children[index];
        }
        node.isEndOfWord = true;
    }

    // search if a word is in trie
    public boolean search(String word) {
        // YOUR CODE HERE
        TrieNode node = root;
        for (int i = 0; i < word.length(); i += 1) {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) return false;

            node = node.children[index];
        }

        return node.isEndOfWord;
    }

    // find if there are any words start with prefix
    public boolean startsWith(String prefix) {
        // YOUR CODE HERE
        TrieNode node = root;
        for (int i = 0; i < prefix.length(); i += 1) {
            char ch = prefix.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) return false;
        }

        return true;
    }
}
