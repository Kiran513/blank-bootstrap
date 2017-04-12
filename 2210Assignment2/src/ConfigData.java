/**
 * Represents a board configuration.
 * Links a concatenated string, config, representing the board state,
 * to an int, score, representing the computers favorability for winning.
 */
public class ConfigData {

    private String config;
    private int score;

    public ConfigData(String config, int score){
        this.config = config;
        this.score = score;
    }

    public String getConfig(){
        return config;
    }

    public int getScore(){
        return score;
    }
}
