import java.util.ArrayList;
import java.util.List;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     *
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // read Boggle board file
        char[][] board = readBoardFromFile(boardFilePath);

        // build trie and cache the dictionary
        Trie trie = buildTrieFromDictionary(dictPath);

        // 使用 Trie 查找单词
        List<String> words = findWords(board, trie, k);

        return words;
    }

    // read Boggle board from file
    private static char[][] readBoardFromFile(String boardFilePath) {
        // YOUR CODE HERE
        In in = new In(boardFilePath);
        String[] lines = in.readAllLines();
        int rows = lines.length;
        int cols = lines[0].length();

        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = lines[i];
            for (int j = 0; j < cols; j++) {
                board[i][j] = line.charAt(j);
            }
        }

        return board;
    }

    // construct trie and cache dictionary file
    private static Trie buildTrieFromDictionary(String dictPath) {
        // YOUR CODE HERE
        In in = new In(dictPath);
        Trie trie = new Trie();
        while(!in.isEmpty()) {
            String word = in.readLine();
            trie.insert(word);
        }

        in.close;
        return trie;
    }

    // use Trie to search words in Boggle
    private static List<String> findWords(char[][] board, Trie trie, int k) {
        // YOUR CODE HERE
        List<String> words = new ArrayList<>();
        int N = board.length;
        int M = board[0].length;
        boolean[][] isVisit = new boolean[N][M];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < M; j += 1) {
                isVisit[i][j] = true;
                findWordsDFS(board, trie, i, j, "", isVisit, words);
                if (words.size() >= k) break;
            }
        }
        return words;
    }

    private static void findWordsDFS(char[][] board, Trie dictionary, int row, int col, String currentWord, boolean[][] visited, List<String> words, int k) {
        if(!validInput(row, col, board.length, board[0].length)) return;
        if (!visited[row][col]) {
            visited[row][col] = true;
            currentWord += board[row][col];
            if (currentWord.length() >= 3 && dictionary.search(currentWord)) {
                words.add(currentWord);
                return;
            } else if (!dictionary.startsWith(currentWord)) return;
            else if (words.size() >= k) return;
            else {
                findWordsDFS(board, dictionary, row + 1, col, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row, col + 1, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row - 1, col, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row, col - 1, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row + 1, col + 1, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row + 1, col - 1, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row - 1, col + 1, currentWord, visited, words, k);
                findWordsDFS(board, dictionary, row - 1, col - 1, currentWord, visited, words, k);
            }
            visited[row][col] = false;
        }

    }

    private static boolean validInput(int i, int j, int N, int M) {
        return i < N && i >= 0 && j < M && j >= 0;
    }
}
