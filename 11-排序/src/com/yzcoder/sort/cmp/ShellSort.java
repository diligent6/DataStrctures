package com.yzcoder.sort.cmp;

import com.yzcoder.sort.Sort;

import java.util.ArrayList;
import java.util.List;

public class ShellSort  <T extends Comparable<T>>extends Sort<T> {
    @Override
    protected void sort() {
        //遍历step序列进行对每个step进行排序
        List<Integer> setpList = getSetpList();
        for (Integer step : setpList) {
            sort(step);
        }
    }
    //求步长序列
    private List<Integer> getSetpList(){
        ArrayList<Integer> stepList = new ArrayList<>();
        int step = array.length;
        //计算每个步长序列并存储
        while ((step >>=1)>0){
            stepList.add(step);
        }
        return stepList;

    }

    //将给定步长序列的每一列排序
    private void sort(Integer setp){
        //遍历每一列
        for (int col = 0; col <setp; col++){
            //将每列的元素进行排序
            for (int val= setp+col;val <array.length; val+=setp){
                int j = val;
                T value = array[val];
                while (j>col&& cmp(value,array[j-setp])<0){
                    array[j] = array[j-setp];
                    j-=setp;
                }
                array[j] = value;
            }
        }
    }

}
