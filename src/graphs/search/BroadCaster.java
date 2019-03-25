package graphs.search;

import java.util.function.Function;

/**
 * @author Patrick Plieschnegger
 */
@FunctionalInterface
public interface BroadCaster<T> extends Function<T, Iterable<T>> {

    Iterable<T> apply(T source);

}
