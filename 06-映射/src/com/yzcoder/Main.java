package com.yzcoder;

public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, Integer> integerIntegerTreeMap = new TreeMap<>();
        java.util.TreeMap<Integer, Integer> integerIntegerTreeMap1 = new java.util.TreeMap<>();
        integerIntegerTreeMap.traversal(new Map.Visitor<Integer, Integer>() {

            @Override
            public boolean visit(Integer key, Integer value) {
                System.out.println(key+value);
                return true;
            }
        });
    }
}
