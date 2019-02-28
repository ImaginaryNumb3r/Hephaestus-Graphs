package graphs.search;

import java.util.Deque;

/**
 * @author Patrick
 * @since 02.05.2017
 */
// TODO: Depth First doesn't work properly yet
public class DepthFirstSearch<T> implements GraphSearchStrategy<T> {

    @Override
    public void enqueue(Deque<T> nodeCollection, T node) {
        nodeCollection.addFirst(node);
    }

    @Override
    public T dequeue(Deque<T> nodeCollection) {
        return nodeCollection.poll();
    }

    @Override
    public String toString() {
        return "Depth First Search Strategy";
    }
}
