package graphs.abstract_machine;

import essentials.annotations.Package;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Creator: Patrick
 * Created: 07.03.2019
 * Purpose:
 */
@Package class BufferQueue<T> implements Iterable<T>{
    protected final int _bufferSize;
    private final Deque<T> _queue;
    protected final Iterator<T> _source;

    public BufferQueue(int bufferSize, Iterator<T> source) {
        _bufferSize = bufferSize;
        _source = source;
        _queue = new ArrayDeque<>(bufferSize);
    }

    public T pop() {
        if (_queue.isEmpty()) {
            refill();
        }

        return _queue.pop();
    }

    public T peek() {
        if (_queue.isEmpty()) {
            refill();
        }

        return _queue.peekFirst();
    }

    public boolean hasNext() {
        return !_queue.isEmpty() || _source.hasNext();
    }

    private void refill() {
        for (int i = 0; i != _bufferSize && _source.hasNext(); ++i) {
            T next = _source.next();
            _queue.addLast(next);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return _queue.iterator();
    }
}
