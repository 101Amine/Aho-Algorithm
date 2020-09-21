package Aho;

import java.util.ArrayList;
import java.util.List;

public class Tries {

    public static void main(String[] args) {
        List<String> setOfStrings = new ArrayList<>();
        setOfStrings.add("pqrs");
        setOfStrings.add("pprs");
        setOfStrings.add("psst");
        setOfStrings.add("ggrs");
        setOfStrings.add("pqrs");

        final Trie trie = new Trie();


        setOfStrings.forEach(trie::insert);
        System.out.println(trie.query("ggggg"));
        //System.out.println(trie.query("abc"));
        //System.out.println(trie.query("psst"));

        char b = 'b';
        System.out.println(b);


    }

}


class Trie {
    final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public int query(String s){
        TrieNode current = root;
        for(int i= 0; i< s.length(); i++) {
            if (current.trieNodes[s.charAt(i) -'a']==null) {
                return 0;
            }
            current.next(s.charAt(i));
        }
        return current.terminating;
    }


    public void insert(final String s){
        TrieNode current = root;
        System.out.println(current.terminating);
        for (int i=0 ; i<s.length(); i++){
            if(current.trieNodes[s.charAt(i) -'a']== null) {
                current.trieNodes[s.charAt(i) - 'a'] = new TrieNode();
            }
            current = current.next(s.charAt(i));
        }
        current.terminating=1;
    }


    public void delete(String s){
        TrieNode current = root;
        for(int i= 1; i< s.length(); i++) {
            if (current.trieNodes[s.charAt(i) -'a']==null)
                throw new RuntimeException("String doesn't exist");
            current.next(s.charAt(i));
        }
        current.terminating--;
    }

    public void update (final String old, final String updated){
        delete(old);
        insert(updated);
    }
}



class TrieNode {
    int terminating;
    final TrieNode[] trieNodes = new TrieNode[26];


    public TrieNode next(final char c){
        return trieNodes[c - 'a'];
    }

}
