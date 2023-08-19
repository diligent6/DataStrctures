package com.jiaran.queue.circlequeue;

public class DoubleQueue<E> {

    private int size;
    private int front;
    private  E[] elements;

    private static final int DEFAULT_CAPACITY =10;

    public DoubleQueue(int capacity){
        capacity = DEFAULT_CAPACITY > capacity ? DEFAULT_CAPACITY : capacity;
        elements = (E[]) new Object[capacity];
    }


    /**
     * 入队，从队尾入队
     * @param element
     */
    public void enQueueRear(E element){
        ensureCapacity(size+1);
        elements[index(front + size)] = element;
        size++;
    }

    /**
     * 出队，从队头出队
     * @return
     */
    public E deQueueFront(){
        E element  = elements[front];
        front = index(front +1);
        size--;
        return element;
    }
    /**
     * 入队，从队头入队
     * @param element
     */
    public void enQueueFront(E element){
      ensureCapacity(size+1);
        front = index(front-1);
      elements[front] =element;

        size++;

    }

    /**
     * 出队，从队尾出队
     * @return
     */
    public E deQueueRear(){
        int rear = index(front + size -1);
       E element =  elements[rear] ;
       elements[rear] = null;
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
     * 获取当前索引映射在循环队列里面的索引
     * @param index
     * @return
     */
    private int index(int index){
//        int mapIndex = index % elements.length;
        //前提：n< 2m
        // n%m = n>=m? n-m : n;
      if (index < 0){
          return index + elements.length;
      }else{
          return index >= elements.length? index - elements.length : index;
      }

    }
}
