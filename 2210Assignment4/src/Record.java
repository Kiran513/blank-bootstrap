/**
 * An object to store a Key and string value pair.
 */
public class Record {

    private Key key;
    private String data;

    public Record(Key key, String data) {
        this.key = key;
        this.data = data;
    }

    public Key getKey() {
        return key;
    }

    public String getData() {
        return data;
    }
}
