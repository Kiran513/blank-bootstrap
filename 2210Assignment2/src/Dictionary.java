
import java.util.Iterator;
import java.util.LinkedList;

public class Dictionary implements DictionaryADT {

    private LinkedList<ConfigData>[] dictionary;

    public Dictionary(int size){
        dictionary = new LinkedList[size];
    }


    /**
     * Uses Horner's Rule to polynomially hash the string s.
     *
     * @param s a string
     * @return an int representing the polynomial hash of s
     */
    private int hornersRule(String s){
        int length = s.length();
        int size = dictionary.length;

        int sum = ((int)s.charAt(length - 1)) % size;

        for(int i = length - 2; i >= 0; i--){
            sum = ((sum*37) + (int)s.charAt(i)) % size;
        }

        return sum;
    }


    /**
     * Hashes the string s.
     * Uses a modified polynomial hash for even distribution, decreasing collisions.
     *
     * @param s a string
     * @return an int representing the hash of s
     */
    private int hash(String s){

        int a = (int)s.charAt(0) + (int)s.charAt(s.length()/2);
        int b = (int)s.charAt(s.length()/4) * (int)s.charAt(s.length() - 1);

        return (a * hornersRule(s) + b) % dictionary.length;
    }


    /**
     * Inserts an element into the dictionary if it is not already present.
     *
     * @param pair a ConfigData type object, containing the string representation of the configuration
     * @return an int representing if a collision has (1) or hasn't (0) occurred
     * @throws DictionaryException if the current configuration is already in the dictionary
     */
    public int insert(ConfigData pair) throws DictionaryException {
        int key = hash(pair.getConfig());
        LinkedList<ConfigData> position = dictionary[key];

        // Checks to see if this position is empty,
        // if so creates a new linked list and inputs the pair.
        if(position == null){
            dictionary[key] = new LinkedList<>();
            dictionary[key].add(pair);

            return 0;
        }

        else{

            // Checks to see if this configuration is already located in the linked list.
            // If so, throws a DictionaryException, exiting the method.
            Iterator<ConfigData> keys = position.iterator();
            while(keys.hasNext()){
                if(keys.next().getConfig().equals(pair.getConfig())){
                    throw(new DictionaryException("Key " + pair.getConfig() + " is already in the dictionary."));
                }
            }

            // If we make it through every key without throwing an exception than the configuration is not in
            // the current dictionary, so we add it.
            position.add(pair);

            return 1;
        }

    }

    /**
     * Removes an element into from dictionary if it already present.
     *
     * @param config a string representing the configuration we wish to remove from the dictionary
     * @throws DictionaryException if the current configuration is not in the dictionary
     */
    public void remove(String config) throws DictionaryException {
        LinkedList<ConfigData> position = dictionary[hash(config)];

        if(position == null){
            throw(new DictionaryException("Key " + config + " is not in the dictionary."));
        }

        // Iterate through the keys checking if the configuration exists.
        // If it does, we remove it and return.
        Iterator<ConfigData> keys = position.iterator();
        while(keys.hasNext()){
            if(keys.next().getConfig().equals(config)){
                keys.remove();
                return;
            }
        }

        // If we iterate through every element and the configuration is not present we throw an exception.
        throw(new DictionaryException("Key " + config + " is not in the dictionary."));

    }

    /**
     * Determines the computer favorability score of the specified configuration.
     *
     * @param config a string representing the configuration we wish to determine the score of
     * @return an int representing the score of the specified configuration, or -1 if the current configuration is not present
     */
    public int find(String config) {
        LinkedList<ConfigData> position = dictionary[hash(config)];

        if(position == null){
            return -1;
        }

        // Iterate through the keys checking if the configuration exists.
        // If it does, we return it's score.
        Iterator<ConfigData> keys = position.iterator();
        while (keys.hasNext()) {
            ConfigData current = keys.next();
            if (current.getConfig().equals(config)) {
                return current.getScore();
            }
        }

        // If we iterate through every element and the configuration is not present so we return -1.
        return -1;
    }

}
