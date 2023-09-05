package quick_union;

public class QuickUnion extends UnionFind{
    public QuickUnion(int capacity) {
        super(capacity);
    }

    /**
     * 含有多层关系，要一直往上寻找根节点
     * @param v
     * @return
     */
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (parents[v] !=v){
             v = parents[v];
        }
        return parents[v];
    }

    @Override
    public void union(int v1, int v2) {

        int p1 = parents[v1];
        int p2 = parents[v2];

        if (p1 ==p2) return;
        parents[p1] = p2;
    }
}
