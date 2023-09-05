public interface Queue<E> {
    int size();	// 元素的数量
    boolean isEmpty();	// 是否为空
    void enQueue(E element);	// 入队
    E deQueue();	// 出队
    E front();	// 获取队列的头元素
    void clear();	// 清空
}
