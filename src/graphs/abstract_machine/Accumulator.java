package graphs.abstract_machine;

import java.util.function.BiFunction;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
@FunctionalInterface
public interface Accumulator<Acc, Buffer> extends BiFunction<Acc, Buffer, Buffer> {

    Buffer apply(Acc t, Buffer u) throws RuntimeException;

    default Buffer noOp(Acc acc, Buffer buffer) {
        return buffer;
    }
}
