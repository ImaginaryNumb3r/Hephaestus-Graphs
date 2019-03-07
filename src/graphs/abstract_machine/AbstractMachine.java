package graphs.abstract_machine;

import graphs.abstract_machine.exception.MachineTermination;
import graphs.abstract_machine.exception.StateViolation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * The Machine has full control over the
 *
 * Creator: Patrick
 * Created: 26.02.2019
 * Purpose:
 */
public class AbstractMachine<Acc, Out> implements MachineExecutor<Acc, Out> {
    private final State<Acc, Out> _initialState;
    private Iterable<Acc> _inputStream;
    private Out _data;

    protected AbstractMachine(@NotNull State<Acc, Out> initialState) {
        _initialState = initialState;
    }

    public Out process(Iterable<Acc> inputStream, Out start) {
        State<Acc, Out> state = _initialState;
        _inputStream = inputStream;
        _data = start;

        try {
            Iterator<Acc> iterator = _inputStream.iterator();
            while (iterator.hasNext()) {
                var endPoint = transition(state, iterator, _data);

                state = endPoint._state;
                _data = endPoint._data;
            }
        } catch (MachineTermination fin) {
            // Terminate machine and return the data that was computed so far.
        }

        return _data;
    }

    private Transition<Acc, Out> transition(State<Acc, Out> state,
                                            Iterator<Acc> inputStream,
                                            Out buffer
    ) throws StateViolation {
        var transitions = state.getTransitions();

        for (TransitionFunction<Acc, Out> transition : transitions) {
            var endPoint = transition.utilize(inputStream, buffer);

            if (endPoint.isPresent()) {
                return endPoint.get();
            }
        }

        throw new StateViolation("Exhausted all transitions from state");
    }
}
