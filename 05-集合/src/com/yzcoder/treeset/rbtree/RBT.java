package com.yzcoder.treeset.rbtree;

import java.util.Comparator;

/**
 * 红黑树
 */
public class RBT<E> extends BBST<E>{

    //==========================================定义属性=======================================================
    // 定义颜色常量
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    //定义节点内部类 【红黑树节点增加了颜色属性】
    private static class RBNode<E> extends Node<E>{
        // 默认添加的节点是红色，能够更快满足红黑树的性质
        boolean color = RED;

        public RBNode(E element,Node<E>parent){
            super(element,parent);
        }
    }
    // 构造方法
    public RBT(){
        super(null);
    }

    public RBT(Comparator<E> comparator){
        super(comparator);
    }
    //============================================辅助方法======================================================
    // 包括染色、求节点的颜色、求当前节点的兄弟节点

    /**
     * 求传入节点的颜色
     * @param node
     * @return
     */
    private boolean colorOf(Node<E> node){
        return node ==null ? BLACK : ((RBNode<E>)node).color;
    }
    private boolean isBlack(Node<E> node){
        return colorOf(node)==BLACK;
    }
    private boolean isRed(Node<E> node){
        return colorOf(node)==RED;
    }

    /**
     * 染色
     * @param node
     * @param color
     * @return  返回染色后的节点
     */
    private Node<E> color(Node<E> node,boolean color) {
        // TODO:null 节点直接返回了，会有什么影响
        if (node == null) return node;
        ((RBNode<E>)node).color = color;
        return node;
    }
    private Node<E> black(Node<E> node){
        return color(node,BLACK);
    }
    // 将该节点染为红色
    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    //===========================================进行增加和删除后方法的编写=======================================================

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
    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent =node.parent;
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
        Node<E> uncle = parent.siblingNode();
        // TODO:祖先节点为什立刻染成红色了？
        //这里祖先节点染成红色对后面有影响吗？如果原来是黑色染成红色 对于 非上溢的情况有影响吗？思考一下
        // 这里染红是正确的因为不管是后面那种情况都需要进行染红操作，上溢是作为新节点添加，非上溢需要把黑色节点给其它的节点 形成B树节点 所以grand需要进行染红
        Node<E> grand = red(parent.parent);

//        --不符合性质4 同时上溢
        if (isRed(uncle)){//叔父节点红色 【B树节点上溢】
            black(parent);
            black(uncle);
            //把祖父节点当作新添加的节点进行处理 向上处理 【祖父节点需要染红】
            afterAdd(grand);
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
    @Override
    protected void afterRemove(Node<E> node) {
        //如果删除的节点是红色 直接返回不处理
        //传入的节点有一个子节点是红色的
        if (isRed(node)){
            //TODO:为什么把当前节点染成黑色，应该把子节点染成黑色 这里时进行了代码的整合处理 传入的节点时replacement
            black(node);
            return;
        }
        // 删除的节点是黑色节点


        Node<E> parent = node.parent;
        //删除的是根节点
        //说明树里面就一个根元素 直接返回不处理
        if (parent == null) return;




        //删除的节点是黑色叶子节点【发生下溢】
        //获取它的兄弟节点
        //TODO： 为什么这里 要进行一个非空的判断，如果这样sibling有可能就是node
        //因为node节点已经被断开了
        boolean left = parent.left ==null || node.isLeftChild();
        Node<E> sibling = left ? parent.right :parent.left;


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
}
