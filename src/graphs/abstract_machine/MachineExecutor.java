package graphs.abstract_machine;

import graphs.abstract_machine.exception.StateViolation;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
@FunctionalInterface
public interface MachineExecutor<Acc, Data> {

    Data process(Iterable<Acc> inputStream, Data empty) throws StateViolation;

    default Data process(Acc[] inputStream, Data empty) throws StateViolation {
        return process(Arrays.asList(inputStream), empty);
    }

    default Data process(Iterator<Acc> inputStream, Data empty) throws StateViolation {
        return process(() -> inputStream, empty);
    }

}
