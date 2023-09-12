package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 插入排序
 * 核心思想：
 * 不断维护有序序列，让有序序列不断壮大，减少无序序列
 * 基本过程时，遍历无序序列，从中取出一个值然后采取一定的方式放入有序序列的正确位置
 * 其中核心要点是：正确位置，和采取一定的方式
 * 重复上述过程
 */
public class InserctionSort1 <T extends Comparable<T>>extends Sort<T> {


    @Override
    protected void sort() {
        //遍历无序序列
        for (int i=1; i<array.length; i++){
            //从当前位置找到合适的位置放入这个无序中挑选出来的元素
            int j = i;
            while (j >0 && cmp(j,j-1) <0){
                //这里采取的是交换
                swap(j,j-1);
                j--;
            }
        }
    }
}
