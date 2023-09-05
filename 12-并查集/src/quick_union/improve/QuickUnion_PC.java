package quick_union.improve;

/**
 * 基于路径压缩的优化
 */
public class QuickUnion_PC extends QuickUnion_R{

    public QuickUnion_PC(int capacity) {
        super(capacity);
    }

    /**
     * 路径压缩，在find时让通过路径上的所有节点都指向父节点
     * 从而进行压缩树的高度
     * @param v
     * @return
     */
    @Override
    public int find(int v) {
        rangeCheck(v);
      if (parents[v] !=v){
          parents[v] =find(parents[v]);
      }
      return parents[v];
    }
}
