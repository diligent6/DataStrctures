package com.yzcoder.treeset.rbtree;

import java.util.Comparator;

/**
 * 用于对平衡树的代码抽象实现代码复用
 * Balanced Binary Search Tree
 */
public class BBST<E> extends BST<E> {

    //=================================================================================================================
    /**
     * 构造方法使用构造器
     * @param comparator
     */
    public BBST(Comparator<E> comparator){
       super(comparator);
    }
    public BBST(){
        super(null);
    }
    //=================================================================================================================
    //抽取的 公共旋转代码
    /**
     * 旋转方法封装 包括左旋和右旋
     * 目标：让失衡节点下来，子节点 子孙节点上了达到平衡的效果
     * 前提条件：失衡的第一个节点必定是添加处的祖父节点、删除的也是某个节点的祖父节点【是删除节点父节点或者祖父节点】
     */

    /**
     * 左旋
     * @param grand
     */
    protected void rotateLeft(BinaryTree.Node<E> grand){
        BinaryTree.Node<E> parent = grand.left;
        BinaryTree.Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;


        //TODO:旋转后统一操作
        afterRotate(grand,parent,child);

    }
    protected void rotateRight(BinaryTree.Node<E> grand){
        BinaryTree.Node<E> parent = grand.right;
        BinaryTree.Node<E> child = parent.right;
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
    protected void afterRotate(BinaryTree.Node<E> grand, BinaryTree.Node<E> parent, BinaryTree.Node<E> child){
        // 更新每个节点的父节点指向【包括父节点是谁，和原来父节点的新子节点指向】
        // 1.更新parent的父节点指向【注意是双向包括让parent 指向新的父节点还有更新原来父节点的指向】
        parent.parent = grand.parent;
        BinaryTree.Node<E> preGrandParent = grand.parent;
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

}
