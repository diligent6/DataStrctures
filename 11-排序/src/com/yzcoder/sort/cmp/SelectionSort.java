package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 选择排序
 */
public class SelectionSort<T extends Comparable<T>> extends Sort<T> {
    /**
     * 基本思路分析：
     * 和冒泡排序思想是一样的，每次在最大范围内让一个归位成功
     * 优化点在不进行多次“无用交换” 只于最大的元素进行交换，通过下标记录最大元素的位置
     * 最后进行一次交换
     */
    @Override
    protected void sort() {
        for (int i =array.length-1;i>0;i--){
            int maxIndex = 0;
            T max = array[maxIndex];
            for (int j=1;j<=i; j++){
                if (cmp(array[j],max)>=0){
                    maxIndex = j;
                    max = array[j];
                }
            }
            swap(maxIndex,i);
        }

    }
}
