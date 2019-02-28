package graphs.abstract_machine.lib;

/**
 * @author Patrick Plieschnegger
 */
@FunctionalInterface
public interface Flattener<R, T> {

    Iterable<R> flatten(T data);

}
