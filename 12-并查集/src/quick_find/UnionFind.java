package quick_find;

public abstract class UnionFind {

    protected int[] parents;

    public UnionFind(int capacity){
        //外界必须初始化容量大小
        if (capacity <=0){
            throw new IllegalArgumentException("capacity must be >=1");
        }

        parents = new int[capacity];
        //初始化单元素集合
        for (int i=0;i < parents.length; i++){
            parents[i] =i;
        }
    }


    //定义接口


    private boolean isSame(int v1,int v2){
        return find(v1) == find(v2);
    }

    /**
     * 找到v的所属集合【找到它的根节点】
     * @param v
     * @return
     */
    public abstract int find(int v);

    /**
     * 合并v1、v2所属的集合
     * @param v1
     * @param v2
     */
    public abstract void union(int v1,int v2);


    //合法性检测
    protected void rangeCheck(int v){
        if (v<0 || v>=parents.length){
            throw new IndexOutOfBoundsException("v is not allowed");
        }
    }
}
