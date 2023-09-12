package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;
//优化思路：不采取连续交换的方式了，不进行交换，只是将当前位置往后挪动，直到找到合适的位置然后放入
public class InserctionSort2 <T extends Comparable<T>>extends Sort<T> {

    @Override
    protected void sort() {
        for (int i=1; i<array.length; i++){
            int j =i;
            T value = array[j];
            while (j >0 && cmp(value,array[j-1])<0){
                array[j] = array[j-1];
                j--;
            }
            array[j] = value;

        }
    }
}
