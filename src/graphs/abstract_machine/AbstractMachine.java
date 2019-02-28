package graphs.abstract_machine;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The Machine has full control over the
 *
 * Creator: Patrick
 * Created: 26.02.2019
 * Purpose:
 */
public abstract class AbstractMachine<In, Acc, Out> {
    private final State<Acc, Out> _initialState;

    protected AbstractMachine(@NotNull State<Acc, Out> initialState) {
        _initialState = initialState;
    }

    public abstract Out process(In input);

    private Transition<Acc, Out> transition(State<Acc, Out> state, Acc input, Out buffer) throws StateViolation {
        var transitions = state.getTransitions();

        try {
            for (TransitionFunction<Acc, Out> transition : transitions) {
                var optionalState = transition.utilize(input, buffer);

                if (optionalState.isPresent()) {
                    return optionalState.get();
                }
            }
        } catch (MachineTermination ex) {
            throw new StateTermination();
        }

        throw new StateViolation("Cannot reach final state");
    }

}
