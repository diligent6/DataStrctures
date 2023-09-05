import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
public class ListGraph<V,E> extends Graph<V, E> {
    //传入的v 和对应点类的映射 TODO：思考一下为什么要用映射，为什么不和边一样使用集合存储？
    //方便取出对应value的点
    //传入时传入的就是点的值，所以将值和点进行了映射
    private HashMap<V,Vertex<V,E>> vertices =new HashMap<>();
    //边的集合
    private Set<Edge<V,E>> edges = new HashSet<>();



    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    /**
     * 点
     * @param <V>
     * @param <E>
     */
    private static class Vertex<V,E>{
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>(); // 进来的边
        Set<Edge<V, E>> outEdges = new HashSet<>(); // 出去的边
        public Vertex(V value){
            this.value = value;
        }
        @Override
        public boolean equals(Object obj) {
            return Objects.equals(value, ((Vertex<V, E>)obj).value);
        }
        @Override
        public int hashCode() {
            return value == null ? 0 : value.hashCode();
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }

    }
    /*
     * 边
     */
    private static class Edge<V, E> {
        Vertex<V, E> from; // 出发点
        Vertex<V, E> to; // 到达点
        E weight;	// 权值

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        public EdgeInfo<V,E> info(){
            return  new EdgeInfo<V,E>(from.value, to.value,weight);
        }

        @Override
        public boolean equals(Object obj) {
            Edge<V, E> edge = (Edge<V, E>) obj;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }
        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }
        @Override
        public String toString() {
            return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }

    }


    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertices.size();
    }



    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v))return;
        vertices.put(v,new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
       this.addEdge(from,to,null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        //如果from 和 to点类不存在就进行创建并添加到map中
        if (!vertices.containsKey(from)){
            vertices.put(from,new Vertex<>(from));
        }
        if (!vertices.containsKey(to)){
            vertices.put(to,new Vertex<>(to));
        }
        //获取起始点
        Vertex<V,E> fromVertex = vertices.get(from);
        Vertex<V,E> toVertex = vertices.get(to);

        //创建对应的边
        Edge<V,E> edge = new Edge<>(fromVertex,toVertex);
        edge.weight = weight;

        //如果边已经存在就删除
        if (fromVertex.outEdges.remove(edge)){
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        //添加对应的边
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        //从map中删除对应的点，如果不存在就不进行后续处理
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex ==null)return;

        //遍历点的入度集合，并从边的集合中删除
        for (Iterator<Edge<V,E>> iterator = vertex.inEdges.iterator();iterator.hasNext();){
            Edge<V,E> edge =iterator.next();//取出进入该点的边

            edge.from.outEdges.remove(edge); //从起始点中删除这条边
            iterator.remove(); //将当前遍历到的元素从inEdges中删除
            edges.remove(edge);
        }

        //遍历点的出度集合，并从边的集合中删除
        for (Iterator<Edge<V,E>>iterator = vertex.outEdges.iterator();iterator.hasNext();){
            Edge<V,E> edge = iterator.next(); //取出出去的边

            edge.to.inEdges.remove(edge);//从终点中删除这条边
            iterator.remove();//从起始点中删除这条边
            edges.remove(edge);

        }
    }

    @Override
    public void removeEdge(V from, V to) {

        //获取起始点
        Vertex<V,E> fromVertex = vertices.get(from);
        if (fromVertex ==null) return;
        Vertex<V,E> toVertex = vertices.get(to);
        if (toVertex ==null) return;

        //根据起始点获取对应的边
        Edge<V, E> edge = new Edge<V, E>(fromVertex, toVertex);
        //TODO:思考一下为什么不直接进行删除？要进行判断？提高了性能，如果一次删除不成功就不要进行后续的操作了
        if (fromVertex.outEdges.remove(edge)){
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }




    @Override
    public void bfs(V begin, vertexVisitor<V> visitor) {
        // 遍历器如果不存在就不进行遍历
        if (visitor ==null) return;

        //根据begin找到对应的 节点
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        //准备工作
        Queue<Vertex<V,E>>queue = new LinkedList<>();
        //记录已经遍历过的节点
        Set<Vertex<V,E>> visitedVertices = new HashSet<>();
        //开始广度遍历
        queue.offer(beginVertex);
        visitedVertices.add(beginVertex);
        while(!queue.isEmpty()){
            Vertex<V,E> vertex = queue.poll();
            if (visitor.visit(vertex.value))return;
            //遍历让当前节点的直接出度节点依次入队
            for (Edge<V, E> edge : vertex.outEdges) {
                if (visitedVertices.contains(edge.to))continue;
                queue.offer(edge.to);
                visitedVertices.add(edge.to);
            }
        }
    }


    @Override
    public void dfs(V begin, vertexVisitor<V> visitor) {

        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex ==null ||visitor==null)return;
        Set<Vertex<V,E>> visitedVertices = new HashSet<>();
        dfsRecursion(beginVertex,visitor,visitedVertices);
    }

    /**
     * 递归辅助函数
     * @param beginVertex
     * @param visitor
     */
    private void dfsRecursion(Vertex<V,E> beginVertex, vertexVisitor<V> visitor,Set<Vertex<V,E>> visitedVertices) {


        //2.访问节点

        if (visitor.visit(beginVertex.value))return;
        visitedVertices.add(beginVertex);
        //3.递归遍历
        for (Edge<V, E> edge : beginVertex.outEdges) {
            if (visitedVertices.contains(edge.to))continue;
            dfsRecursion(edge.to,visitor,visitedVertices);
        }

    }




    @Override
    public List<V> topologicalSort() {
        //准备工作
        ArrayList<V> list = new ArrayList<>();
        HashMap<Vertex<V, E>, Integer> ins = new HashMap<>();
        Queue<Vertex<V,E>> queue =new LinkedList<>();

        //进行初始化
        this.vertices.forEach((V v, Vertex<V,E> vertex)->{
            int indegree = vertex.inEdges.size();//入度
            if (indegree ==0){
                queue.offer(vertex);
            }else{
                ins.put(vertex,indegree);
            }
        });

        //进行遍历
        while (!queue.isEmpty()){
            //出队 同时存储结果
            Vertex<V,E> vertex = queue.poll();
            list.add(vertex.value);

            //入队 继续遍历
            for (Edge<V, E> edge : vertex.outEdges) {

                Integer toIndegree = ins.get(edge.to) -1;
                if (toIndegree ==0){
                    queue.offer(edge.to);
                }else{//入度不为0 更新他们的入度值
                    ins.put(edge.to,toIndegree);
                }
            }

        }
        return list;
    }



    @Override
    public Set<EdgeInfo<V, E>> prim() {
        //准备工作

        //生成树顶点的数量
        int verticesSize = vertices.size();
        //遍历每个节点
        Iterator<Vertex<V,E>> it = vertices.values().iterator();
        if (!it.hasNext())return null;

        //准备 记录添加节点的集合、存储结果的集合、小根堆
        Set<EdgeInfo<V,E>> edgeinfos = new HashSet<>();
        Set<Vertex<V,E>> addVertices = new HashSet<>();
        //进行初始化 随机选取一个作为起点
        Vertex<V,E> vertex = it.next();
        addVertices.add(vertex);
        //初始化小根堆
        MinHeap<Edge<V,E>> heap = new MinHeap<>(vertex.outEdges,edgeComparator);

        while (!heap.isEmpty() && addVertices.size() <verticesSize){
            Edge<V, E> edge = heap.remove();
            if (addVertices.contains(edge.to))continue;

            edgeinfos.add(edge.info());
            addVertices.add(edge.to);
            heap.addAll(edge.to.outEdges);
        }


        return null;
    }
    //用于比较两条边的权重大小 TODO：思考一下这个lambda实例化函数式接口的例子
    //具体比较规则由外界制定
    private Comparator<Edge<V, E>> edgeComparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
        return weightManager.compare(e1.weight, e2.weight);
    };
    @Override
    public Set<EdgeInfo<V, E>> kuruskal() {
        //获取生成树边的数量
        int edgeSize = vertices.size()-1;
        if (edgeSize ==-1) return null; //说明是空图

        Set<EdgeInfo<V,E>> edgeInfos = new HashSet<>();
        MinHeap<Edge<V,E>> heap = new MinHeap<>(edges,edgeComparator);

        //并查集 通过并查集来判断出现环的情况
        UnionFind<Vertex<V, E>> uf = new UnionFind<>();
        //首先每个点形成单元素集合
        vertices.forEach((V v, Vertex<V,E> vertex)->{
            uf.makeSet(vertex);
        });

        while(!heap.isEmpty() && edgeInfos.size() <edgeSize){
            Edge<V,E> edge = heap.remove();
            //有环就跳过这个不添加
            if (uf.isSame(edge.from,edge.to))continue;

            edgeInfos.add(edge.info());
            //这个过程最终会将连在一起的点变为同一个集合，这样当有线再连接这些集合中的某两个点时就能进行判断避免出现环
            uf.union(edge.from,edge.to);
        }
        return edgeInfos;
    }

    @Override
    public Map<V,PathInfo<V,E>>shortPath(V begin) {
        return dijkstra(begin);

    }

    private  Map<V,PathInfo<V,E>> dijkstra(V begin){
        //获取源点的类
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex ==null) return null;

        //记录最终到各个点距离的信息 使用map记录 key是各个点 value是相关最短信息
//        Map<V,E> selectedPaths = new HashMap<>();
        Map<V,PathInfo<V,E>> selectedPaths = new HashMap<>();
        //当前源点到各个点的最短距离
        Map<Vertex<V,E>,PathInfo<V,E>> paths = new HashMap<>();

        //初始化paths
        for (Edge<V, E> edge : beginVertex.outEdges) {

            PathInfo<V, E> vePathInfo = new PathInfo<>();
            vePathInfo.weight = edge.weight;
            vePathInfo.edgeInfos.add(edge.info());
            paths.put(edge.to,vePathInfo);
        }
        while (!paths.isEmpty()){
            //每次取出最短的路径放入 结果map中
            Entry<Vertex<V, E>, PathInfo<V,E>> minPathEntry = getMinPath(paths);
            Vertex<V, E> minVertex = minPathEntry.getKey();
            PathInfo<V, E> minPaths = minPathEntry.getValue();
            selectedPaths.put(minVertex.value,minPaths);
            paths.remove(minVertex);


            for (Edge<V, E> edge : minVertex.outEdges) {
                if (selectedPaths.containsKey(edge.to.value) ) continue;
                relaxForDijkstra(edge,minPaths,paths);
            }
        }
        selectedPaths.remove(begin);
        return selectedPaths;
    }

    private Map<V,PathInfo<V,E>> bellManFord(V begin){
        //松弛操作的次数
        int countSize = vertices.size();
        //存储返回结果的map
        Map<V,PathInfo<V,E>> selectedPaths = new HashMap<>();
        //初始化
        PathInfo<V,E> beginPathInfo = new PathInfo<>(weightManager.zero());
        selectedPaths.put(begin,beginPathInfo);

        //循环迭代
        for (int i=0;i<countSize;i++){
            //对每条边进行松弛操作
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
                //说明当前起点的最短信息还没有更新无法进行松弛操作
                if (fromPath ==null) continue;
                //进行松弛操作
                relax(edge,fromPath,selectedPaths);
            }
        }
        //检测负权环，如果进行了n-1次的松弛操作 还可以进行松弛操作，说明源点到某点的距离是一直可以变小的，那么就存在负权边
        for (Edge<V, E> edge : edges) {
            PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
            if (fromPath == null) continue;
            if (relax(edge,fromPath,selectedPaths)){
                System.out.println("有负权环，不存在最短路径");
                return null;
            }
        }
        // 从最短路径中移除源点的最短路径信息
        selectedPaths.remove(begin);
        return selectedPaths;
    }



    /**
     * 松弛操作
     * @param edge
     * @param fromPath
     * @param paths
     * @return
     */
    private boolean relax(Edge<V,E> edge,PathInfo<V,E> fromPath,Map<V,PathInfo<V,E>> paths){
        // TODO:思考一下松弛操作思路
        E newWeight = weightManager.add(edge.weight, fromPath.weight);
        PathInfo<V, E> oldPath = paths.get(edge.from.value);
        //不用进行松弛操作
        if (oldPath !=null && weightManager.compare(oldPath.weight,newWeight)<=0) return false;

        //需要进行松弛操作
        if (oldPath ==null){//说明源点并没有到这个点的距离信息，那么需要进行创建
            oldPath = new PathInfo<>();
            paths.put(edge.to.value,oldPath);
        }else{//存在 但是信息需要更新
            oldPath.edgeInfos.clear();
        }
        oldPath.setWeight(newWeight);
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());
        return true;
    }
    /**
     * 松弛操作
     * @param edge 要进行松弛的边
     * @param fromPath edge from的路径信息
     * @param paths  存放着其他的点的路径信息
     */
    private void relaxForDijkstra(Edge<V,E> edge,PathInfo<V,E> fromPath,Map<Vertex<V,E>,PathInfo<V,E>> paths){
        E newWeight = edge.weight;
        PathInfo<V, E> oldPath = paths.get(edge.to);
        //判断是否需要进行松弛
        if (oldPath !=null &&weightManager.compare(oldPath.getWeight(),newWeight)<=0)return;

        //进行松弛 对paths中的路径信息进行更新

        if (oldPath ==null){//创建PathInfo
            oldPath = new PathInfo<>();
            paths.put(edge.to,oldPath);
        }else{
            oldPath.edgeInfos.clear();
        }
        oldPath.weight = newWeight;
        oldPath.edgeInfos.addAll(fromPath.edgeInfos);
        oldPath.edgeInfos.add(edge.info());
    }
    /**
     * 获取最短路径
     * @param paths
     * @return
     */
    private Entry<Vertex<V,E>,PathInfo<V,E>> getMinPath(Map<Vertex<V,E>,PathInfo<V,E>> paths){
        //获取迭代器
        Iterator<Entry<Vertex<V, E>, PathInfo<V, E>>> iterator = paths.entrySet().iterator();

        Entry<Vertex<V,E>, PathInfo<V,E>> miniEntry =iterator.next();
        while (iterator.hasNext()){
            Entry<Vertex<V, E>, PathInfo<V, E>> entry = iterator.next();
            if (weightManager.compare(entry.getValue().weight, miniEntry.getValue().getWeight())<0){
                miniEntry = entry;
            }
        }
        return miniEntry;
    }

    /**
     * floyd算法
     *
     * @return
     */
    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortPath() {
        //返回的结果map
        Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();
        //初始化操作 TODO：如何进行初始化 将所有边的起始点进行初始化
        for (Edge<V, E> edge : edges) {

            Map<V, PathInfo<V, E>> map = paths.get(edge.from.value);
            if (map == null){//map信息还没有 就进行创建
                map = new HashMap<>();
                paths.put(edge.from.value,map);
            }
            PathInfo<V,E> pathInfo = new PathInfo<>();
            pathInfo.weight = edge.weight;
            pathInfo.edgeInfos.add(edge.info());
            map.put(edge.to.value,pathInfo);
        }

        //进行遍历操作
        vertices.forEach((V v2,Vertex<V,E> vertex2)->{
            vertices.forEach((V v1,Vertex<V,E> vertex1)->{
                vertices.forEach((V v3,Vertex<V,E> vertex3)->{
                    //如果全部相等不进行后续操作
                    if (v2.equals(v1) || v2.equals(v3) ||v1.equals(v3))return;
                    //比较距离信息
                    PathInfo<V, E> path12 = getPathInfo(v1, v2, paths);
                    if (path12 ==null) return;

                    PathInfo<V, E> path23 = getPathInfo(v2, v3, paths);
                    if (path23 ==null) return;

                    PathInfo<V, E> path13 = getPathInfo(v1, v3, paths);

                    E newWeight = weightManager.add(path12.weight , path23.weight);
                    if (path13 !=null && weightManager.compare(path13.weight, newWeight)<=0) return;


                    if (path13 == null){
                        path13 = new PathInfo<>();
                        paths.get(v1).put(v3,path13);
                    }else{
                        path13.edgeInfos.clear();
                    }

                    path13.weight = newWeight;
                    path13.edgeInfos.addAll(path12.edgeInfos);
                    path13.edgeInfos.addAll(path23.edgeInfos);

                });
            });
        });
         return paths;
    }

    private PathInfo<V,E> getPathInfo(V from,V to,Map<V,Map<V,PathInfo<V,E>>> paths){
        //TODO:思考一下map为什么是空 因为有的点 不能作为起始点遍历过程是遍历的所有点
        Map<V, PathInfo<V, E>> map = paths.get(from);

        return map ==null ? null :map.get(to);


    }





}
