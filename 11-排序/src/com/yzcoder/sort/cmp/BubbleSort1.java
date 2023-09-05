package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

public class BubbleSort1<T extends Comparable<T>> extends Sort<T> {
    //冒泡排序：
    //思想：在最大范围内通过交换找到最大位置的数
    @Override
    protected void sort() {
        //变换最大范围
        for (int i =array.length-1;i>0;i--){
            for (int j=1;j<=i;j++){
                if (cmp(j,j-1)<0){
                    swap(j,j-1);
                }
            }
        }
    }
}
