import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class UI {

    public static void main(String[] args) {

        OrderedDictionary dictionary = new OrderedDictionary();

        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));

            String line = in.readLine();
            String line2 = in.readLine();


            // Read in and build dictionary from the file.
            while (line != null){

                Key newKey;
                if (line2.endsWith(".gif") || line2.endsWith(".jpg")){
                    newKey = new Key(line.toLowerCase(), 3);
                }
                else if (line2.endsWith(".wav") || line2.endsWith(".mid")){
                    newKey = new Key(line.toLowerCase(), 2);
                }
                else{
                    newKey = new Key(line.toLowerCase(), 1);
                }

                try {
                    dictionary.insert(new Record(newKey, line2));
                } catch (DictionaryException e) {
                    System.out.println("> Duplicate key found, please check your original file. Key: " + newKey.getWord());
                }

                line = in.readLine();
                line2 = in.readLine();
            }

            String command = "";

            // Creating a hashmap to quickly identify which commands are inputted.
            HashMap<String, Integer> commandDeterminant = new HashMap<>();
            commandDeterminant.put("search", 1);
            commandDeterminant.put("remove", 2);
            commandDeterminant.put("insert", 3);
            commandDeterminant.put("next", 4);
            commandDeterminant.put("prev", 5);
            commandDeterminant.put("first", 6);
            commandDeterminant.put("last", 7);
            commandDeterminant.put("end", 8);

            // Initializing audio player.
            SoundPlayer player = new SoundPlayer();

            // We start the main loop containing all of the functionality of the UI. Loops exists when the command end is entered.
            while(!command.equals("end")){

                try {

                    StringReader keyboard = new StringReader();
                    StringTokenizer input = new StringTokenizer(keyboard.read("\nEnter next command: "));
                    command = input.nextToken().toLowerCase();

                    // We determine what command was entered.
                    int current = commandDeterminant.get(command);

                    switch(current){

                        // SEARCH
                        case 1:

                            int count = 0;
                            try {
                                String word = input.nextToken();

                                // Need to iterate through each type to see if a word with such a type exists in the dictionary.
                                for (int i = 1; i < 4; i++) {

                                    // We try to find the key in the dictionary.
                                    Key search = new Key(word, i);
                                    Record found = dictionary.find(search);

                                    // If we are returned a leaf, the key is not in the dictionary, we move on to the next type.
                                    if (found == null) {
                                        continue;
                                    }

                                    count++;

                                    // Text file
                                    if (i == 1) {
                                        System.out.println("> Type text: " + found.getData());
                                    }

                                    // Audio file
                                    else if (i == 2) {
                                        System.out.print("> Type audio: playing audio. \n> ");
                                        try {
                                            player.play(found.getData());
                                        } catch (MultimediaException e) {
                                            System.out.println("> Error playing audio file.");
                                        }
                                    }

                                    // Image file
                                    else {
                                        System.out.println("> Type image: displaying image.");
                                        try {
                                            PictureViewer viewer = new PictureViewer();
                                            viewer.show(found.getData());
                                        } catch (MultimediaException e) {
                                            System.out.println("> Error displaying image.");
                                        }
                                    }
                                }

                                // Use count to determine if there were no elements with the specified input word.
                                if (count == 0) {
                                    System.out.println("> Element does not exist in dictionary.");
                                }
                            }

                            // Will be called by the StringTokenizer if there is only one word given as input.
                            catch (NoSuchElementException e) {
                                System.out.println("> Insufficient arguments, must be of form: search word.");
                            }
                            break;

                        // REMOVE
                        case 2:
                            try {

                                // Tries to build a key from the input string and remove the key from the dictionary.
                                Key rmv = new Key(input.nextToken(), (Integer.parseInt(input.nextToken())));
                                dictionary.remove(rmv);
                                System.out.println("> Element removed.");
                            }

                            catch (NoSuchElementException e){
                                System.out.println("> Insufficient arguments, must be of form: remove word type.");
                            }
                            catch (NumberFormatException e){
                                System.out.println("> Invalid arguments.");
                            }
                            catch (DictionaryException e){
                                System.out.println("> Element does not exist in dictionary.");
                            }
                            break;

                        // INSERT
                        case 3:
                            try {

                                // Tries to build a record from the input string and insert the record into the dictionary.
                                String word = input.nextToken();
                                String type = input.nextToken();
                                String data = "";
                                while (input.hasMoreTokens()){
                                    data += input.nextToken() + " ";
                                }
                                Record ins = new Record(new Key(word, (Integer.parseInt(type))), data);
                                dictionary.insert(ins);
                                System.out.println("> Element inserted.");
                            }
                            catch (NoSuchElementException e){
                                System.out.println("> Insufficient arguments.");
                            }
                            catch (NumberFormatException e){
                                System.out.println("> Invalid arguments, must be of form: insert word type data.");
                            }
                            catch (DictionaryException e){
                                System.out.println("> Element already exists in dictionary.");
                            }
                            break;

                        // NEXT
                        case 4:
                            try{

                                // Attempts to find the successor of a constructed Key from the input string.
                                Record next = dictionary.successor(new Key(input.nextToken(), (Integer.parseInt(input.nextToken()))));

                                // If the successor function returns null than we know that the inputted key is greater than any in the dictionary.
                                if (next == null){
                                    System.out.println("> A larger key does not exist in the dictionary.");
                                }
                                else {
                                    System.out.println("> Next key: (" + next.getKey().getWord() + ", " + next.getKey().getType() + ")");
                                }
                            }
                            catch (NoSuchElementException e){
                                System.out.println("> Insufficient arguments, must be of form: next word type.");
                            }
                            catch (NumberFormatException e){
                                System.out.println("> Invalid arguments.");
                            }

                            break;

                        // PREV
                        case 5:
                            try{

                                // Attempts to find the predecessor of a constructed Key from the input string.
                                Record prev = dictionary.predecessor(new Key(input.nextToken(), (Integer.parseInt(input.nextToken()))));

                                // If the predecessor function returns null than we know that the inputted key is less than any in the dictionary.
                                if (prev == null){
                                    System.out.println("> A smaller key does not exist in the dictionary.");
                                }
                                else {
                                    System.out.println("> Previous key: (" + prev.getKey().getWord() + ", " + prev.getKey().getType() + ")");
                                }
                            }
                            catch (NoSuchElementException e){
                                System.out.println("> Insufficient arguments.");
                            }
                            catch (NumberFormatException e){
                                System.out.println("> Invalid arguments, must be of form: prev word type");
                            }

                            break;

                        // FIRST
                        case 6:
                            try {

                                // Attempts to find the smallest key in the dictionary (which is always possible unless the dictionary is empty).
                                Key smallest = dictionary.smallest().getKey();
                                System.out.println("> Smallest: (" + smallest.getWord() + ", " + smallest.getType() + ")");
                            }
                            catch (NullPointerException e){
                                System.out.println("> The dictionary is empty.");
                            }
                            break;

                        // LAST
                        case 7:
                            try {
                                // Attempts to find the largest key in the dictionary (which is always possible unless the dictionary is empty).
                                Key largest = dictionary.largest().getKey();
                                System.out.println("> Largest: (" + largest.getWord() + ", " + largest.getType() + ")");
                            }
                            catch (NullPointerException e){
                                System.out.println("> The dictionary is empty.");
                            }
                            break;

                        // END
                        case 8:
                            System.out.println("> Goodbye.");
                            break;
                    }

                }

                // If an invalid command was entered, the HashMap will throw a NullPointerException, we deal with that here.
                // We catch a NoSuchElementException if an empty string was entered.
                catch (NullPointerException | NoSuchElementException f){
                    System.out.println("> Please enter a valid command: search, remove, insert, next, prev, first, last, end.");
                }

            }
        }

        // If the inputted file cannot be opened we deal with that here and the program exits (since the file is given as a program argument).
        catch (IOException e){
            System.out.println("> Trouble opening file, please ensure the file exists and the path is correct.");
        }

        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("> Insufficient arguments.");
        }
    }
}




