package com.jiaran.queue.linkedQueue.singlequeue;

/**
 * 基于链表实现，因为要对头尾进行频繁操作 使用双向链表实现效率更高
 * 先进先出 队尾入队，队头出队
 */
public class SingleQueue<E> {
    private DoubleCircleLinkedList<E> linkedList =new DoubleCircleLinkedList<E>();


    public void enQueue(E element){
        linkedList.add(element);
    }
    public E deQueue(){
        return linkedList.remove(0);
    }

    public void clear(){
        linkedList.clear();
    }

    public E front(){
        return linkedList.get(0);
    }

    public boolean isEmpty(){
        return linkedList.isEmpty();
    };
    public int size(){
        return linkedList.size();
    };


}

//
//    public void enQueue(E element){}
//    public E deQueue(){}
//
//    public void clear(){}
//
//    public E front(){}
//
//    public boolean isEmpty(){};
//public int size(){};