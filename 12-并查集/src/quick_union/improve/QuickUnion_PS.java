package quick_union.improve;

/**
 * 路径分裂的优化
 */
public class QuickUnion_PS  extends QuickUnion_R{
    public QuickUnion_PS(int capacity) {
        super(capacity);
    }

    /**
     * 让每个节点都指向它的祖父节点
     * @param v
     * @return
     */
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (parents[v] !=v){
            int parnt = parents[v];
            parents[v] = parents[parents[v]];
            v = parnt;
        }
        return parents[v];
    }
}
