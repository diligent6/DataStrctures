package com.yzcoder;

public interface Map<K,V>{

    int size();

    boolean isEmpty();

    void clear();

    V put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    void traversal(Visitor<K, V> visitor);

    public static abstract class Visitor<K, V> {
        boolean stop;

        /**
         * 结果返回值决定了是否进行遍历，true 停止遍历 false 继续遍历
         * @param key
         * @param value
         * @return
         */
        public abstract boolean visit(K key, V value);
    }

}
