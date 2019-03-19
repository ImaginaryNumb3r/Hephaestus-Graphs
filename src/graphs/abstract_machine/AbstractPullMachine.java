package graphs.abstract_machine;

import graphs.abstract_machine.exception.MachineTermination;
import org.jetbrains.annotations.NotNull;

/**
 * Creator: Patrick
 * Created: 19.03.2019
 * Purpose:
 */
public class AbstractPullMachine<Acc, Out> extends AbstractMachine<Acc, Out> implements MachineExecutor<Acc, Out> {

    protected AbstractPullMachine(@NotNull State<Acc, Out> initialState) {
        super(initialState);
    }

    /**
     * While the method is running, the backing input must not be mutated.
     * @throws java.util.ConcurrentModificationException if the input iterable was mutated while this method is executing.
     */
    public Out process(Iterable<Acc> queue, Out start) {
        State<Acc, Out> state = _initialState;
        Out data = start;

        try {
            for (Acc input : queue) {
                var endPoint = transition(state, input, data);

                state = endPoint._state;
                data = endPoint._data;
            }
        } catch (MachineTermination fin) {
            // Terminate machine and return the data that was computed so far.
        }

        return data;
    }
}
