package com.yzcoder;

import com.yzcoder.sort.Sort;
import com.yzcoder.sort.cmp.BubbleSort1;
import com.yzcoder.sort.cmp.BubbleSort2;
import com.yzcoder.sort.cmp.SelectionSort;
import com.yzcoder.tools.Asserts;
import com.yzcoder.tools.Integers;
import com.yzcoder.tools.Times;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Integer[] random = Integers.random(10000, 10, 20000000);
        Integer[] array = {7, 3, 5, 8, 6, 7, 4, 5};
        Integer[] integers = Integers.headAscOrder(10, 200, 0);
        Integers.println(integers);
        testSort(integers,
                new BubbleSort1(),
                new BubbleSort2(),
                new SelectionSort());

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
