package com.jiaran;

import java.util.Arrays;

/**
 * 动态数组 实现
 */
public class ArrayList<E> {

    //元素数量
    private int size;
    //数组 用于存储元素
    private E[] elements;

    // 默认容量
    private static final int DEFAULT_CANPACITY = 10;
    private static final int NOT_FOUND = -1;

    public ArrayList(int capacity){
        capacity = DEFAULT_CANPACITY >=capacity?DEFAULT_CANPACITY:capacity;
        this.elements = (E[]) new Object[capacity];
    }

    public ArrayList(){
        this(DEFAULT_CANPACITY);
    }

    /**
     * 清空数组
     */
    public void clear(){
        // 防止内存泄漏
        for(int i =0;i<size;i++){
            elements[i] =null;
        }
        this.size = 0;
    }

    /**
     * 往index位置添加元素
     * @param index
     * @param element
     */
    public void add(int index,E element){
        //1.边界判断
        rangeCheckForAdd(index);
        ensureCanpacity(size+1);

        //2.腾出空间
        for (int i=this.size-1;i>=index;i--){
            elements[i+1] =elements[i];
        }
        //3. 放入元素
        elements[index] = element;
        //4. 状态更新
        this.size++;
    }

    /**
     * 往末尾添加元素
     * @param element
     */
    public void add(E element){
        add(size,element);
    }

    /**
     * 删除index位置的元素
     * @param index
     * @return
     */
    public E remove(int index){
        //1. 边界判断
        rangeCheck(index);

        //2.保留元素
        E element = elements[index];

        //3.覆盖元素
        for(int i =index;i<size;i++){
            elements[i] = elements[i+1];
        }
        //4. 更新状态
        /**
         * size = size -1
         * elements[size-1] =null 防止内存泄漏
         */
        elements[--size] = null;
        return element;
    }

    /**
     * 修改指定下标的元素
     * @param index
     * @param element
     * @return
     */
    public E set(int index,E element){
        //1.边界判断
        rangeCheck(index);
        //2. 保留元素 修改元素
        E oldElement = elements[index];
        elements[index] = element;
        //3.返回修改前的元素
        return oldElement;
    }

    /**
     * 返回指定下标处的元素
     * @param index
     * @return
     */
    public E get(int index){
        //1. 合法性判断
        rangeCheck(index);
        //2. 返回元素
        return elements[index];
    }

    /**
     * 返回元素在数组的下标
     * @param element
     * @return
     */
    public int indexOf(E element){

        if (element ==null){// 对null进行处理 更为健壮，能够存储null 容错率更大
            for (int i = 0;i < size; i++){
                if (elements[i] == null)return i;
            }

        }else{
            for (int i =0;i<this.size;i++){
                if (elements[i].equals(element) ){
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    /**
     * 判断数组是否为空
     * @return
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     * 是否包含给定元素
     * @param element
     * @return
     */
    public boolean contains(E element){
        return indexOf(element)!=NOT_FOUND;
    }








    //**********************工具函数封装***********************************

    /**
     * 下标越界
     * @param index
     */
    private void outBounds(int index){
       throw new IndexOutOfBoundsException("index:"+index+", size:"+this.size);
    }

    /**
     * 检查下标是否越界
     * @param index
     */
    private void rangeCheck(int index){
        if (index<0 || index>=this.size){
            outBounds(index);
        }
    }
    /**
     * 检查添加时下标是否越界
     * @param index
     */
    private void rangeCheckForAdd(int index){

        if (index<0 || index>this.size){
            outBounds(index);
        }
    }

    /**
     * 保证容量充足，动态扩容
     * @param newCanpacity
     */
    private void ensureCanpacity(int newCanpacity){

        int oldCanpacity = this.elements.length;
        if (oldCanpacity>=newCanpacity) return;

        // 扩容 变为原来容量的1.5倍
        oldCanpacity = oldCanpacity +(oldCanpacity>>1);
        E[] newElements = (E[]) new Object[oldCanpacity];
        for (int i =0;i<this.size;i++){
            //拷贝原数组到新数组中
            newElements[i] = elements[i];
        }
        elements =newElements;

    }

    /**
     * 打印数组[1,3,4,5] size =1=4
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i =0;i<size;i++){
            if (i==0){
                stringBuilder.append(elements[i]);
            }else{
                stringBuilder.append(",").append(elements[i]);
            }
        }
        stringBuilder.append("]").append(" size = "+this.size);
        return stringBuilder.toString();
    }
}
