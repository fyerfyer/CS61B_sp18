public class HuffmanDecoder {
    public static void main(String[] args) {
        //Read the Huffman coding trie.
        //If applicable, read the number of symbols.
        //Read the massive bit sequence corresponding to the original txt.
        String fileName = args[0];
        ObjectReader or = new ObjectReader(fileName);
        BinaryTrie trie = (BinaryTrie) or.readObject();
        int num = (int) or.readObject();
        BitSequence bitSequence = (BitSequence) or.readObject();

        //Repeat until there are no more symbols:
        //    4a: Perform a longest prefix match on the massive sequence.
        //    4b: Record the symbol in some data structure.
        //    4c: Create a new bit sequence containing the remaining unmatched bits.
        char[] res = new char[num];
        for(int i = 0; i < num; i += 1) {
            Match match = trie.longestPrefixMatch(bitSequence);
            res[i] = match.getSymbol();
            bitSequence = bitSequence.allButFirstNBits(match.getSequence().length());
        }

        FileUtils.writeCharArray(fileName, res);
    }
}
