package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 数组已经部分有序，即后面部分已经有序了
 */
public class BubbleSort3 <T extends Comparable<T>>extends Sort<T> {

    /**
     * 优化思路：
     * 如果后面已经部分有序了，那么最大范围可以调整为后面已经有序的第一个位置
     */
    @Override
    protected void sort() {
        for (int i = array.length-1; i > 0; i-- ){
            int sortIndex = 1;
            for (int j =1; j<=i; j++){
                if (cmp(j,j-1)<0){
                    swap(j,j-1);
                    sortIndex = j;
                }
            }
            i = sortIndex;
        }

    }
}
