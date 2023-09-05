package com.yzcoder;

import java.util.Comparator;

public abstract class AbstrctHeap<E>  implements Heap<E>{

    protected int size;
    protected Comparator<E> comparator;

    public AbstrctHeap(Comparator<E> comparator){
        this.comparator = comparator;
    }

    public AbstrctHeap(){
        this(null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    protected int compare(E e1,E e2){
        if (comparator !=null){
            return comparator.compare(e1,e2);
        }
        return ((Comparable)e1).compareTo(e2);
    }
}
