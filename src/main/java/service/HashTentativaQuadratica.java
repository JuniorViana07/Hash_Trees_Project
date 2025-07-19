package service;

public class HashTentativaQuadratica<Key, Value> {
    private int N; // número de pares de chaves na tabela
    private int M = 512; // tamanho da tabela hash sondagem quadratica
    private Key[] keys; //keys
    private Value[] vals; //values

    public HashTentativaQuadratica() {
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }

    public HashTentativaQuadratica(int cap) {
        M = cap;
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private void resize(int cap) {
        HashTentativaQuadratica<Key, Value> t = new HashTentativaQuadratica<>(cap);
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                t.put(keys[i], vals[i]);
            }
        }
        keys = t.keys;
        vals = t.vals;
        M = t.M;
    }

    public void put(Key key, Value val) {
        if (N >= M / 2) resize(2 * M);

        int i = hash(key);
        int j = 1;

        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
            i = (i + j * j) % M;
            j++;
        }

        keys[i] = key;
        vals[i] = val;
        N++;
    }

    public Value get(Key key) {
        int i = hash(key);
        int j = 1;

        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                return vals[i];
            }
            i = (i + j * j) % M;
            j++;
        }
        return null;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }


    // as remoções nem são usadas na verdade pq o código serve mais pra inserção e busca, os registros não são removidos.
    public void delete(Key key) {
        if (!contains(key)) return;

        int i = hash(key);
        int j = 1;

        while (!key.equals(keys[i])) {
            i = (i + j * j) % M;
            j++;
        }

        keys[i] = null;
        vals[i] = null;
        N--;

        i = (i + j * j) % M;
        j = 1;

        while (keys[i] != null) {
            Key keyToRehash = keys[i];
            Value valToRehash = vals[i];
            keys[i] = null;
            vals[i] = null;
            N--;
            put(keyToRehash, valToRehash);
            i = (i + j * j) % M;
            j++;
        }

        if (N > 0 && N <= M / 8) resize(M / 2);
    }
}
