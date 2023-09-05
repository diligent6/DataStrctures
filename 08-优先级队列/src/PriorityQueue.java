import heap.BinaryHeap;
import heap.Heap;

/**
 * 优先级队列基于堆实现
 * @param <E>
 */

public class PriorityQueue<E> implements Queue<E>{
    private Heap<E> heap=new BinaryHeap();
    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * 入队
     * @param element
     */
    @Override
    public void enQueue(E element) {
        heap.add(element);
    }

    /**
     * 出队
     * @return
     */
    @Override
    public E deQueue() {
        return heap.remove();
    }

    /**
     * 队头元素
     * @return
     */
    @Override
    public E front() {
        return heap.get();
    }

    @Override
    public void clear() {
        heap.clear();
    }
}
