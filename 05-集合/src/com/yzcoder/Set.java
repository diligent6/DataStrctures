package com.yzcoder;

public interface Set<E> {
    int size();	//元素数量
    boolean isEmpty(); // 是否为空
    void clear(); // 清空集合
    boolean contains(E element); // 是否包含element元素
    void add(E element); // 添加element元素
    void remove(E element); // 删除element元素
    void traversal(Visitor<E> visitor); // 通过访问器遍历

    public static abstract class Visitor<E>{ // 访问器
        boolean stop;
        public abstract boolean visit(E element);
    }



}
