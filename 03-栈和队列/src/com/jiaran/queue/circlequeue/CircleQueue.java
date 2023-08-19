package com.jiaran.queue.circlequeue;

/**
 * 循环队列 基于动态数组实现
 */
public class CircleQueue<E> {
    private int size;
    private int front;
    private  E[] elements;

    private static final int DEFAULT_CAPACITY =10;

    public CircleQueue(int capacity){
        capacity = DEFAULT_CAPACITY > capacity ? DEFAULT_CAPACITY : capacity;
        elements = (E[]) new Object[capacity];
    }


        public void enQueue(E element){
        ensureCapacity(size+1);
            //1.元素入队
            elements[index(front+size)] =element;
            //2. 更新状态

            size++;
        }
    public E deQueue(){
        //1. 元素出队

        E element = elements[front];
        //2. 更新状态
        elements[front] =null;
        front = index(front + 1);
        size--;
        return element;
    }

    public void clear(){
        for (int i =0;i<size;i++){
            elements[i] = null;
        }
        size =0;
        front =0;
    }

    public E front(){
        return elements[front];
    }

    public boolean isEmpty(){
        return  size==0;
    };
public int size(){
    return size;
};









    private void ensureCapacity(int newCapacity){
        //1.获取当前容量
        int oldCapacity = elements.length;
        if (oldCapacity >=newCapacity) return;
        //2. 扩容
        newCapacity = newCapacity+ (newCapacity >>1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i =0;i<size;i++){
            newElements[i] = elements[i];
        }
        elements =newElements;
    }

    /**
     * 将front的索引 映射到循环队列里面的索引
     * @param index
     * @return
     */
    private int index(int index){
        int mapIndex = index % elements.length;
        return mapIndex;
    }
}
