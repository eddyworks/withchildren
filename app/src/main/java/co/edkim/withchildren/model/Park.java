package co.edkim.withchildren.model;

/**
 * Created by Edward on 2014-08-24.
 */
public class Park {
    public String content;
    public String address;
    public String name;
    public String keyName;

    public Park(String _keyName, String _name, String _address, String _content) {
        keyName = _keyName;
        name = _name;
        address = _address;
        content = _content;
    }
}
