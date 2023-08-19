package com.jiaran.queue.linkedQueue.doublequeue;

public class DoubleCircleLinkedList<E> extends AbstractList<E> {
    private Node<E> first;
    private Node<E> last;

    private static class Node<E> {
        private Node<E> prev;
        private Node<E> next;
        private E element;

        public Node(Node<E> prev,E element,Node<E> next){
            this.prev = prev;
            this.element = element;
            this.next = next;
        }
    }
    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public void add(E element) {

        add(size,element);
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        E old = node(index).element;
        node(index).element = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        //1.合法性检测
        rangeCheckForAdd(index);
        //2. 将节点添加到指定位置
        if (index ==size) {//添加到最后
            Node<E> oldLast = last;
            last  = new Node<E>(oldLast,element,first);

            if (oldLast ==null){// 当前元素是链表的第一个元素
                first = last;
                last.next = first; // 更新节点的next指向 保持循环性
        }else{
                oldLast.next = last;
            }
        }else{
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode =new Node<>(prev,element,next);

            next.prev = newNode;
            prev.next = newNode;
            if (newNode.next ==newNode.prev){
                first = newNode;
            }
        }
        size++;
    }

    @Override
    public E remove(int index) {
        return remove(node(index));
    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == element)
                    return i;
                node = node.next;
            }
        } else {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element.equals(element))
                    return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;

    }

    private E remove(Node<E> node){

        if (size==1){
            first =null;
            last=null;
        }else{
            //正常调整指向
            node.prev.next = node.next;
            node.next.prev = node.prev;

            //判断是否需要更新first 和last指向
            if (first ==node){
                first = node.next;
            }
            if (last ==node){
                last = node.prev;
            }
        }
        size--;
        return node.element;
    }


    private Node<E> node(int index){
        rangeCheck(index);

        if (index< (size>>1)){
            Node<E> node =first;
            for (int i =1;i<=index;i++){
                node =node.next;
            }
            return node;
        }else{
            Node<E> node =last;
            for (int i=size-2;i>=index;i++){
                node = node.prev;
            }
            return node;

        }


    }
}
