package quick_find;

public class QuickFind extends UnionFind{


    public QuickFind(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);
        return parents[v];
    }

    /**
     * 将v1集合嫁接到v2集合中
     * @param v1
     * @param v2
     */
    @Override
    public void union(int v1, int v2) {
        //获取两个所属的集合
        int p1 = parents[v1];
        int p2 = parents[v2];
        if (p1 ==p2) return;

        //将根节点是p1的所有元素的根节点都设置为p2完成嫁接
       for (int i = 0;i<parents.length;i++){
           if (parents[i] ==p2){
               parents[i] = p2;
           }
       }
    }
}
