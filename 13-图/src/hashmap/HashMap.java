package hashmap;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 基于哈希表实现Map
 * @param <K>
 * @param <V>
 */
public class HashMap<K,V> implements Map<K,V> {
    //==============================================TODO:属性=====================================================
    //定义常量
    // 定义颜色常量【红黑树相关】
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    //初始桶的容量
    private static final int DEFAULT_CAPACITY = 1 <<4;
    //装载因子
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //键值对总数量
    private int size;
    //桶数组
    private Node<K,V>[] table;

    /**
     * 节点内部类
     */
    protected static class Node<K,V> {
        K key;
        V value;
        int hash;
        // 默认添加的节点是红色，能够更快满足红黑树的性质
        boolean color = RED;



        protected Node<K,V> left;
        protected Node<K,V> right;
        protected Node<K,V> parent;

        public Node(K key, V value, Node<K,V> parent){
            this.key = key;
            this. value = value;
            this.parent = parent;
            this.hash = key==null?0:key.hashCode();
            this.hash = hash^(hash >>>16);
        }

        //节点方法
        public boolean isLeftChild(){
            return parent!=null && parent.left == this;
        }

        public boolean isRightChild(){
            return  parent !=null && parent.right == this;
        }

        public boolean isLeafNode(){
            return this.left ==null && this.right ==null;
        }

        public boolean hasTwoChildren() { // 是否有两个子节点
            return left != null && right != null;
        }

        //TODO: 兄弟节点可能是空节点，困惑：返回空节点：当前节点不存在，或者兄弟节点不存在
        // 外面在使用时可以进行判断
        public Node<K,V> siblingNode(){
            if (isLeftChild()){
                return  parent.right;
            }
            if (isRightChild()){
                return parent.left;
            }

            return null;
        }
    }

    //==================================================构造方法================================================================
    public HashMap(){
        table = new Node[DEFAULT_CAPACITY];
    }
    //===================================================TODO:具体接口逻辑==============================================================
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public void clear() {
        if (size ==0) return;
        for (int i =0;i<table.length;i++){
            table[i] = null;
        }
        size =0;
    }

    @Override
    public V put(K key, V value) {
        resize();
        // 计算出对应数组索引
        int index = index(key);
        Node<K,V> root = table[index];
        if (root ==null){//添加的元素是第一个元素
            root = createNode(key,value,null);
            table[index] = root;
            size++;
            fixAfterPut(root);
            return null;
        }
        // 添加的元素发生哈希冲突，放到指定位置
        //获取准备信息包括【遍历节点node、插入位置的父节点parent、递归遍历查找的result节点、递归遍历的状态searched、比较结果cmp】
        Node<K,V> node = root;
        Node<K,V> parent =root;
        Node<K,V> result = null;
        boolean searched = false;
        int cmp = 0;
        K k1 = key;
        int h1 = hash(k1);
        while (node !=null){
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
           //进行合理的判断，如果存在和key相等的就覆盖否则就添加的叶子节点
            if (h1 > h2){//根据哈希值判断
                cmp = 1;
            }else  if (h1 < h2){
                cmp = -1;
            }else if (Objects.equals(k1,k2)){
                cmp = 0;
            }else if (k1 !=null && k2 !=null//使用compare进行比较
            && k1 instanceof Comparable
            && (cmp =(((Comparable)k1).compareTo(k2))) !=0){
            }else if (searched){//已经递归扫描过
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }else {
                if (node.right !=null &&(result =node(node.right,k1) )!=null
                    || node.left !=null &&(result = node(node.left,k1))!=null){
                    node = result;
                    cmp =0;
                }else {
                    searched =true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }

            }

            //根据cmp结果进行操作
            if (cmp >0){
                node = node.right;
            }else if (cmp <0){
                node = node.left;
            }else {
                V oldValue = node.value;
                node.key = k1;
                node.value = value;
                return oldValue;
            }

        }
        // 添加节点
        Node<K,V> newNode = createNode(key,value,null);
        if (cmp >0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;
        fixAfterPut(newNode);
        return null;
    }



    @Override
    public V get(K key) {

        Node<K,V> node = node(key);
        return node !=null?node.value :null;
    }

    @Override
    public V remove(K key) {



        Node<K,V> node = node(key);

        return remove(node);
    }

    private V remove(Node<K,V> node){
        if (node ==null)return null;

        Node<K,V> willNode = node;
        V oldValue = node.value;
        size --;


        if (node.hasTwoChildren()) { // 度为2的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2的节点的值
            node.key = s.key;
            node.value = s.value;
            node.hash = s.hash;
            // 删除后继节点
            node = s;
        }

        Node<K,V> replacement = node.left ==null?node.right :node.left;
        int index = index(node);//找到根节点索引

        if (replacement != null) { // node是度为1的节点
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left、right的指向
            if (node.parent == null) { // node是度为1的节点并且是根节点
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            fixAfterRemove(replacement);
        } else if (node.parent == null) { // node是叶子节点并且是根节点
            table[index] = null;
        } else { // node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else { // node == node.parent.right
                node.parent.right = null;
            }

            // 删除节点之后的处理
            fixAfterRemove(node);
        }
        afterRemove(willNode,node);

        return oldValue;
    }
    @Override
    public boolean containsKey(K key) {
        return node(key) !=null;
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value)) return true;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return false;

    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) return;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) return;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }



    //====================================================封装的辅助方法===================================================


    //--------------------------------------------TODO:<<哈希表独有>>----------------------------------------------------------

    /**
     * 扩容
     */
    private void resize() {
        //小于加载因子就不扩容
        if (size/table.length <=DEFAULT_LOAD_FACTOR)return;

        //进行扩容操作
        Node<K,V> [] oldTable = table;
        table = new Node[DEFAULT_CAPACITY <<1];

        //挪动原来的每个节点
        for (int i =0;i<oldTable.length; i++){
            //获取根节点
            Node<K,V> root = oldTable[i];
            if (root ==null) continue;
            Queue<Node<K,V>> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()){//队列非空

                Node<K,V> node =queue.poll();

                if (node.left !=null){
                    queue.offer(node.left);
                }

                if (node.right !=null){
                    queue.offer(node.right);
                }

                moveNode(node);
            }
        }
    }

    /**
     * 挪动节点作为新的节点放入table当中
     * @param newNode
     */
    private void moveNode(Node<K,V> newNode) {
        //重置
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;


        // 计算出对应数组索引
        int index = index(newNode);
        Node<K, V> root = table[index];
        if (root == null) {//添加的元素是第一个元素
            root = newNode;
            table[index] = root;
            fixAfterPut(root);
            return;
        }
        // 添加的元素发生哈希冲突，放到指定位置
        //获取准备信息包括【遍历节点node、插入位置的父节点parent、递归遍历查找的result节点、递归遍历的状态searched、比较结果cmp】
        Node<K, V> node = root;
        Node<K, V> parent = root;
//        Node<K,V> result = null;
        //TODO:思考一下为什么不用判断key是否相等了 因为这是挪动并不是第一次添加，如果存在相等在原来的红黑树中已经进行了覆盖
//        boolean searched = false;
        int cmp = 0;
        K k1 = newNode.key;
        int h1 = newNode.hash;
        while (node != null) {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            //进行合理的判断，如果存在和key相等的就覆盖否则就添加的叶子节点
            if (h1 > h2) {//根据哈希值判断
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (k1 != null && k2 != null//使用compare进行比较
                    && k1 instanceof Comparable
                    && (cmp = (((Comparable) k1).compareTo(k2))) != 0) {
            } else {
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }

        }

        //根据cmp结果进行操作
        if (cmp > 0) {
            node = node.right;
        } else if (cmp < 0) {
            node = node.left;
        }


        // 添加节点
        newNode.parent = parent;
        if (cmp >0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        fixAfterPut(newNode);



    }
    private Node<K,V> node(K key){
        Node<K,V> root = table[index(key)];
        return root==null ?null : node(root,key);
    }
    /**
     * 在node节点为根的树中【找到】和k1一样key的节点 找到了就返回
     * @param node
     * @param k1
     * @return
     */
    private Node<K,V> node(Node<K,V>node,K k1){

        int h1 = hash(k1);
        //存储扫描的后结果
        Node<K,V> result = null;
        int cmp = 0;

        while (node !=null){

            K k2 = node.key;
            int h2 = node.hash;
            //先根据哈希值进行比较
            if (h1 > h2){
                node = node.right;
            }else if (h1 < h2){
                node = node.left;
            }else if (Objects.equals(k1,k2)){//递归扫描的目的就是找有没有符合这个条件的节点
                return node;
            }else if (k1 !=null && k2 !=null
                  && k1 instanceof Comparable
                  && k1.getClass() == k2.getClass()
                  && (cmp =(((Comparable)k1).compareTo(k2)))!=0){
                node = cmp >0 ? node.right : node.left;
            }else if (node.right !=null &&(result = node(node.right,k1))!=null){//右子树递归扫描看是否有一致的
                return result;
            }else {
                node =node.left;
            }

        }
        return null;
    }

    /**
     * 根据key生成对应的索引（在桶数组中的位置）
     * @param key
     * @return
     */
    private int index(K key){
        return hash(key) &(table.length -1);
    }

    /**
     * 计算出node节点在table中的索引
     * @param node
     * @return
     */
    private int index(Node<K,V> node){
        return node.hash &(table.length -1);
    }
    /**
     * 对哈希值进一步进行扰动计算，增加其随机性
     * @param key
     * @return
     */
    private int hash(K key){
        if (key == null) return 0;
        int hash = key.hashCode();
        //扰动计算
        //让哈希值的高位和低位进行异或运算
        return hash ^(hash >>>16);
    }
    //--------------------------------------------TODO:<<红黑树相关>>----------------------------------------------------------


    // 包括染色、求节点的颜色、求当前节点的兄弟节点
    /**
     * 求传入节点的颜色
     * @param node
     * @return
     */
    private boolean colorOf(Node<K,V> node){
        return node ==null ? BLACK :node.color;
    }
    private boolean isBlack(Node<K,V> node){
        return colorOf(node)==BLACK;
    }
    private boolean isRed(Node<K,V> node){
        return colorOf(node)==RED;
    }

    /**
     * 染色
     * @param node
     * @param color
     * @return  返回染色后的节点
     */
    private Node<K,V> color(Node<K,V> node, boolean color) {
        // TODO:null 节点直接返回了，会有什么影响
        if (node == null) return node;
        node.color = color;
        return node;
    }
    private Node<K,V> black(Node<K,V> node){
        return color(node,BLACK);
    }
    // 将该节点染为红色
    private Node<K,V> red(Node<K,V> node) {
        return color(node, RED);
    }

//    /**
//     * 比较方法，比较器存在就是用比较器否则默认实现了comparable接口
//     * @param k1
//     * @param k2
//     * @return
//     */
//    private int compare(K k1, K k2){
//        if (comparator !=null){
//            return comparator.compare(k1,k2);
//        }else{
//            return ((Comparable<K>)k1).compareTo(k2);
//        }
//    }

    private void keyNotEmptyCheck(K key){
        if (key ==null){
            throw new IllegalArgumentException("Key must not be empty");
        }
    }


    //TODO：添加后恢复平衡

    /**
     * 基本思路：添加后的判定修复
     * 添加的位置：一定是B树节点的叶子节点
     * 情况讨论：
     * -1.父节点是黑色 直接添加即可 如果是根节点 染成黑色
     * -2 父节点是红色
     * --不符合性质4 同时需要考虑上溢问题 是否上溢 根据叔父节点进行判定
     * --不符合性质4 但没有上溢
     *  修复性质4，染色 + 旋转进行操作 恢复性质4
     * --不符合性质4 同时上溢
     *   -- 染色 同时挑出一个节点向上合并 同时将向上的节点作为新添加的节点处理，因为后续可能还会上溢
     * @param node
     */

    protected void fixAfterPut(Node<K,V> node) {
        Node<K,V> parent =node.parent;
        //-1.父节点是黑色 直接添加即可 如果是根节点 染成黑色
        //添加的节点是根节点 直接染黑 或者 上溢的节点到达了根节点
        if (parent == null){
            black(node);
            return;
        }
        //父节点是黑色 不需要进行任何处理
        if (isBlack(parent))return;

        // -2 父节点是红色
        // 判定是否上溢  如果上溢 叔父节点是红色
        Node<K,V> uncle = parent.siblingNode();
        // TODO:祖先节点为什立刻染成红色了？
        //这里祖先节点染成红色对后面有影响吗？如果原来是黑色染成红色 对于 非上溢的情况有影响吗？思考一下
        // 这里染红是正确的因为不管是后面那种情况都需要进行染红操作，上溢是作为新节点添加，非上溢需要把黑色节点给其它的节点 形成B树节点 所以grand需要进行染红
        Node<K,V> grand = red(parent.parent);

//        --不符合性质4 同时上溢
        if (isRed(uncle)){//叔父节点红色 【B树节点上溢】
            black(parent);
            black(uncle);
            //把祖父节点当作新添加的节点进行处理 向上处理 【祖父节点需要染红】
            fixAfterPut(grand);
            return;
        }
//        --不符合性质4 但没有上溢
        // 处理没有上溢的情况
        if (parent.isLeftChild()){//L
            if (node.isLeftChild()) {//LL
                black(parent); // 染黑父节点
            }else{// LR
                black(node);
                //本来这里需要染黑祖先节点的
                rotateLeft(parent);
            }
            //统一进行旋转
            rotateRight(grand);
        }else{// R
            if (node.isLeftChild()){//RL
                black(node);
                rotateRight(parent);

            }else{//RR
                black(parent);
            }
            rotateLeft(grand);

        }
    }
    //TODO：删除后恢复平衡
    /**
     * 删除后判断：
     * 1.删除的节点是红色 直接返回不用处理
     * 2. 删除的节点是黑色
     * --度为2 有两个红色子节点 根本不会发生，因为会转化为删除前驱或者后继，前驱或者后继的节点度必定是1或者0 转化为其它节点进行处理了
     * --度为1 有一个红色子节点 子节点染黑
     * --度为0 是叶子节点
     *      --- 会发生下溢
     *      ---要判断兄弟节点的情况 是红还是黑
     *      ---是黑
     *      看看兄弟节点是否有红色子节点可以借出，有红色子节点就通过旋转进行借出
     *      没有红色子节点，那么父节点下来进行合并 兄弟节点染红，父节点下来合并【有可能又造成下溢，判断父节点是否为黑决定是否进行对父节点下溢进行处理】
     *      ---是红
     *      将兄弟节点染黑 同时进行旋转，和父节点交换角色，然后将这种情况转化为兄弟节点是黑的情况
     * @param node
     */
    protected void fixAfterRemove(Node<K,V> node) {
        //如果删除的节点是红色 直接返回不处理
        //传入的节点有一个子节点是红色的
        if (isRed(node)){
            //TODO:为什么把当前节点染成黑色，应该把子节点染成黑色 这里时进行了代码的整合处理 传入的节点时replacement
            black(node);
            return;
        }
        // 删除的节点是黑色节点


        Node<K,V> parent = node.parent;
        //删除的是根节点
        //说明树里面就一个根元素 直接返回不处理
        if (parent == null) return;




        //删除的节点是黑色叶子节点【发生下溢】
        //获取它的兄弟节点
        //TODO： 为什么这里 要进行一个非空的判断，如果这样sibling有可能就是node
        //因为node节点已经被断开了
        boolean left = parent.left ==null || node.isLeftChild();
        Node<K,V> sibling = left ? parent.right :parent.left;


        if (left){// 被删除节点在左边，兄弟节点在右边
            // ---处理兄弟节点是红色的情况
            if (isRed(sibling)){//兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }
            //兄弟节点必然是黑色
            //看兄弟节点是否有红色子节点
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                //兄弟节点没有一个红色子节点
                //下面就需要将父节点拿下来进行合并操作，需要先判断父节点是否是黑色节点
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack){
                    //将父节点作为新的删除节点进行处理
                    fixAfterPut(parent);
                }

            }else{//兄弟节点必然有一个红色子节点，向兄弟节点借元素
                //TODO：这个地方应该是左右子节点都行都能作为判断条件
                //不是，这个是为了进行后续统一的左旋操作 这一步是为了向删除的位置输送一个黑色节点
                if (isBlack(sibling.right)){
                    rotateRight(sibling);
                    //更新兄弟节点，兄弟节点变为红色
                    sibling = parent.right;
                }
                //TODO:思考一下为什么要继承父节点的颜色？
                //我的思考：是为了保持原来父节点的状态，如果进行更改不保持的话可能会造成溢出
                color(sibling,colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);

            }

        }else{//被删除的节点在右边，兄弟节点在左边
            //判断兄弟节点的颜色
            if (isRed(sibling)){
                black(sibling);
                red(parent);
                rotateRight(parent);
                //更换兄弟
                sibling = parent.left;
            }

            //兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)){
                //兄弟节点没有红色子节点可以借出 父节点下来
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack){
                    fixAfterPut(parent);
                }
            }else{//兄弟节点必然有一个红色节点
                if (isBlack(node.left)){
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                color(sibling,colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);

            }

        }

    }
    protected void afterRemove(Node<K,V> willNode,Node<K,V>removeNode){};

    //TODO:旋转相关方法
    /**
     * 左旋
     * @param grand
     */
    protected void rotateLeft(Node<K,V> grand){
        Node<K,V> parent = grand.left;
        Node<K,V> child = parent.left;
        grand.right = child;
        parent.left = grand;


        //TODO:旋转后统一操作
        afterRotate(grand,parent,child);

    }
    protected void rotateRight(Node<K,V> grand){
        Node<K,V> parent = grand.right;
        Node<K,V> child = parent.right;
        grand.left = child;
        parent.right=grand;

        //TODO:旋转后统一操作
        afterRotate(grand,parent,child);
    }

    /**
     *
     * 进行旋转后的统一调整包括 调整【祖先节点、父节点、孩子】的父节点指向谁
     * * 和原来 祖先节点的父节点指向哪个新的子节点
     * * 更新高度，首先更新祖先节点、然后更新父节点
     * 以上操作不管是左旋还是右旋都要进行所以通过方法对这些公共代码进行封装
     * @param grand 失衡的节点
     * @param parent 失衡节点的tallerchild
     * @param child g和p交换的字数（child本来是p的子树，旋转后变为g的子树）
     */
    protected void afterRotate(Node<K,V> grand, Node<K,V> parent, Node<K,V> child){
        // 更新每个节点的父节点指向【包括父节点是谁，和原来父节点的新子节点指向】
        // 1.更新parent的父节点指向【注意是双向包括让parent 指向新的父节点还有更新原来父节点的指向】
        parent.parent = grand.parent;
        Node<K,V> preGrandParent = grand.parent;
        if (grand.isLeftChild()){
            preGrandParent.left = parent;
        }else if (grand.isRightChild()){
            preGrandParent.right = parent;
        }else{//原来grand的父节点是空节点 说明grand 是根节点
            table[index(grand)] = parent;
        }
        //2. 更新grand 和child的父节点指向
        if (child !=null){
            child.parent = grand;
        }
        grand.parent = parent;

    }

    //TODO：找到后继节点
    private Node<K,V> successor(Node<K,V> node){
        if (node ==null) return null;
        //在右子树中进行寻找
        if (node.right !=null){
            node = node.right;
            while (node.left !=null){
                node =node.left;
            }
            return node;
        }
        //父节点中寻找
        while (node.parent !=null &&node.isRightChild()){//父节点首先要存在！！！
            node = node.parent;
        }
        return node.parent;

    }
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }


}
