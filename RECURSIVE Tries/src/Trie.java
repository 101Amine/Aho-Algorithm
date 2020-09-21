public class Trie {
    char c;
    Trie[] child;
    boolean word;

    public Trie() {
    }

    public void add(String s) {
        if (s.isEmpty()){
            this.word = true;
            return;
        }

        // extract the first character in s
        char letter = s.charAt(0);

        //   'a' value of 97, lets suppose letter = b so 'a' - 'b' b=98 => what means 98 - 97 = 1
        int index = letter - 'a';

        if (this.child[index] == null){
            this.child[index] = new Trie();
        }

        // substring delete the prefix of the String with the number specified, 1 = 1 prefix word
        this.child[index].add(s.substring(1));
    }


    public boolean isWord (String s){
        if (s.isEmpty()){
            return this.word;
        }

        char letter = s.charAt(0);
        int index = letter - 'a';

        if(this.child[index]== null){
            return false;
        }

        return this.child[index].isWord(s.substring(1));
    }
}


