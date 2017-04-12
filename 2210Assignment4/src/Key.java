
/**
 * An object to store a string and int value pair.
 */
public class Key {

    private String word;
    private int type;

    public Key (String word, int type){
        this.word = word;
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public int getType() {
        return type;
    }

    /**
     * Compares two key type objects based firstly on their respective "word" values (lexicographically),
     * and then based on their int type values.
     *
     * @param k the key object against which this key object is being compared
     * @return -1 if (other key) > (this key); 0 if (other key) = (this key); 1 if (other key) < (this key)
     */
    public int compareTo(Key k){

        // First comparison will be lexicographical on the String "word" variable.
        int wordCompare = k.getWord().compareTo(word);

        // If the words are equal we look at the int "type" variable.
        if (wordCompare == 0){

            if (k.getType() == type) {
                return 0;
            }
            if (k.getType() < type){
                return 1;
            }
            else{
                return -1;
            }
        }

        if (wordCompare > 0){
            return 1;
        }
        else{
            return -1;
        }
    }

}
