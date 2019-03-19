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
public abstract class AbstractMachine<Acc, Out> {
    protected final State<Acc, Out> _initialState;

    protected AbstractMachine(@NotNull State<Acc, Out> initialState) {
        _initialState = initialState;
    }

    /**
     * @return always returns a transition, never returns null.
     * @throws StateViolation if no transition to another state is possible.
     */
    protected Transition<Acc, Out> transition(State<Acc, Out> state,
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

    protected Transition<Acc, Out> resolveFallback(State<Acc, Out> state, Acc input, Out buffer) {
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
