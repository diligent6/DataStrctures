import java.util.Comparator;

/**
 * 二叉搜索树 ，有序树，左<根《右
 * Binary Search Tree
 * @param <E>
 */
public class BST<E> extends BinaryTree<E>{

    //比较器 ：外界可以自定义比骄规则
    protected Comparator<E> comparator;

    public BST(Comparator<E> comparator){
        this.comparator = comparator;
    }

    public BST(){
        this(null);
    }

    /**
     * 是否包含某个元素
     * @param element
     * @return
     */
    public boolean contains(E element){
        return node(element) !=null;
    }

    /**
     *  增加
     * @param element
     */
    public void add(E element){
        /**
         * 基本思路：找到指定位置的父元素 然后进行添加
         */
        elementNotNullCheck(element);
        //添加的是第一个元素
        if (root ==null){
            root = createNode(element,null);
            size ++;
            afterAdd(root);
            return;
        }
        // 添加的不是第一个元素
        Node<E> node = root;
        Node<E> parent = root;
        int cmp =0;
        while (node !=null){//元素非空
            parent = node;
            //获取元素与当前节点的比较值
            cmp = compareTo(element,node.element);
            if (cmp >0){
                node = node.right;
            }else if (cmp <0){
                node = node.left;
            }else {
                node.element =element;
                return;
            }
        }
        Node<E> newNode= new Node<>(element,parent);
        if (cmp >0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }

        //更新状态
        size++;
        afterAdd(newNode);
    }
    protected  void afterAdd(Node<E> node){
        return;
    }
    /**
     * 删除的基本思路：
     * 1. 删除根据被删除节点的度的不同分情况进行讨论
     * 2. 当度为2时，转化为删除前驱或者后继节点【度一定为1或者0】
     * 3. 当度为1时，用父节点指向它的子节点
     * 4. 当度为0时，父节点指向null
     * @param element
     */
    public void remove(E element){
       elementNotNullCheck(element);
       remove(node(element));
    }
    public void remove(Node<E> node){
        if (node ==null)return;
        size --;
        //度为2 转化为度为1或者0 后续进行处理
        if (node.hasTwoChildren()){
            //用后继节点的元素覆盖当前节点元素
            Node<E> s =successor(node);
            node.element = s.element;
            //转化为后继节点进行后续的删除【前驱和后继都一样，这里转化为后继】
            node =  s;
        }
        //处理度为1 或者0
        //找到替代节点
        Node<E> replacement = node.left !=null ?node.left:node.right;
        Node<E> parent  = node.parent;
        //进行判断
        if (replacement !=null){// 删除的节点度为1
            replacement.parent = parent;
            if (parent ==null){
                root = replacement;
            }else if (replacement.isLeftChild()){
                parent.left = replacement;
            }else{
                parent.right = replacement;
            }
            afterRemove(replacement);
        }else{//删除的节点度为0
            if (parent ==null){//删除的是根节点
                root = null;
                afterRemove(node);
            }else if(node.isLeftChild()){
                parent.left =null;
            }else {
                parent.right = null;
            }
            afterRemove(node);
        }

    }
    protected void afterRemove(Node<E> node){
        return;
    }
    //根据元素的值找到对应的节点元素
     private Node<E> node(E element){
        //非空判断
        elementNotNullCheck(element);

        Node<E> node = root;
        while (node !=null){
            int cmp = compareTo(element,node.element);//获取元素与当前节点的比较值
            if (cmp >0){
                node = node.right;
            }else if(cmp <0){
                node = node.left;
            }else{
                return node;
            }
        }
        return null;//元素没找到
     }
    //封装一个比较方法
    private int compareTo(E e1,E e2){
        //比较规则：关系 返回值
//              e1>e2  大于0
//              e1=e2 等于零
//               e1<e2 小于零
        if (comparator !=null){ //有比较器就使用比较器进行比较
            return comparator.compare(e1,e2);
        }
        //没有传入比较器，元素内部必须实现Comparable接口 保证了 元素是可比较的
        return ((Comparable<E>)e1).compareTo(e2);

    }
    private void elementNotNullCheck(E element){
        if (element == null){//元素非空检查
            throw new IllegalArgumentException("elements must not null");
        }
    }
}
