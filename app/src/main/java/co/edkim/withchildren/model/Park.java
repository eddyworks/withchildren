package co.edkim.withchildren.model;

/**
 * Created by Edward on 2014-08-24.
 */
public class Park {
    public String content;
    public String address;
    public String gu;
    public String dong;
    public String name;
    public String keyName;
    public int id;

    public Park(String _keyName, String _name, String _address, String _content, String _gu, String _dong, int _id) {
        id = _id;
        keyName = _keyName;
        name = _name;
        address = _address;
        content = _content;
        gu = _gu;
        dong = _dong;
    }
}
