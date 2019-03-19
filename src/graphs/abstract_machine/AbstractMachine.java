package graphs.abstract_machine;

import graphs.abstract_machine.exception.MachineTermination;
import graphs.abstract_machine.exception.StateViolation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The Machine has full control over the
 *
 * Creator: Patrick
 * Created: 26.02.2019
 * Purpose:
 */
public class AbstractMachine<Acc, Out> implements MachineExecutor<Acc, Out> {
    private final State<Acc, Out> _initialState;

    protected AbstractMachine(@NotNull State<Acc, Out> initialState) {
        _initialState = initialState;
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

    /**
     * @return always returns a transition, never returns null.
     * @throws StateViolation if no transition to another state is possible.
     */
    private Transition<Acc, Out> transition(State<Acc, Out> state,
                                            Acc input,
                                            Out buffer
    ) throws StateViolation {
        var transitions = state.getTransitions();

        for (TransitionFunction<Acc, Out> transition : transitions) {
            var endPoint = transition.utilize(input, buffer);

            if (endPoint.isPresent()) {
                return endPoint.get();
            }
        }

        // If all normal transitions are exhausted, use the hard transition as default fallback.
        return resolveFallback(state, input, buffer);

    }

    private Transition<Acc, Out> resolveFallback(State<Acc, Out> state, Acc input, Out buffer) {
        if (state.hasHardTransition()) {
            var fallback = state.getHardTransition();
            Optional<Transition<Acc, Out>> endPoint = fallback.utilize(input, buffer);

            if (endPoint.isPresent()) {
                return endPoint.get();
            }
        }

        // If no hard transition is present or possible, an exhausted transition exception must be thrown.
        throw new StateViolation("Exhausted all transitions from state");
    }
}
