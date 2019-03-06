package graphs.search;

import essentials.contract.Contract;
import essentials.util.HashGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Patrick
 * @since 01.05.2017
 */
@SuppressWarnings("WeakerAccess")
public class TreeTraverser<N extends Iterable<N>> implements Iterator<N> {
    protected final GraphSearchStrategy<N> _strategy;
    protected final Deque<N> _nodes;
    protected final N _root;
    protected N _current;

    protected TreeTraverser(N root, GraphSearchStrategy<N> strategy){
        _nodes = new ArrayDeque<>();
        _strategy = strategy;
        _root = root;
        _current = _root;

        strategy.enqueue(_nodes, _current);
    }

    /**
     * Creates a GraphIterator fromEntries the given root of a tree
     * @param source root of the tree
     * @param strategy to create a list based fromEntries the branching nodes of the root
     * @param <T> Type of root. Must be iterable to have access its children
     * @return GraphIterator<T> based fromEntries the given parameters
     */
    public static <T extends Iterable<T>> TreeTraverser<T> of(T source, @NotNull GraphSearchStrategy<T> strategy){
        Contract.checkNull(strategy);
        return new TreeTraverser<>(source, strategy);
    }

    @Override
    public boolean hasNext() {
        return !_nodes.isEmpty();
    }

    /**
     * Mutating operation which adds the children of the returned object to the queue.
     *
     * @return the next object from the queue.
     */
    @Override
    public N next() {
        if (!hasNext()) throw new NoSuchElementException();

        N next = _strategy.dequeue(_nodes);

        if (next != null) {
            next.forEach(node -> _strategy.enqueue(_nodes, node));
        }

        return next;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof TreeTraverser && obj.hashCode() == this.hashCode());
    }

    @Override
    public int hashCode() {
        return new HashGenerator(getClass())
                .appendObjs(_strategy, _root, _current, _nodes)
                .toHashCode();
    }
}
