package com.yz.singlecirclelinkedlist;

/**
 * 单向循环链表
 * @param <E>
 */
public class SingleCircleLinkedList<E> extends AbstractList<E>{
    private Node<E> first;

    private static class Node<E> {
        private E element;
        private Node<E> next;
        public Node(E element,Node<E> next){
            this.element = element;
            this.next  =next;
        }
    }
    @Override
    public void clear() {
        //1.清空状态
        size =0;
        //2. 跳转头指针指向
        first = null;
    }

    @Override
    public void add(E element) {
        add(size,element);
    }

    @Override
    public E get(int index) {
        return node(index).element;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = node(index);
        E old = node.element;
        node.element = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        //1. 合法性检测
        rangeCheckForAdd(index);
        //2. 将节点放入到指定位置【单链表依赖于前驱节点】
        if (index ==0){// 位置在第一个节点
            Node<E> newNode = new Node<>(element,first);
//            first = newNode;
//            if (size ==0){
//                newNode.next =first;
//            }else{
//                Node<E> lastNode = node(size-1);
//                lastNode.next = first;
//            }
            //代码优化
            //先获得最后的节点
            // 因为找节点的过程需要用到first索引先不要修改first
            Node<E> lastNode = (size==0)?newNode:node(size-1);
            lastNode.next = newNode;
            first = newNode;
        }else{
            Node<E> prevNode = node(index-1);
            Node<E> newNode =  new Node<>(element,prevNode.next);
            prevNode.next = newNode;

        }
        //3.更新状态
        size++;
    }

    @Override
    public E remove(int index) {
        //1.合法性检测
        rangeCheck(index);
        //2. 删除index位置的节点
        if (index == 0){//  删除的是第一个节点
            E element = first.element;
            if (size ==1){
                first = null;
            }else{ //保持循环性
                Node <E> lastNode = node(size-1);
                lastNode.next = first;
                first = first.next;
            }
            return  element;
        }else{
            Node<E> prevNode = node(index-1);
            Node<E> node = prevNode.next;
            E element = node.element;
            prevNode.next = node.next;
            return element;
        }

    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == element) return i;
                node = node.next;
            }
        } else {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element.equals(element)) return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;

    }


    /**
     * 根据index找到对应的节点
     * @param index
     * @return
     */
    private Node<E> node(int index){
        //合法性检测
        rangeCheck(index);
        //遍历找到指定的节点
        Node<E> node = first;
        for (int i =1;i<=index;i++){
            node = node.next;
        }
        return node;
    }
}
