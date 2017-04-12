

public class Achi {

    private char[][] gameBoard;

    /**
     * Builds an empty game board of size board_size x board_size.
     *
     * @param board_size the length and width of the board
     * @param max_levels the recursion limit on the computer AI
     */
    public Achi (int board_size, int max_levels){

        gameBoard = new char[board_size][board_size];
        for(int i = 0; i < board_size; i++){
            for(int j = 0; j < board_size; j++){
                gameBoard[i][j] = ' ';
            }
        }
    }


    /**
     * Creates a string representation of the board.
     * Will be n^2 characters long, n being the width of the board.
     * Consists of X's (human), O's (computer) and spaces (empty).
     *
     * @return a string representing the state of the board.
     */
    private String getBoard(){
        String curBoard = "";
        for(int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                curBoard += gameBoard[i][j];
            }
        }
        return curBoard;
    }


    /**
     * Creates a new Dictionary object.
     * Uses the prime 9973 as the dictionary size.
     *
     * @return an empty Dictionary object.
     */
    public Dictionary createDictionary(){
        return new Dictionary(9973);
    }


    /**
     * Determines if the current boar configuration has already been considered.
     *
     * @param configurations a dictionary containing all of the previous seen configurations
     * @return boolean representing if the current configuration is in the dictionary
     */
    public int repeatedConfig(Dictionary configurations){
        return configurations.find(getBoard());
    }


    /**
     * Inserts a new ConfigData object into the dictionary if the configuration is not already present.
     *
     * @param configurations a dictionary containing all of the previous seen configurations
     * @param score the computer favorability score of the current configuration
     */
    public void insertConfig(Dictionary configurations, int score){
        ConfigData newConfig = new ConfigData(getBoard(), score);

        try {
            configurations.insert(newConfig);
        } catch (DictionaryException e) {
            //e.printStackTrace();
        }
    }


    /**
     * Updates the gameBoard with the current move.
     *
     * @param row the y position of the current move
     * @param col the x position of the current move
     * @param symbol the type of move that was made
     */
    public void storePlay(int row, int col, char symbol){
        gameBoard[row][col] = symbol;
    }

    /**
     * Determines if the specified position is empty.
     *
     * @param row the y position of the current move
     * @param col the x position of the current move
     * @return a boolean indicating if the position is empty
     */
    public boolean tileIsEmpty (int row, int col){
        return gameBoard[row][col] == ' ';
    }


    /**
     * Determines if the specified position is 'O'.
     *
     * @param row the y position of the current move
     * @param col the x position of the current move
     * @return a boolean indicating if the position is 'O'
     */
    public boolean tileIsComputer (int row, int col){
        return gameBoard[row][col] == 'O';
    }


    /**
     * Determines if the specified position is 'X'.
     *
     * @param row the y position of the current move
     * @param col the x position of the current move
     * @return a boolean indicating if the position is 'X'
     */
    public boolean tileIsHuman (int row, int col){
        return gameBoard[row][col] == 'X';
    }


    /**
     * Checks every row to determine if a win has occurred.
     *
     * @param c the character we are checking for a win
     * @return a boolean representing if an entire row consists entirely of char c
     */
    private boolean checkHorizontal(char c){

        int length = gameBoard.length;

        for(int i = 0; i < length; i++) {
            int num = 0;

            // Fix row while we iterate through columns, therein checking each row.
            for (int j = 0; j < length; j++) {
                if(gameBoard[i][j] != c){
                    break;
                }
                num++;
            }

            // Determine when we broke out of the loop (did we make it to the end?)
            if(num == length){
                return true;
            }
        }

        return false;
    }

    /**
     * Checks every column to determine if a win has occurred.
     *
     * @param c the character we are checking for a win
     * @return a boolean representing if an entire column consists entirely of char c
     */
    private boolean checkVertical(char c){

        int length = gameBoard.length;

        for(int j = 0; j < length; j++) {
            int num = 0;

            // Fix column while we iterate through rows, therein checking each column.
            for (int i = 0; i < length; i++) {
                if(gameBoard[i][j] != c){
                    break;
                }
                num++;
            }

            // Determine when we broke out of the loop (did we make it to the end?)
            if(num == length){
                return true;
            }
        }

        return false;
    }

    /**
     * Checks both diagonals to determine if a win has occurred.
     *
     * @param c the character we are checking for a win
     * @return a boolean representing if either diagonal consists entirely of char c
     */
    private boolean checkDiagonal(char c){

        int length = gameBoard.length;

        int count1 = 0;
        int count2 = 0;

        int j =length - 1;

        for (int i = 0; i < length; i++) {

            // Checks top-left to bottom-right
            if(gameBoard[i][i] == c){
                count1++;
            }

            // Checks top-right to bottom-left
            if(gameBoard[i][j--] == c){
                count2++;
            }
        }

        // If either were found to be c length times, then we know that at least one diagonal is entirely char c
        return(count1 == length || count2 == length);
    }

    /**
     * Determines if the player represented by symbol has won.
     * Checks the three possible win conditions:
     * winning in a vertical, horizontal, or diagonal line.
     *
     * @param symbol a char representing the player we are checking to have won.
     * @return a boolean representing if the player represented by symbol has won.
     */
    public boolean wins (char symbol){
        return(checkDiagonal(symbol) || checkHorizontal(symbol) || checkVertical(symbol));
    }

    /**
     * Determines if the next move will be a draw.
     * This can only occur when there is one space left, and none of the next players piece adjacent to that space.
     *
     * @param symbol representing the next player who will move
     * @return a boolean representing if the next player cannot move
     */
    public boolean isDraw(char symbol){

        // Need to determine if there is only one space left.
        int rowPos = -1;
        int colPos = -1;
        boolean found = false;

        for(int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if(gameBoard[i][j] == ' '){

                    // If we have found an empty space before, then there must be greater than 1 empty spaces,
                    // therefore the next move cannot be a draw.
                    if(found){
                        return false;
                    }
                    rowPos = i;
                    colPos = j;
                    found = true;
                }
            }
        }

        // Check every adjacent spot, if any spot is symbol then there is not a draw as the next player can move.
        for(int i = rowPos - 1; i < rowPos + 2; i++){
            for(int j = colPos - 1; j < colPos + 2; j++) {
                try{
                    if(gameBoard[i][j] == symbol){
                        return false;
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    continue;
                }
            }
        }

        return true;
    }


    /**
     * Determines the computer favorability score of the current board.
     * 3 = computer wins, 2 = draw, 1 = undecided, 0 = human wins.
     *
     * @param symbol representing the next player who will move
     * @return an int representing the computer favorability score
     */
    public int evalBoard(char symbol){

        if(wins('O')){
            return 3;
        }
        else if(wins('X')){
            return 0;
        }
        else if(isDraw(symbol)){
            return 2;
        }
        else{
            return 1;
        }
    }
}

