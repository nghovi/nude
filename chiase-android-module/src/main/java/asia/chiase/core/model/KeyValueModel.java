package asia.chiase.core.model;

/**
 * Created by PC on 9/19/2014.
 */
public class KeyValueModel {

    private String key;

    private String value;

    public KeyValueModel(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
