package com.yz.singleLinkedList;

/**
 * 带虚拟头节点的单链表
 */
public class SingleLinkedList2<E> extends AbstractList<E>{
    private Node<E> first;

    private static class Node<E>{
        private E element;
        private Node<E> next;
        public Node(E element, Node next){
            this.element = element;
            this.next =next;
        }
    }

    private SingleLinkedList2(){
        this.first = new Node<>(null,null);
    }
    @Override
    public void clear() {
        size = 0;
        first.next =null;

    }

    @Override
    public void add(E element) {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {
        //1.合法性判断
        rangeCheckForAdd(index);
        //2. 将节点放入指定位置

        Node<E> preNode = node(index-1);
        Node<E> newNode = new Node<>(element,preNode.next);
        preNode.next = newNode;

    }

    @Override
    public E remove(int index) {
        //合法性判断
        rangeCheck(index);
        //删除index位置的节点
        Node<E> prevNode = node(index-1);
        E element = prevNode.next.element;
        prevNode.next = prevNode.next.next;
        return element;
    }

    @Override
    public int indexOf(E element) {
        return 0;
    }

    private Node<E> node(int index){
        //1.合法性判断
        rangeCheck(index);
        //2. 取出头节点
        Node<E> node = first.next;
        for(int i =1;i<=index;i++){
            node = node.next;
        }
        return node;
    }
}
