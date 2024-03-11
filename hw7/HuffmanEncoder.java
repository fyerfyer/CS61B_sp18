import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> table = new HashMap<>();
        for (char ch : inputSymbols) {
            if (!table.containsKey(ch)) {
                table.put(ch, 1);
            } else {
                int time = table.get(ch);
                table.put(ch, time + 1);
            }
        }

        return table;
    }

    public static void main(String[] args) {
        // Read the file as 8 bit symbols.
        String fileName = args[0];
        char[] symbols = FileUtils.readFile(fileName);

        //Build frequency table.
        //Use frequency table to construct a binary decoding trie.
        Map<Character, Integer> frequencyTable = buildFrequencyTable(symbols);
        BinaryTrie trie = new BinaryTrie(frequencyTable);

        //Write the binary decoding trie to the .huf file
        String hufFileName = fileName + ".huf";
        ObjectWriter ow = new ObjectWriter(hufFileName);
        ow.writeObject(trie);
        ow.writeObject(symbols.length);

        //Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> table = trie.buildLookupTable();

        //Create a list of bit sequences.
        List<BitSequence> bitSequences= new ArrayList<>();

        //For each 8 bit symbol:
        //Lookup that symbol in the lookup table.
        //Add the appropriate bit sequence to the list of bit sequences.
        for (char c : symbols) {
            BitSequence bit = table.get(c);
            bitSequences.add(bit);
        }

        //Write the huge bit sequence to the .huf file.
        ow.writeObject(BitSequence.assemble(bitSequences));
    }
}
