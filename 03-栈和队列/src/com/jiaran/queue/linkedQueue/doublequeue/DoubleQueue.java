package com.jiaran.queue.linkedQueue.doublequeue;

import com.jiaran.queue.linkedQueue.doublequeue.DoubleCircleLinkedList;

/**
 * 双端队列 可以从两端进行入队和出队
 */
public class DoubleQueue<E> {

    private DoubleCircleLinkedList<E> linkedList =new DoubleCircleLinkedList<E>();


    public void enQueueRear(E element){
        linkedList.add(element);
    }
    public E deQueueFront(){
        return linkedList.remove(0);
    }
    public void enQueueFront(E element){
        linkedList.add(0,element);
    }
    public E deQueueRear(){
        return linkedList.remove(linkedList.size()-1);
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
