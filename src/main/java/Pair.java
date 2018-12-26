import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V>{
    private K key;
    private V value;


    public Pair(K key, V value) {
        this.setKey(key);
        this.setValue(value);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V newValue) {
        value = newValue;
        return value;
    }

    public K setKey(K newKey) {
        key = newKey;
        return key;
    }

    @Override
    public String toString() {
        return "<" + this.key.toString() + ", " + this.value.toString() + ">";
    }
}


