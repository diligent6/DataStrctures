package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

/**
 * 归并排序
 * 核心思想：
 * 先拆分到最小，然后依次合并两个有序序列，形成一个大的有序序列
 * 重复上述过程就将全部序列变为有序序列
 * @param <T>
 */
public class MergeSort  <T extends Comparable<T>>extends Sort<T> {

    //用于备份左边的数组
    private T[] leftArry;
    @Override
    protected void sort() {
        leftArry = (T[]) new Comparable[array.length >>1];
        sort(0,array.length);
    }

    /**
     * 进行归并排序
     * @param begin
     * @param end
     */
    private void sort(int begin,int end){
        //一个元素不用排序
        if (end - begin <2) return;
        int mid = (begin +end)>>1;
        //将左右进行排序
        sort(begin,mid);
        sort(mid,end);
        merge(begin,end,mid);
    }

    /**
     * 将[begin，mid) 和[mid,end)范围内的序列合并为一个有序序列
     * @param begin
     * @param end
     * @param mid
     */
    private void merge(int begin,int end,int mid){
        //定义指针
        int li=0,le = mid-begin;
        int ri = mid,re = end;
        int ai = begin;
        // 将左边的数组进行备份
        for (int i =li;i< le; i++){
            leftArry[i] = array[begin +i];
        }
        //进行合并
        while (li < le){//当左边等于lr 说明左边部分排序完毕，那么归并就可以结束了
            if (ri < re && cmp(ri,li) <0){
                array[ai++] = array[ri++];
            }else{
                array[ai++] = array[li++];
            }
        }
    }
}
