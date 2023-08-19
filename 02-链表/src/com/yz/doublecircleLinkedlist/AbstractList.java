package com.yz.doublecircleLinkedlist;

//抽象类 用于抽取公共字段 和 公用方法
public abstract class AbstractList<E> implements List<E> {
    //元素数量
    protected int size;

    //**********************工具函数封装***********************************

    /**
     * 下标越界
     * @param index
     */
    protected void outBounds(int index){
        throw new IndexOutOfBoundsException("index:"+index+", size:"+this.size);
    }

    /**
     * 检查下标是否越界
     * @param index
     */
    protected void rangeCheck(int index){
        if (index<0 || index>=this.size){
            outBounds(index);
        }
    }
    /**
     * 检查添加时下标是否越界
     * @param index
     */
    protected void rangeCheckForAdd(int index){

        if (index<0 || index>this.size){
            outBounds(index);
        }
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
        return indexOf(element)!=ELEMENT_NOT_FOUND;
    }

    public int size() {
        return size;
    }

}
