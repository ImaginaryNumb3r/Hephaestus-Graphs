package graphs.abstract_machine;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
@FunctionalInterface
public interface MachineExecutor<Acc, Data> {

    Data process(Iterable<Acc> inputStream, Data empty);

    default Data process(Acc[] inputStream, Data empty) {
        return process(Arrays.asList(inputStream), empty);
    }

    default Data process(Iterator<Acc> inputStream, Data empty) {
        return process(() -> inputStream, empty);
    }

}
