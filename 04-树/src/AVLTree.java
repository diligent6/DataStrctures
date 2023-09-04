import java.util.Comparator;

/**
 * 平衡树
 * @param <E>
 */
public class AVLTree<E> extends BBST<E>{

    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    public AVLTree() {
        this(null);
    }


    //=========================================================================================
    //对原来的节点进行拓展，增加一个height属性 ，用于平衡树通过计算平衡因子进行保持平衡
    private static class AVLNode<E> extends Node<E>{
        // 节点的高度 默认节点是1
         int height=1;
        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }
        public int balanceFactor(){// 计算平衡因子 左子树高度-右子树高度
            int leftHeight = left ==null ?0 :((AVLNode<E>)left).height;
            int rightHeight = right ==null ?0 :((AVLNode<E>)right).height;
            return leftHeight - rightHeight;
        }
        public void updateHeight(){//更新高度
            int leftHeight = left ==null ?0 :((AVLNode<E>)left).height;
            int rightHeight = right ==null ?0 :((AVLNode<E>)right).height;
            height = 1 + Math.max(leftHeight,rightHeight);
        }
        public Node<E> tallerChild(){//获取高度较高的子节点
            int leftHeight = left ==null ?0 :((AVLNode<E>)left).height;
            int rightHeight = right ==null ?0 :((AVLNode<E>)right).height;
            if (leftHeight > rightHeight) return left;
            if (rightHeight >leftHeight)return right;
            //如果子节点高度一样就返回 与当前同方向的子节点
            return isLeftChild() ? left :right;
        }

    }
    /**
     * 恢复平衡需要进行的操作：
     * 一：判断是否失衡
     * 1. 封装一个方法进行平衡因子的绝对值 并与1进行比较
     * 2. 更新高度的方法 每到一个新的节点都要进行更新节点高度 【添加或者删除要更新高度】
     * 二：恢复平衡
     * 1. 封装 旋转的所有方法
     * 2. 封装一个用于判断不同情况下进行旋转的恢复平衡方法
     * 3. 封装一个旋转后的一些通用操作调整的方法
     */

    //=========================================================================================
    /**
     * 重写父类中的 createNode
     * 返回 AVLNode
     * TODO：这个是什么作用？提高通用性，节点不一致对node节点进行了扩展
     */
    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }
    /**
     * 判断传入节点是否平衡（平衡因子的绝对值 <= 1）
     */
    private boolean isBalanced(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    /**
     * 更新高度
     */
    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }


    //=========================================================================================
    /**
     * 旋转方法封装 包括左旋和右旋
     * 目标：让失衡节点下来，子节点 子孙节点上了达到平衡的效果
     * 前提条件：失衡的第一个节点必定是添加处的祖父节点、删除的也是某个节点的祖父节点【是删除节点父节点或者祖父节点】
     */

//    /**
//     * 左旋
//     * @param grand
//     */
//    private void rotateLeft(Node<E> grand){
//       Node<E> parent = grand.left;
//       Node<E> child = parent.left;
//       grand.right = child;
//       parent.left = grand;
//
//
//        //TODO:旋转后统一操作
//        afterRotate(grand,parent,child);
//
//    }
//    private void rotateRight(Node<E> grand){
//        Node<E> parent = grand.right;
//        Node<E> child = parent.right;
//        grand.left = child;
//        parent.right=grand;
//
//        //TODO:旋转后统一操作
//        afterRotate(grand,parent,child);
//    }
//
//    /**
//     *
//     * 进行旋转后的统一调整包括 调整【祖先节点、父节点、孩子】的父节点指向谁
//     * * 和原来 祖先节点的父节点指向哪个新的子节点
//     * * 更新高度，首先更新祖先节点、然后更新父节点
//     * 以上操作不管是左旋还是右旋都要进行所以通过方法对这些公共代码进行封装
//     * @param grand 失衡的节点
//     * @param parent 失衡节点的tallerchild
//     * @param child g和p交换的字数（child本来是p的子树，旋转后变为g的子树）
//     */
//    private void afterRotate(Node<E> grand,Node<E> parent,Node<E> child){
//        // 更新每个节点的父节点指向【包括父节点是谁，和原来父节点的新子节点指向】
//        // 1.更新parent的父节点指向【注意是双向包括让parent 指向新的父节点还有更新原来父节点的指向】
//        parent.parent = grand.parent;
//        Node<E> preGrandParent = grand.parent;
//        if (grand.isLeftChild()){
//            preGrandParent.left = parent;
//        }else if (grand.isRightChild()){
//            preGrandParent.right = parent;
//        }else{//原来grand的父节点是空节点 说明grand 是根节点
//            root = parent;
//        }
//        //2. 更新grand 和child的父节点指向
//        if (child !=null){
//            child.parent = grand;
//        }
//        grand.parent = parent;
//
//        //更新高度
//        updateHeight(grand);
//        updateHeight(parent);
//    }

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

//        更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    //=========================================================================================
    /**
     * 接下来对恢复平衡的代码进行封装
     * 目标：通过适当的旋转 实现失衡后恢复平衡
     * 包括四种情况：LL、RR、LR、RL
     */
    private void reBalance(Node<E> grand){
        // 获取失衡节点后面的较高子节点 通过对他们进行旋转达到平衡
        Node<E> parent = ((AVLNode<E>)grand).tallerChild();
        Node<E> child =  ((AVLNode<E>)parent).tallerChild();

        // 判断属于那种情况 从而进行对应的旋转
        if (parent.isLeftChild()){//L
            if (child.isLeftChild()){//LL
                rotateRight(grand);// LL对失衡节点右旋
            }else{//LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else{//R
            if (child.isLeftChild()){//RR
                rotateLeft(grand);
            }else{//RL
                rotateRight(parent);
                rotateLeft(grand);
            }

        }
    }
    //=================================================================================================
    /**
     * 下面对于添加和删除进行对应操作进行判断是否失衡和恢复平衡的操作
     */
    @Override
    protected void afterAdd(Node<E> node) {

        //从添加的节点一直往上
        // 如果失衡就恢复平衡
        //否则就更新高度
        while (node.parent!=null){
            node = node.parent;
            if (isBalanced(node)){//更新高度
                updateHeight(node);
            }else{//失衡 恢复平衡
                reBalance(node);
                break;// 只要下面失衡的恢复平衡整棵树恢复平衡
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //从删除的节点一直往上
        // 如果失衡就恢复平衡
        //否则就更新高度
        while (node.parent!=null){
            node = node.parent;
            if (isBalanced(node)){//更新高度
                updateHeight(node);
            }else{//失衡 恢复平衡  // 删除可能导致上面很多节点都失衡 必须一直往上进行遍历寻找
                reBalance(node);
            }
        }
    }
}

