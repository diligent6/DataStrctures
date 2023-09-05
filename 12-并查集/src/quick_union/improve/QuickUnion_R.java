package quick_union.improve;

import quick_union.QuickUnion;

/**
 * 基于rank的优化
 */
public class QuickUnion_R extends QuickUnion {
    //记录每个集合树的高度
    protected int[] rank;


    public QuickUnion_R(int capacity) {
        super(capacity);
        rank = new int[capacity];
        for (int i =0;i<parents.length; i++){
            rank[i] = 1;
        }
    }


    /**
     * 合并思路让高度矮的集合合并到高度高的上面
     * @param v1
     * @param v2
     */
    @Override
    public void union(int v1, int v2) {
       //情况考虑，一样高，有一个比较高
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2)return;
        if (rank[root1] >rank[root2]){
            parents[root2] = root1;
        }else if (rank[root1] <rank[root2]){
            parents[root1] = root2;
        }else{
            parents[root1] = root2;
            rank[root2]+=1;
        }
    }
}
