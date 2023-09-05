package com.yzcoder;

public class LinkedHashMap<K,V> extends HashMap<K,V>{

    //头尾指针
    private LinkedNode<K,V> first;
    private LinkedNode<K,V> last;
    private static class  LinkedNode<K,V> extends Node<K,V>{

        LinkedNode<K,V> prev;
        LinkedNode<K,V> next;

        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }
    }

    @Override
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        //创建节点的时候串线
        LinkedNode<K,V> linkedNode = new LinkedNode<>(key,value,parent);
        if (first ==null){
            first = last = linkedNode;
        }
        last.next = linkedNode;
        linkedNode.prev = last;
        last = linkedNode;

        return linkedNode;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        LinkedNode<K,V> node = first;
        for (int i =0;i<this.size();i++){
            if (visitor.visit(node.key,node.value))return;
            node = node.next;
        }
    }

    @Override
    protected void afterRemove(Node<K, V> willNode, Node<K, V> removeNode) {
        //处理度为2的节点，因为其是覆盖导致链表节点位置发生了变化 所以要将位置进行调换
        LinkedNode<K,V> node1 = (LinkedNode<K, V>) willNode;
        LinkedNode<K,V> node2 = (LinkedNode<K, V>) removeNode;
        if (node1 !=node2){//说明要进行调换
            LinkedNode<K,V> prevTemp = node1.prev;     //调换prev
            node1.prev = node2.prev;
            node2.prev = prevTemp;
            if (node1.prev ==null){
                first = node1;
            }else{
                node1.prev =node1;
            }
            if (node2.prev ==null){
                first = node2;
            }else{
                node2.prev = node2;
            }
            //调换next
            LinkedNode<K,V> nextTemp = node1.next;
            node1.next = node2.next;
            node2.next = nextTemp;
            if (node1.next ==null){
                last =node1;
            }else{
                node1.next.prev =node1;
            }
            if (node2.next ==null){
                last = node2;
            }else{
                node2.next.prev =node2;
            }
        }
        //删除逻辑
        LinkedNode<K,V> prev = node2.prev;
        LinkedNode<K,V> next = node2.next;

        if (prev ==null){
            first = next;
        }else{
            prev.next =next;
        }

        if (next ==null){
            last = prev;
        }else{
            next.prev = prev;
        }
    }
}
