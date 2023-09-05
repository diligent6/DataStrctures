package com.yzcoder.sort;

import com.yzcoder.Student;

import java.text.DecimalFormat;


/**
 * 排序算法的顶层类
 */
public abstract class Sort<T extends Comparable<T>> implements Comparable<Sort<T>> {

    protected T[] array;
    //算法性能的一些衡量标准
    private int cmpCount;//比较次数
    private int swapCount;//交换次数
    private long time;   //时间
    private DecimalFormat fmt = new DecimalFormat("#.00");//用于格式化数字格式，保留两位小数

    //用于对数组初始化同时记录 运行时间
    public void sort(T[] array){
        if (array ==null || array.length <2)return;

        this.array = array;
        long begin = System.currentTimeMillis();
        sort();
        time = System.currentTimeMillis() - begin;

    }
    //每个算法实现自己的排序逻辑
    protected abstract void sort();
    //用于比较各个算法的性能
    @Override
    public int compareTo(Sort<T> o) {
        int result = (int)(time - o.time);


        if (result !=0) return result;

        result = cmpCount - o.cmpCount;
        if (result !=0)return result;

        return swapCount - o.swapCount;
    }

    //一些子类用到的 公共方法
    protected int cmp(int i1, int i2){
        cmpCount++;
        return array[i1].compareTo(array[i2]);
    }

    protected int cmp(T v1,T v2){
        cmpCount++;
        return v1.compareTo(v2);
    }
    protected void swap(int i1,int i2){
        swapCount++;
        T temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }

    //衡量算法是否稳定
    private boolean isStable(){

        Student[] students = new Student[20];

        for (int i =0;i<students.length ;i++){
            students[i] = new Student(i*10,10);
        }
        sort((T[]) students);
        for (int i=1;i<students.length;i++){
            int score = students[i].score;
            int prevScore = students[i -1].score;
            if (score !=prevScore +10) return false;
        }
        return true;
    }

    //格式化数字
    private  String numberString(int number){
        if (number < 10000) return "" +number;

        if (number < 100000000)return fmt.format(number/10000.0)+"万";
        return fmt.format(number/100000000.0) + "亿";
    }
    public String toString(){
        String timeStr = "耗时：" + (time / 1000.0) + "s(" + time + "ms)";
        String compareCountStr = "比较：" + numberString(cmpCount);
        String swapCountStr = "交换：" + numberString(swapCount);
        String stableStr = "稳定性：" + isStable();
        return "【" + getClass().getSimpleName() + "】\n"
                + stableStr + " \t"
                + timeStr + " \t"
                + compareCountStr + "\t "
                + swapCountStr + "\n"
                + "------------------------------------------------------------------";
    }
}
