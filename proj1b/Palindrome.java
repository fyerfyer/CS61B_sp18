public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deq = new LinkedListDeque<>();
        for (char c : word.toCharArray()) {
            deq.addLast(c);
        }
        return deq;
    }

    public boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        }

        char first = word.charAt(0);
        char last = word.charAt(word.length() - 1);
        if (first == last) {
            return isPalindrome(word.substring(1, word.length() - 1));
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() <= 1) {
            return true;
        }

        char first = word.charAt(0);
        char last = word.charAt(word.length()-1);
        if (cc.equalChars(first, last)) {
            return isPalindrome(word.substring(1, word.length() - 1), cc);
        } else {
            return false;
        }
    }
}
