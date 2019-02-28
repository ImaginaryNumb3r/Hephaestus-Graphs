package graphs.abstract_machine;

import java.util.function.BiFunction;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
public interface Accumulator<Acc, Buffer> extends BiFunction<Acc, Buffer, Buffer> {

    default Buffer noOp(Acc acc, Buffer buffer) {
        return buffer;
    }
}
