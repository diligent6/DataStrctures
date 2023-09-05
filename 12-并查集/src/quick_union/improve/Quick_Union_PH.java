package quick_union.improve;

/**
 * 路径减半
 */
public class Quick_Union_PH extends QuickUnion_R{
    public Quick_Union_PH(int capacity) {
        super(capacity);
    }

    /**
     * 每隔一个节点就让它的父节点变为祖父节点
     * @param v
     * @return
     */
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (parents[v] !=v){
            parents[v] =parents[parents[v]];
            v =  parents[v];
        }
        return v;
    }
}
