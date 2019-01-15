import java.util.*;

public class OpenTable<K, V> implements Map<K, V>, Iterable<Pair> {
    private int mod;          // счетчик для итератора
    private int numCells = 3; // кол-во ячеек
    private int size = 0;     // размер таблицы
    private Pair<K, V>[] table; // массив пар

    /**
     * конструктор
     */
    OpenTable() {
        table = new Pair[numCells];
        mod = 0;
    }

    /**
     * метод для вычисления хэш-кода
     */
    private int hashFunction(Object key) {
        return Math.abs(key.hashCode() % table.length);
    }

    /**
     * метод для добавления пар ключ значение в таблицу
     */
    @Override
    public V put(K key, V value) {
        int hash = hashFunction(key);
        V old = null;

        if (value == null) {
            throw new NullPointerException();
        }

        // если в таблице уже существует такой ключ, то даем ему нове значение
        if (keySet().contains(key)) {
            // выполнится только при совпадении ключей и таким образом мы нашли индекс ячейки с ключом или поняли,
            //что его нет
            while ((table[hash] == null || !table[hash].getKey().equals(key)) && ++hash < table.length) ;
            if (hash == table.length) {
                rehash();
                return put(key, value);
            } else {
                old = table[hash].getValue();
                table[hash] = new Pair<>(key, value);
                size++;
            }
        } else { // если ключа нет
            //и ячейка таблицы пустая, записываем новую пару ключ значенение
            if (table[hash] == null) {
                table[hash] = new Pair<>(key, value);
                size++;
            } else {// иначе ищем пустую ячейку
                while (hash < table.length && table[hash] != null)
                    hash++;
                // если таблица заполнена, то увеличиваем ее размер
                if (hash == table.length) {
                    rehash();
                    return put(key, value);
                } else { // иначе записываем в пустую ячейку
                    table[hash] = new Pair<>(key, value);
                    size++;
                }
            }
        }
        // если таблица заполнена на кф заполнения(0,75), увеличиваем размер
        if (size == new Double(numCells * 0.75).intValue())
            rehash();
        mod++;
        return old;
    }

    /**
     * метод для увеличения размера таблицы
     */
    protected void rehash() {
        Map<K, V> map = toMap();
        numCells = (table.length << 1) + 1; // увеличение числа ячеек
        table = new Pair[numCells]; // создаем новую таблицу с соотв числом ячеек
        putAll(map);  // переносим все значения из старой таблицы в новую
        size = map.size();
        mod++;
    }

    private Map<K, V> toMap() {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : table) {
            if (pair != null)
                map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    /**
     * метод для поиска значения по ключу
     */
    @Override
    public V get(Object key) {
        V value = null;
        int hash = hashFunction(key);
        if (keySet().contains(key)) { // если существует такой ключ, то находим и возвращаем его значение
            int i = hash;
            while (table[i] == null || !table[i].getKey().equals(key)) // поиск индекса ячейки с ключем
                i++;
            value = table[i].getValue();
        }
        return value;
    }

    /**
     * метод для удаления значения по его ключу
     */
    @Override
    public V remove(Object key) {
        int hash = hashFunction(key);
        if (keySet().contains(key)) { // если существует такой ключ, то находим пару и заменяем ее на null
            int i = hash;
            while (table[i] == null || !table[i].getKey().equals(key)) // поиск индекса ячейки с ключем
                i++;
            Pair<K, V> old = table[i];
            table[i] = null;  // замена пары на null
            mod++;
            size--;
            return old.getValue();
        }
        return null;
    }

    /**
     * метод для очистки таблицы
     */
    public void clear() {
        size = 0;
        table = new Pair[numCells]; // создаем новую
    }

    /**
     * метод для проверки наличия ключа в таблие
     */
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    /**
     * метод для проверки наличия значения в таблице
     */
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    /**
     * метод для получения набора <ключ,значение>
     */
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new HashSet<>();
        for (Pair<K, V> pair : table) {
            if (pair != null)
                set.add(new Pair<>(pair.getKey(), pair.getValue()));
        }
        return set;
    }

    /**
     * метод для проверки наличия пар в таблице
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * метод для получения количества пар в таблице
     */
    public int size() {
        return this.size;
    }

    /**
     * метод для копирования всех пар с указанной мапы на данную
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * метод для получения коллекции ключей
     */
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> pair : entrySet())
            keys.add(pair.getKey());
        return keys;
    }

    /**
     * метод для получения коллекции значений
     */
    public Collection<V> values() {
        Set<V> values = new HashSet<>();
        for (Map.Entry<K, V> pair : entrySet())
            values.add(pair.getValue());
        return values;
    }

    /**
     * метод для печати таблицы
     */
    public void print() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numCells; i++) {
            if (table[i] != null) {
                result.append("[").append(hashFunction(table[i].getKey())).append("] ");
                result.append("[").append(i).append("] ");
                result.append(table[i].toString()).append("  \n");
            }
        }
        System.out.println(result);

    }


    private class Iteration<T> implements Iterator<T> {
        int i = 0, mod;
        T current;

        Iteration() {
            this.mod = OpenTable.this.mod;
        }

        @Override
        public boolean hasNext() {
            if (i == OpenTable.this.table.length)
                return false;
            do {
                current = (T) OpenTable.this.table[i];
            } while (++i < OpenTable.this.numCells && current == null);
            return current != null;
        }

        @Override
        public T next() {
            try {
                if (mod != OpenTable.this.mod)
                    throw new ConcurrentModificationException();
            } catch (ConcurrentModificationException e) {
                System.err.println("err 1 : вызов недопустимого метода в цикле");
                System.exit(1);
            }

            return current;
        }

        @Override
        public void remove() {
            try {
                if (mod != OpenTable.this.mod)
                    throw new ConcurrentModificationException();
            } catch (ConcurrentModificationException e) {
                System.err.println("err 1 : вызов недопустимого метода в цикле");
                System.exit(1);
            }
            OpenTable.this.remove(((Pair) current).getKey());
            current = null;
            mod++;
        }
    }

    public Iteration iterator() {
        return new Iteration<Pair<K, V>>();
    }

}
