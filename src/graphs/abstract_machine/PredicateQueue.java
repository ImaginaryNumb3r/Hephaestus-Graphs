package graphs.abstract_machine;

import java.util.function.Predicate;

/**
 * Creator: Patrick
 * Created: 09.03.2019
 * Purpose:
 */
public class PredicateQueue<T> extends BufferQueue<T> {
    private final Predicate<T> _condition;

    public PredicateQueue(BufferQueue<T> input, Predicate<T> condition) {
        super(input._bufferSize, input._source);
        _condition = condition;
    }

    @Override
    public boolean hasNext() {
        T next = peek();
        return _condition.test(next) && super.hasNext();
    }
}
