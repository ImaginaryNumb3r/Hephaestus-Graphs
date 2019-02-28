package graphs.node;

import collections.iteration.Iteration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose: A simple node which serves as basis for simple nodes in a graph.
 */
@SuppressWarnings("WeakerAccess")
public class GenericNode<T> implements Iterable<GenericNode<T>> {
    protected final List<GenericNode<T>> _children;
    protected T _value;

    public GenericNode(T value) {
        _children = new ArrayList<>();
        _value = value;
    }

    public void add(GenericNode<T>... nodes) {
        for (GenericNode<T> node : nodes) {
            add(node);
        }
    }

    public void add(GenericNode<T> node) {
        _children.add(node);
    }

    public T getValue() {
        return _value;
    }

    protected void setValue(T value) {
        _value = value;
    }

    @NotNull
    @Override
    public ListIterator<GenericNode<T>> iterator() {
        return _children.listIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericNode<?> that = (GenericNode<?>) o;
        return Objects.equals(_children, that._children) &&
                Objects.equals(_value, that._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_children, _value);
    }

    @Override
    public String toString() {
        return "Value: " + _value + " | Children: " + _children.size();
    }
}
