package com.yzcoder;

import com.yzcoder.sort.Sort;
import com.yzcoder.sort.cmp.*;
import com.yzcoder.tools.Asserts;
import com.yzcoder.tools.Integers;
import com.yzcoder.tools.Times;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //随机数组
        Integer[] random = Integers.random(10000, 10, 20000000);
        //普通数组小数量
        Integer[] array = {7, 3, 5, 8, 6, 7, 4, 5};
        //完全有序数组
        Integer[] sortedArray = Integers.headAscOrder(10, 200, 0);
        //头部有序
        Integer[] headAscOrder = Integers.headAscOrder(10, 200, 30);
        testSort(random,
                new BubbleSort1(),
                new BubbleSort2(),
                new BubbleSort3(),
                new SelectionSort(),
                new HeapSort(),
                new InserctionSort1(),
                new InserctionSort2(),
                new InserctionSort3(),
                new MergeSort(),
                new ShellSort(),
                new QuickSort());

    }

    public static void testSort(Integer[] array,Sort ...sorts){
        for (Sort sort : sorts) {
            Integer[] newArray = Integers.copy(array);
            sort.sort(newArray);
            Asserts.test(Integers.isAscOrder(newArray));
        }
        Arrays.sort(sorts);
        for (Sort sort : sorts) {
            System.out.println(sort.toString());

        }
    }
}
