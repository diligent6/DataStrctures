package com.yzcoder.listSet.doubleLinkedList;

public class DoubleLinkedList<E> extends AbstractList<E> {
    private Node<E> first;
    private Node<E> last;


    // 节点内部类
    private static class Node<E>{
        private Node<E> prev;
        private E element;
        private Node<E> next;

        public Node(Node<E> prev,E element,Node<E> next){
            this.prev = prev;
            this.element = element;
            this.next = next;
        }
    }
    @Override
    public void clear() {
        size = 0;
        first =null;
        last = null;

    }

    @Override
    public void add(E element) {
        add(size,element);
    }

    @Override
    public E get(int index) {
        //合法性判断
        rangeCheck(index);
        //获取index位置的节点
        Node<E> node = node(index);

        return node.element;
    }

    @Override
    public E set(int index, E element) {
        //合法性判断
        rangeCheck(index);
        //获取index位置的节点
        Node<E> node = node(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    @Override
    public void add(int index, E element) {
        //1.合法性判断
        rangeCheckForAdd(index);
//        //2. 将节点放入指定的位置
//        //index =0 size=0
//        //或者添加到最后
//        if (index ==size){// 添加到最后
//            Node<E> oldLast =last;
//            last = new Node<E>(oldLast,element,null);
//            if (oldLast ==null){
//                first = last;
//            }else{
//                oldLast.next = last;
//            }
//        }else{
////            //
//            Node<E> next = node(index);
//            Node<E> prev = next.prev;
//            Node<E> newNode = new Node<>(prev,element,next);
//            //调整指向
//            next.prev = newNode;
//           if (prev ==null){
//               first = newNode;
//           }else{
//               prev.next = newNode;
//           }
//
//        }
        //2.将节点放入正确的位置
//        //1.  链表中一个元素都没有
//        if (size ==0){
//            Node<E> newNode =new Node<>(null,element,null);
//            first = last =newNode;
//        }else {
//         if (index ==size){
//             Node<E> newNode = new Node<>(last,element,null);
//             last.next = newNode;
//             last =newNode;
//         }else{
//             Node<E> next = node(index);
//             Node<E> prev = next.prev;
//             Node<E> newNode = new Node<>(prev,element,next);
//             if (prev ==null){
//                 first = newNode;
//             }else{
//                 prev.next = newNode;
//             }
//             next.prev = newNode;
//         }
//        }
        // index = 0 size =0
        if (index == size){//size==0 index ==size
            Node<E> oldLast = last;
            last = new Node<>(oldLast,element,null);
            if (oldLast ==null){// 链表的第一个元素
                first = last;
            }else{// 将元素放入最后
                oldLast.next = last;
            }
        }else{
            //
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<>(prev,element,next);
            //调整指向
            next.prev = newNode;
           if (prev ==null){
               first = newNode;
           }else{
               prev.next = newNode;
           }

        }

        //3.更新状态
        size++;
    }

    @Override
    public E remove(int index) {
        //1。合法性判断
        rangeCheck(index);
        //2.删除index位置的节点
        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if(prev ==null){// index =0
            first =next;
        }else{
          prev.next = next;
        }
        if (next ==null){// index =size-1
            last = prev;
        }else{
            next.prev =prev;
        }
        //3.更新状态
        size--;
        return node.element;
    }

    @Override
    public int indexOf(E element) {
        Node<E> node = first;
        if (element ==null){
            for (int i =0;i<size;i++){
                if (node.element ==null)return i;
                node = node.next;
            }
        }else{
            for (int i=0;i<size;i++){
                if (node.equals(element))return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 找到index位置的节点
     * @param index
     * @return
     */
    private Node<E> node(int index){
        rangeCheck(index);

        if (index <(size>>1)){ //index在左半部分 从前往后找
            Node<E> node = first;
            for(int i=0;i<=index;i++){
                node = node.next;
            }
            return node;
        }else{
            Node<E> node = last;
            for (int i=size-2;i>=index;i--){
                node = node.prev;
            }
            return node;
        }

    }

}
