package graphs.search;

import java.util.Deque;

/**
 * @author Patrick
 * @since 02.05.2017
 */
public class BreadthFirstSearch<T> implements GraphSearchStrategy<T>{

    @Override
    public void enqueue(Deque<T> nodeCollection, T node) {
        nodeCollection.addLast(node);
    }

    @Override
    public T dequeue(Deque<T> nodeCollection) {
        return nodeCollection.poll();
    }

    @Override
    public String toString() {
        return "Breadth First Search Strategy";
    }
}
