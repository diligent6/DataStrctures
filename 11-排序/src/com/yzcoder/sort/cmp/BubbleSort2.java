package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 冒泡排序优化一：已经排好序了
 */
public class BubbleSort2<T extends Comparable<T>>extends Sort<T> {
    @Override
    protected void sort() {
        for (int i=array.length-1;i >0;i--){
            boolean sorted =true;
            for (int j=1; j<=i; j++){
                if (cmp(j,j-1)<0){
                    sorted = false;
                    swap(j,j-1);
                }
            }
            if (sorted)return;
        }
    }
}
