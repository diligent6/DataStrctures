package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 堆排序
 * 和选择排序思路一样，选择最大的排到最后
 * 利用堆能提高找到最大的效率
 */
public class HeapSort <T extends Comparable<T>>extends Sort<T> {
    //TODO:通过一个变量记录堆的大小
    private int heapSize;

    @Override
    protected void sort() {
        //原地建堆
        heapSize = array.length;
        for (int i=(heapSize>>1);i>=0;i--){
            siftDown(i);
        }

        //堆排序
        while (heapSize>1){
            //每次将最大元素和最后一个元素进行交换，然后减少堆的数量
            //重复这个过程
            swap(0,--heapSize);
            siftDown(0);
        }


    }

    //下滤，使用自下而上的下滤进行建堆
    private void siftDown(int index){
        //TODO：思考一下要更改heapsize大小
        //获取非叶子节点的数量，只有当前节点有子代节点才会进行下滤
        int noLeafNodeSize = heapSize>>1;
        int currentIndex = index;
        T parentValue = array[index];
        while (currentIndex < noLeafNodeSize){
           int childIndex = (currentIndex<<1)+1;
            T child = array[childIndex];
            int rightIndex = childIndex +1;
            if (rightIndex < heapSize&& cmp(rightIndex,childIndex)>0){
                child = array[rightIndex];
            }
           if (cmp(parentValue,child)>=0)return;
           array[currentIndex] = child;
           currentIndex = childIndex;
        }
        array[currentIndex] = parentValue;

    }
}

