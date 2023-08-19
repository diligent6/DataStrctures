package com.yz.singleLinkedList;

public class SingleLinkedList<E> extends AbstractList<E>{
    private Node<E> first;// 头节点

    private static class Node<E>{
        private E element;
        private Node<E> next;

        public Node(E element,Node next){
            this.element = element;
            this.next =next;
        }

    }


    /**
     * 清空元素
     */
    @Override
    public void clear() {
        size =0;
        first = null;
    }


    /**
     * 增加元素
     * @param element
     */
    @Override
    public void add(E element) {
        add(size,element);
    }

    @Override
    public E get(int index) {
        //合法性判断
        rangeCheck(index);
        //找到index位置的节点
        Node<E> node = node(index);

        return node.element;
    }

    @Override
    public E set(int index, E element) {
        //合法性判断
        rangeCheck(index);
        //找到index位置的节点
        Node<E> node = node(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    @Override
    public void add(int index, E element) {
        //1. 合法性判断
        rangeCheckForAdd(index);
        //2. 将节点放入正确位置
        if (index ==0){ //单独考虑第一个节点 她没有前驱节点
            Node<E> node = new Node<>(element,first);
            first.next =node;
        }else{
            //找到前驱节点
            Node<E> prevNode = node(index-1);
            // 创建新节点
            Node<E> newNode = new Node<>(element,prevNode.next);
            //调整指向
            prevNode.next = newNode;
        }
        //3. 更新状态
        size++;

    }

    /**
     * 删除index位置的节点并将其元素返回
     * @param index
     * @return
     */
    @Override
    public E remove(int index) {
        //1. 合法性判断
        rangeCheck(index);

        E element = null;
        //2. 删除index位置的节点
        if (index ==0){
             element = first.element;
            first.next = first.next;
        }else{
            //找到前驱节点
            Node<E> prevNode = node(index-1);
            //保存元素
             element = prevNode.next.element;
            //调整指向
            prevNode.next = prevNode.next.next;
        }
        return element;
    }

    @Override
    public int indexOf(E element) {
        //判断element 是否为null
        Node<E> node = first;
        if (element ==null){

            for (int i = 0;i<size;i++){
                if (node ==null) return i;
                node = node.next;
            }
        }else{
            for (int i =0 ;i<size;i++){
                if (node.element.equals(element))return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }


    /**
     * 获取index位置的节点
     *
     * @param index
     * @return
     */
    private Node<E> node(int index){
        //1. 合法性判断
        rangeCheck(index);
        //2. 取出头节点
        Node<E> node = first;
        for(int i =1; i<=index;i++){
            node = node.next;
        }
        return node;
    }
}
