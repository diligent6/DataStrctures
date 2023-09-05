package quick_union.improve;

import quick_union.QuickUnion;

/**
 * 基于size的优化
 */
public class QuickUnion_S extends QuickUnion {
    protected int[] size;
    
    public QuickUnion_S(int capacity) {
        super(capacity);
        
        size = new int[capacity];
        for (int i=0; i<parents.length;i++){
            size[i] =1;
        }
    }

    @Override
    public void union(int v1, int v2) {
        //情况考虑，一样高，有一个比较高
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2)return;
        if (size[root1] >=size[root2]){
            parents[root2] = root1;
            size[root1] += size[root2];
        }else {
            parents[root1] = root2;
            size[root2] += size[root1];
        }
    }
}
