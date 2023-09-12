package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

public class InserctionSort3 <T extends Comparable<T>>extends Sort<T> {

    @Override
    protected void sort() {

        for (int i = 1; i < array.length; i++) {
            //开始进行挪动操作
            insert(i,binarySearch(i));
        }
    }
    private void insert(int source,int dest){
        T v = array[source];
        for (int i=source; i>dest;i--){
            array[source] =array[source-1];
        }
        array[dest] = v;
    }


    //查找范围【begin，end)
    private int binarySearch( int endIndex){
        //计算mid索引
        int begin =0;
        int end = endIndex;
        while (begin < end){
            int mid = (begin + end)>>1;
            T midValue = array[mid];
            if (cmp(array[endIndex],midValue)<0){
                end = mid;
            }else {
                begin = mid +1;
            }
        }
        return begin;
    }
}
