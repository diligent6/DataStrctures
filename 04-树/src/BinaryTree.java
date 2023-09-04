import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树的顶层类
 */
public class BinaryTree<E>{
    protected int size;
    protected Node<E> root;//根节点


    protected static class Node<E>{
        protected E element;
        protected Node<E> left;
        protected Node<E> right;
        protected Node<E> parent;

        public Node(E element,Node<E> parent){
            this.element = element;
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
        public Node<E> siblingNode(){
            if (isLeftChild()){
                return  parent.right;
            }
            if (isRightChild()){
                return parent.left;
            }

            return null;
        }
    }

    /**
     * 访问器 用于对元素进行访问并控制遍历的进行
     * @param <E>
     */
    public static abstract class Visitor<E>{
        public boolean stop;

        public abstract boolean visit(E element);
    }

    public int size(){
        return size;
    }
    public void clear(){
        size =0;
        root =null;

    }
    public boolean isEmpty(){
        return size ==0;

    }

    /**
     * 创建节点的方法，用于给AVL树创建节点
     */
    //TODO:思考一下可以通过new 创建节点为什么还需要这个？答：提高通用性，因为AVLTree 和RBT 节点有所不同 添加的时候通过这个方法能进行扩展并且复用添加逻辑
    //AVLTree 和RBT 节点 只需要重写这个方法换成自己的对应节点就行了
    // * 创造节点
    //* AVL树 与 B数 的节点各自有其特性
    //* 因此在 BinaryTree 中提供一个方法让他们去覆盖
    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent); // 默认返回一个通用节点
    }




    //遍历方法

    /**
     * 前序遍历 根左右
     */
    public void preOrder(Visitor visitor){
       if (visitor == null) return;
        preOrder(root,visitor);

    }
    protected void preOrder(Node<E> node,Visitor visitor){
        if (node == null || visitor.stop)return;
        // 根
       visitor.stop = visitor.visit(node.element);

       preOrder(node.left,visitor);

       preOrder(node.right,visitor);
    }


    /**
     * 中序遍历 左根右
     * @param visitor
     */
    public void inOrder(Visitor<E> visitor){
        if (visitor == null) return;
        inOrder(root,visitor);
    }
    protected void inOrder(Node<E> node,Visitor<E> visitor){
        if (node == null || visitor.stop) return;

        inOrder(node.left,visitor);

        if (visitor.stop) return;

        visitor.stop = visitor.visit(node.element);

        inOrder(node.right,visitor);
    }


    public void postOrder(Visitor<E> visitor){
        if (visitor == null) return;
        postOrder(root,visitor);

    }

    protected void postOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;

        postOrder(node.left, visitor);
        // 右
        postOrder(node.right, visitor);
        // 根
        if (visitor.stop) return;
        visitor.stop = visitor.visit(node.element);

    }

    /**
     * 层序遍历
     *
     * @param visitor
     */
    public void levalOrder(Visitor<E> visitor){
        //合法性判断
        if (root ==null || visitor ==null) return;

        Queue<Node<E>> queue = new LinkedList<>();

        queue.offer(root);
        while (!queue.isEmpty()){
            //1. 出队
            Node<E> node = queue.poll();

            if (visitor.visit(node.element)) return;

            //2.入队 按照编号入队
            if (node.left !=null){
                queue.offer(node.left);
            }
            if (node.right !=null){
                queue.offer(node.right);
            }
        }

    }

    /**
     * 递归实现
     * @return
     */
    public int height(){
        return  height(root);
    }
    public  int height(Node<E>node){
        if (node ==null) return 0;

        //当前节点的高度 是由当前节点所在层数和 左右子节点中最大的高度绝对的
        return 1 + Math.max(height(node.left),height(node.right));
    }

    public int height2(){
        if (root ==null) return 0;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        //根节点是第一层，该层节点数量必定是1
        int leverSize =1;
        int height =0;
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();

            leverSize--;
            if (node.left !=null){
                queue.offer(node.left);
            }
            if (node.right !=null){
                queue.offer(node.right);
            }
            if (leverSize ==0){
                //当前层节点已经全部出队 更新高度
                height++;
                // 记录下一层的节点数量
                leverSize = queue.size();

            }
        }
        return height;
    }
    /**
     * 是否是完全二叉树
     * @return
     */
    public boolean isCompleteTreee(){
        if (root ==null) return false;

        //层序遍历 过程中进行判断
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        /**
         *  完全二叉树判断条件
         *  1. 不能只有一个右子节点
         *  2. 从只有左子节点开始后面全部是叶子节点
         */
        boolean isLeaf = false;
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();

            // 如果要求是叶子节点 但当前节点并不是
            if (isLeaf && !node.isLeafNode()){
                return false;
            }

            if (node.left !=null){
                queue.offer(node.left);
            }else {
                //不是完全二叉树
                // node.left == null && node.right != null
                if (node.right!=null)return false;
            }

            if (node.right !=null){
                queue.offer(node.right);
            }else{
                // 从这个节点往后都是叶子节点
                // node.left == null && node.right == null
                // node.left != null && node.right == null
                isLeaf = true;
            }

        }
        return true;

    }



    /**
     * 求前驱节点 左根右
     * @param node
     * @return
     */
    protected Node<E>  predecessor(Node<E> node){
        //1. 合法性判断
        if (node ==null ) return null;
        //2. 在左子树中寻找
        if (node.left !=null){
            node  =node.left;
            while (node.right !=null){
                node = node.right;
            }
            return node;
        }
        //3.在父节点中寻找
        while (node.parent !=null && node.isLeftChild()){
            node = node.parent;
        }
        //TODO: node节点一定存在 要么是root节点 或者真正找到了
        //结束条件是：node.parent ==null 或者node.isRightChild()
        return node;
    }

    /**
     * 求后继节点 左根右
     * @param node
     * @return
     */
    protected  Node<E> successor(Node<E> node){
        //1。合法性判断
        if (node == null) return null;
        //2. 在右子树中找
        if (node.right !=null){
            node = node.right;
            while (node.left !=null){
                node = node.left;
            }
            return  node;
        }
        //3.在根节点中寻找
        while (node.parent!=null && node.isRightChild()){
            node = node.parent;
        }

        return node;
    }

}
