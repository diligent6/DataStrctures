package com.jiaran.stack;

/**
 * 栈 基于数组实现
 * @param <E>
 */
public class Stack <E>{
    private ArrayList<E> arrayList =new ArrayList<>();


    public int size(){
        return arrayList.size();
    }
    public void push(E element){
        arrayList.add(element);
    }

    public E pop(){
        return arrayList.remove(arrayList.size()-1);
    }

    public E top(){
        return arrayList.get(arrayList.size()-1);
    }

    public void clear(){
        arrayList.clear();
    }

    public boolean isEmpty(){
        return arrayList.isEmpty();
    }
}
