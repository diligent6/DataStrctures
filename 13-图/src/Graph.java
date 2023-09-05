import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Graph<V,E>{

    //用于权重的比较，因为后续经常用到权重的相关比较所以封装为一个接口方便使用
    protected WeightManager<E> weightManager;

    public Graph(WeightManager<E> weightManager){
        this.weightManager = weightManager;
    }
    abstract int edgesSize(); //边的数量
    abstract int verticesSize(); //顶点数量

    abstract void addVertex(V v); //添加顶点
    abstract void addEdge(V from,V to); //添加边
    abstract void addEdge(V from,V to,E weight); //添加边

    abstract void removeVertex(V v); //删除顶点
    abstract void removeEdge(V from,V to); //删除边

    //图的遍历
    abstract  public void bfs(V beigin,vertexVisitor<V> visitor);
    abstract public void dfs(V beigin,vertexVisitor<V> visitor);

    //拓扑排序
    public abstract List<V> topologicalSort();

    //最小生成树算法：

    public abstract Set<EdgeInfo<V,E>> prim();

    public abstract Set<EdgeInfo<V,E>> kuruskal();

    //最短路径算法
    //dijkstra 算法 +bellford 算法
    public abstract Map<V,PathInfo<V,E>> shortPath(V begin);
    //floyd 算法 多源最短路径算法
    //key 每个源点  value 对应的最短路径信息 终点 + 最短路径信息
    public abstract Map<V,Map<V,PathInfo<V,E>>> shortPath();

    //封装EdgeInfo信息作为生成树里面的每条边进行返回
    public static class EdgeInfo<V,E>{
        private V from;
        private V to;
        private E weight;
        public EdgeInfo(V from, V to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        public V getFrom() {
            return from;
        }
        public void setFrom(V from) {
            this.from = from;
        }
        public V getTo() {
            return to;
        }
        public void setTo(V to) {
            this.to = to;
        }
        public E getWeight() {
            return weight;
        }
        public void setWeight(E weight) {
            this.weight = weight;
        }
        @Override
        public String toString() {
            return "EdgeInfo [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }

    public static class PathInfo<V, E> {
        protected E weight;
        protected List<EdgeInfo<V, E>> edgeInfos = new LinkedList<>();
        public PathInfo() {}
        public PathInfo(E weight) {
            this.weight = weight;
        }
        public E getWeight() {
            return weight;
        }
        public void setWeight(E weight) {
            this.weight = weight;
        }
        public List<EdgeInfo<V, E>> getEdgeInfos() {
            return edgeInfos;
        }
        public void setEdgeInfos(List<EdgeInfo<V, E>> edgeInfos) {
            this.edgeInfos = edgeInfos;
        }
        @Override
        public String toString() {
            return "PathInfo [weight=" + weight + ", edgeInfos=" + edgeInfos + "]";
        }
    }


    abstract  interface vertexVisitor<V>{
        boolean visit(V v);
    }

    //用于权重相关的计算
    public interface WeightManager<E> {
        int compare(E w1, E w2);
        E add(E w1, E w2);
        E zero();
    }


}



