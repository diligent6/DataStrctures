package com.yzcoder.listSet;

import com.yzcoder.Set;
import com.yzcoder.listSet.doubleLinkedList.DoubleLinkedList;

/**
 * 基于LinkedList 链表实现集合
 * @param <E>
 */
public class ListSet <E> implements Set<E> {
    private DoubleLinkedList<E> linkedList = new DoubleLinkedList<>();
    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public boolean isEmpty() {
        return linkedList.isEmpty();
    }

    @Override
    public void clear() {
        linkedList.clear();
    }

    @Override
    public boolean contains(E element) {
        return linkedList.contains(element);
    }

    /**
     * 添加：添加的元素不能重复，如果重复就覆盖
     * @param element
     */
    @Override
    public void add(E element) {
        //判断元素是否存在
        int index = linkedList.indexOf(element);
        if (index == linkedList.ELEMENT_NOT_FOUND){//元素不存在就添加
            linkedList.add(element);
        }else{
            linkedList.set(index,element);//元素存在就进行覆盖
        }
    }

    @Override
    public void remove(E element) {
        int index = linkedList.indexOf(element);
        if (index != linkedList.ELEMENT_NOT_FOUND){
            linkedList.remove(index);
        }
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        for (int i =0;i<linkedList.size();i++){
            visitor.visit(linkedList.get(i));
        }
    }
}
