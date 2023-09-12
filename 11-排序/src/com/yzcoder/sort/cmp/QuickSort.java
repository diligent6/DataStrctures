package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

public class QuickSort  <T extends Comparable<T>>extends Sort<T> {
    @Override
    protected void sort() {

        sort(0,array.length);
    }

    //将【begin,end)使用快速排序
    private void sort(int begin,int end){

        if ((end - begin)<2)return;
        int mid = pivot(begin, end);
        sort(begin,mid);
        //轴点元素已经归位了所以不用对其排序了 mid 要加一
        sort(mid+1,end);

    }

    //获取给定范围内的轴点元素的位置
    private int pivot(int begin,int end){
        //选取一个作为轴点元素
        T pivot = array[begin];
        end--;
        //将轴点元素放入合适的位置
        while (begin < end){
            //下面就进行让左右元素进行归位，一边归位依次，进行交替进行，先进行右边
            while (begin<end){
                if (cmp(array[end],pivot)>=0){
                    end--;
                }else {
                    array[begin++] = array[end];
                    break;
                }
            }

            //从往右扫描
            while (begin <end){
                if (cmp(array[begin],pivot )<=0){
                    begin++;
                }else {
                    //保证交替进行，完成一次就结束循环让另一边进行归位
                    array[end--] = array[begin];
                    break;
                }
            }
        }
        //让轴点元素归位
        array[begin] = pivot;
        return begin;

    }
}
