import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V>{
    private K key;
    private V value;


    /**
     * конструктор
     */
    public Pair(K key, V value) {
        this.setKey(key);
        this.setValue(value);
    }

    /**
     * метод для получения ключа
     */
    public K getKey() {
        return key;
    }

    /**
     * метод для получения значения
     */
    public V getValue() {
        return value;
    }

    /**
     * метод для записи значения
     */
    public V setValue(V newValue) {
        value = newValue;
        return value;
    }

    /**
     * метод для записи ключа
     */
    public K setKey(K newKey) {
        key = newKey;
        return key;
    }

    @Override
    public String toString() {
        return "<" + this.key.toString() + ", " + this.value.toString() + ">";
    }
}


