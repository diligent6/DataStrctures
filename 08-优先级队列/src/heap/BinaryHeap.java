package heap;

import java.util.Comparator;

public class BinaryHeap <E> extends AbstrctHeap<E>{
    //==============================================属性====================================================================
    private E[] elements;
    private static final  int DEFAULT_CAPACITY =10;

    //==============================================构造方法====================================================================


    public BinaryHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);

        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = elements.length;
            int capacity = Math.max(elements.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < elements.length; i++) {
                this.elements[i] = elements[i];
            }
            heapify();
        }
    }

    /**
     * 批量建堆
     */
    private void heapify() {
        //非叶子节点的数量
        int halfIndex = (size>>1)-1;
        for (int i =halfIndex;i>=0;i--){
            siftDown(i);
        }
    }

    public BinaryHeap(Comparator comparator){
        super(comparator);
    }
    public BinaryHeap(){
       super();
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }
    public BinaryHeap(E[] elements){
       this(elements,null);
    }

    //==============================================TODO:接口逻辑====================================================================
    @Override
    public void clear() {
        size = 0;
        for (int i=0;i<elements.length;i++){
            elements[i] = null;
        }
    }

    /**
     * 增加
     * @param element
     */
    @Override
    public void add(E element) {
        //合法性检测  扩容
        elementNotNullCheck(element);
        ensureCapacity(size +1);
        //添加元素
        elements[size++] = element;
        siftUp(size -1);
    }

    /**
     * 上滤操作
     * @param index
     */
    private void siftUp(int index) {

        E element = elements[index];
        /**
         * 基本过程是 比较添加元素和父元素的大小
         * 如果父元素比他小就进行交换，然后重复这个过程
         * 直到达到堆顶
         */
        while (index >0){
           //父元素下标
           int parentIndex = (index -1)>>1; //java中默认是floor
            E parent = elements[parentIndex];

           if (compare(element,parent) <=0) break;

           //TODO:思考一下如何进行优化的，减少了交换次数
           //将父元素先拿下来
            elements[index] = parent;

           index = parentIndex;
       }
        //最后再进行统一的交换，提高效率
        elements[index] = element;


    }


    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();
        E element = elements[0];
        elements[0] = elements[--size];
        siftDown(0);
        return element;
    }

    private void siftDown(int index){
        //获取要替换的元素
        E element = elements[index];
        //当前元素要和子节点元素进行大小的比较，那么它首先必须具备子元素【即它是非叶子节点】
        //非叶子节点的数量
        int half = size>>1;
        while (index < half){
            //index有两种情况
            //1.只有左子节点 2.有两个子节点
            //默认左子节点元素比较大
            int childIndex = (index<<1)+1;
            E child = elements[childIndex];

            //右子节点
            int rightIndex = childIndex +1;

            //选出较大的子元素
            if (rightIndex <size && compare(elements[rightIndex],child)>0){
                child = elements[rightIndex];
            }
            if (compare(element,child)>=0)break;

            //将子节点元素放入父节点位置
            elements[index] = child;
            //更新index
            index = childIndex;
        }
        elements[index] = element;
    }
    /**
     * 下滤操作
     *
     * 基本过程：
     * 1. 获得index位置元素
     * 2. 不断和子节点进行比较直到没有子节点为止
     */
//    private void siftDown(int index) {
//        E element = elements[index];
//        //必须保证有子节点 也就是当前节点一定是非叶子节点
//        while (( (index<<1)+1)<size){
//            //找出比较大的子元素
//            int leftIndex = (index<<1)+1;
//            int rightIndex = (index<<1)+2;
//            E leftElement = elements[leftIndex];
//            E rightElement =rightIndex <size ? elements[rightIndex]:null;
//
//            int cmp = (rightElement ==null)?1 :compare(leftElement,rightElement);
//            E maxElement = cmp >=0 ?leftElement:rightElement;
//
//            if (compare(element,maxElement)>=0)break;
//            //比较更新index
//            elements[index] = maxElement;
//            index = cmp>=0 ?leftIndex:rightIndex;
//        }
//        elements[index] = element;
//
//    }
//
//    private void siftDown(int index) {
//        E element = elements[index];
//        int half = size >> 1; // 非叶子节点的数量
//        // 第一个叶子节点的索引 == 非叶子节点的数量
//        // index < 第一个叶子节点的索引
//        // 必须保证index位置是非叶子节点
//        while (index < half) {
//            // index的节点有2种情况
//            // 1.只有左子节点
//            // 2.同时有左右子节点
//
//            // 默认为左子节点跟它进行比较
//            int childIndex = (index << 1) + 1;
//            E child = elements[childIndex];
//
//            // 右子节点
//            int rightIndex = childIndex + 1;
//
//            // 选出左右子节点最大的那个
//            if (rightIndex < size && compare(elements[rightIndex], child) > 0) {
//                child = elements[childIndex = rightIndex];
//            }
//
//            if (compare(element, child) >= 0) break;
//
//            // 将子节点存放到index位置
//            elements[index] = child;
//            // 重新设置index
//            index = childIndex;
//        }
//        elements[index] = element;
//    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        //TODO:注意堆可能是空的
        E root =null;
        if (size ==0){
            elements[0] =element;
            size++;
        }else{
            //直接覆盖，然后进行下滤操作 不要进行调用remove操作然后再调用add操作 浪费性能
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return root;
    }

    //==============================================TODO:辅助方法====================================================================
    private void elementNotNullCheck(E element) {
        if (element ==null)throw new IllegalArgumentException("element must not be null");
    }
    private void emptyCheck(){
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty");
        }
    }
    private void ensureCapacity(int newCapacity){
        int oldCapacity = elements.length;
        if (oldCapacity >=newCapacity)return;
        //扩容1.5倍
        oldCapacity = oldCapacity +(oldCapacity>>1);
        E[] oldElements = elements;
        elements = (E[]) new Object[oldCapacity];
        for (int i=0;i<oldElements.length;i++){
            elements[i] = oldElements[i];
        }
    }

}
