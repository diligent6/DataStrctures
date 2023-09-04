package com.yzcoder;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 基于红黑树实现Map映射
 * @param <K>
 * @param <V>
 */
public class TreeMap<K,V> implements Map<K,V>{

    //====================================================属性相关================================================================

    // 定义颜色常量【红黑树相关】
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    //元素数量
    private int size;

    //根节点
    private Node<K,V> root;

    //比较器
    private Comparator<K> comparator;

    //===========================================================节点内部类==========================================================
    /**
     * 节点内部类
     */
    private static class Node<K,V>{
        K key;
        V value;

        // 默认添加的节点是红色，能够更快满足红黑树的性质
        boolean color = RED;

        protected Node<K,V> left;
        protected Node<K,V> right;
        protected Node<K,V> parent;

        public Node(K key,V value,Node<K,V> parent){
            this.key = key;
            this. value = value;
            this.parent = parent;

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

    //===================================================构造方法================================================================

    public TreeMap(Comparator<K> comparator){
        this.comparator = comparator;
    }
    public TreeMap(){
        this(null);
    }
    //===================================================具体逻辑实现================================================================

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size ==0;
    }

    @Override
    public void clear() {
        this.size =0;
        root =null;
    }

    /**
     * 添加，如果相等就进行覆盖并返回旧的value
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        //1.合法性检测
        keyNotEmptyCheck(key);
        //2. 添加逻辑
        //2.0 如果添加的是第一个节点 先进行处理
        if (root == null){//添加的是第一个节点
            root = new Node<>(key,value,null);
            size ++;
            //修复平衡
            afterPut(root);
            //结束函数
            return null;
        }
        //2.1 初始化两个重要参数，父级节点，比较结果
        Node<K,V> parent = root;
        Node<K,V> node = root;
        //TODO:思考一下这个cmp 初始值设置为多少好呢？
        int cmp = 0;
        //用于进行遍历节点,找到合适的父节点

        while (node !=null){
            parent = node;
            cmp = compare(key,node.key);
            if (cmp >0){
                node =node.right;
            }else if (cmp <0){
                node = node.left;
            }else {//相等就进行覆盖
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        //2.2 放入节点
        Node<K,V> newNode = new Node<>(key,value,parent);
        if (cmp>0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        //2.3 更新数据
         size++;

        //3.恢复平衡
        afterPut(newNode);
        return null;
    }

    /**
     * 返回指定key的value
     * @param key
     * @return
     */
    @Override
    public V get(K key) {
        Node<K,V> node = node(key);
        return node !=null ? node.value :null;
    }

    private Node<K,V> node(K key){
        keyNotEmptyCheck(key);
        if (root ==null) return null;
        Node<K,V> node = root;
        int cmp = 0;
        while (node !=null){
            cmp = compare(key,node.key);
            if (cmp >0){
                node = node.right;
            }else if (cmp <0){
                node = node.left;
            }else {
                return node;
            }
        }
        return null;
    }

    /**
     * 树的删除逻辑 + 修复平衡
     * @param key
     * @return
     */
    @Override
    public V remove(K key) {
        keyNotEmptyCheck(key);
        return remove(node(key));
    }

    public V remove(Node<K,V> node){
        
        if (node == null) return null;
        
        V oldValue = node.value;
        //删除的节点度为2
        if (node.hasTwoChildren()){
            Node<K,V> s = successor(node);
            node.key = s.key;
            node.value = s.value;
            node = s;
        }
        //统一处理度为1 或者0的节点

        //找到替换节点 核心逻辑：用子节点替换掉被删除节点
        Node<K,V> replacement= node.left ==null? node.right:node.left;
        if (replacement !=null) {//度为1
            Node<K, V> parent = node.parent;
            if (parent == null) {
                parent = root;
            }
            replacement.parent = parent;
            if (node.isLeftChild()) {
                parent.left = replacement;
            } else {
                parent.right = replacement;
            }
            afterRemove(node);
        }else if (node.parent ==null){//node是根节点
            root =null;
            afterRemove(node);
        }else{
            if (node.isLeafNode()){
                node.parent.left = replacement;
            }else{
                node.parent.right = replacement;
            }
            afterRemove(node);
        }
        return oldValue;
    }
    @Override
    public boolean containsKey(K key) {
        return node(key)!=null;
    }

    /**
     * 中序遍历每个节点取出每个节点的value进行比对
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(V value) {
        if (root ==null)return false;
        Queue<Node<K,V>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()){//队列非空
            Node<K,V> node = queue.poll();
            if (node.value ==value || node.equals(value)){
                return true;
            }
            if (node.left !=null){
                queue.offer(node.left);
            }
            if (node.right !=null){
                queue.offer(node.right);
            }
        }
        return false;
    }

    /**
     * 中序遍历每个值让外界进行访问,中序遍历按照 左根右输出 有顺序
     * @param visitor
     */
    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor ==null)return;
        traversal(root,visitor);
    }
    private void traversal(Node<K,V>node,Visitor<K,V>visitor){
        if (node==null || visitor.stop)return;

        traversal(node.left,visitor);

        if (visitor.stop) return;
        visitor.stop =visitor.visit(node.key,node.value);

        traversal(node.right,visitor);
    }



    //============================================辅助方法======================================================


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
    private Node<K,V> color(Node<K,V> node,boolean color) {
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

    /**
     * 比较方法，比较器存在就是用比较器否则默认实现了comparable接口
     * @param k1
     * @param k2
     * @return
     */
    private int compare(K k1, K k2){
        if (comparator !=null){
            return comparator.compare(k1,k2);
        }else{
            return ((Comparable<K>)k1).compareTo(k2);
        }
    }

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

    protected void afterPut(Node<K,V> node) {
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
            afterPut(grand);
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
    protected void afterRemove(Node<K,V> node) {
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
                    afterRemove(parent);
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
                    afterRemove(parent);
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
    protected void afterRotate(Node<K,V> grand,Node<K,V> parent,Node<K,V> child){
        // 更新每个节点的父节点指向【包括父节点是谁，和原来父节点的新子节点指向】
        // 1.更新parent的父节点指向【注意是双向包括让parent 指向新的父节点还有更新原来父节点的指向】
        parent.parent = grand.parent;
        Node<K,V> preGrandParent = grand.parent;
        if (grand.isLeftChild()){
            preGrandParent.left = parent;
        }else if (grand.isRightChild()){
            preGrandParent.right = parent;
        }else{//原来grand的父节点是空节点 说明grand 是根节点
            root = parent;
        }
        //2. 更新grand 和child的父节点指向
        if (child !=null){
            child.parent = grand;
        }
        grand.parent = parent;

    }

    //TODO：找到后继节点
    private Node<K,V>successor(Node<K,V> node){
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
    
    
}
