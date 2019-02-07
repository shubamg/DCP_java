import java.util.*;

class Word_with_Count {
    String word;
    int count;

    public Word_with_Count(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String toString() {
        return String.format("\"%s\": %d", word, count);
    }
}

class Node {
    int num_words_ending = 0;
    Map<Character, Node> children = new HashMap<Character, Node>();

    public Node get_child(Character c) {
        Node temp = children.get(c);
        if (temp != null)
            return temp;

        temp = new Node();
        children.put(c, temp);
        return temp;
    }

    public void increment_count() {
        ++num_words_ending;
    }

    private List<Word_with_Count> get_all_matches(String prefix) {
        List<Word_with_Count> words_with_count = new LinkedList<>();

        if(num_words_ending != 0)
            words_with_count.add(new Word_with_Count(prefix, num_words_ending));

        for (Map.Entry<Character, Node> entry : children.entrySet())
            words_with_count.addAll(entry.getValue().get_all_matches(prefix+entry.getKey()));

        return words_with_count;
    }

    public List<Word_with_Count> find_matches(String prefix, int remaining_index) {
        if (remaining_index == prefix.length())
            return get_all_matches(prefix);

        Node child = children.get(prefix.charAt(remaining_index));
        if (child != null)
            return child.find_matches(prefix, remaining_index + 1);
        else
            return new LinkedList<Word_with_Count>();
    }
}

public class Trie {
    Node root = new Node();

    public void insert_word(String word) {
        Node curr_node = root;
        for(int index = 0; index < word.length(); index++)
            curr_node = curr_node.get_child(word.charAt(index));
        curr_node.increment_count();
    }

    public List<Word_with_Count> find_matches(String prefix) {
        return root.find_matches(prefix, 0);
    }

    public void print_matches(String p) {
        System.out.println("Prefix = \""+p+"\"\n"+find_matches(p)+
                "\n#####################################\n");
    }

    public static void main(String[] args) {
        List<String> words_to_insert = Arrays.asList(
                "abc", "abcd", "", "z", "z", "", "abe", "abc");
        Trie trie = new Trie();
        words_to_insert.stream().forEach(trie::insert_word);
        List<String> prefixes = Arrays.asList(
                "a", "", "ab", "d");
        prefixes.stream().forEach(trie::print_matches);
    }

}
