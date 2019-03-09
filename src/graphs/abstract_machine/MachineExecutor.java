package graphs.abstract_machine;

import essentials.contract.NoImplementationException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiPredicate;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
@FunctionalInterface
public interface MachineExecutor<Acc, Data> {

    Data process(BufferQueue<Acc> inputStream, Data empty);

    default Data process(BufferQueue<Acc> inputStream, Data empty, BiPredicate<Acc, Data> terminationCondition) {
        throw new NoImplementationException();
    }

    default Data process(Iterable<Acc> inputStream, Data empty) {
        var iterator = inputStream.iterator();
        final var queue = new BufferQueue<>(100, iterator);

        return process(queue, empty);
    }

    default Data process(Acc[] inputStream, Data empty) {
        return process(Arrays.asList(inputStream), empty);
    }

    default Data process(Iterator<Acc> inputStream, Data empty) {
        return process(() -> inputStream, empty);
    }

}
