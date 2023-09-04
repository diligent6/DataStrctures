package com.yzcoder.treeset;

import com.yzcoder.Set;
import com.yzcoder.treeset.rbtree.BinaryTree;
import com.yzcoder.treeset.rbtree.RBT;

/**
 * 基于红黑树实现集合
 * @param <E>
 */
public class TreeSet <E> implements Set<E> {
    private RBT<E> tree = new RBT<>();
    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }


    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inOrder(new BinaryTree.Visitor<E>() {
            @Override
            public boolean visit(E element) {
                return visitor.visit(element);
            }
        });
    }
}
