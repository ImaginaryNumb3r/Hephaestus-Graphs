package graphs.abstract_machine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick Plieschnegger
 */
/*package*/ class State<ACC, DATA> {
    private final List<TransitionFunction<ACC, DATA>> _transitions;

    public State() {
        _transitions = new ArrayList<>();
    }

    public State(List<TransitionFunction<ACC, DATA>> transitions) {
        _transitions = transitions;
    }

    public List<TransitionFunction< ACC, DATA>> getTransitions() {
        return _transitions;
    }

    public void addTransition(TransitionFunction<ACC, DATA> transition) {
        _transitions.add(transition);
    }

    /*
    public void addTransition(State< ACC, DATA> target, BiPredicate<ACC, DATA> condition, BiFunction<ACC, DATA, DATA> processing) {
        _transitions.add(TransitionFunction.ofStates(target, condition, processing));
    }

    public Transition<ACC, DATA> transition(ACC input, DATA buffer) throws StateViolation {

        try {
            for (TransitionFunction<ACC, DATA> transition : _transitions) {
                var optionalState = transition.utilize(input, buffer);

                if (optionalState.isPresent()) {
                    return optionalState.get();
                }
            }
        } catch (MachineTermination ex) {

        }

        throw new StateViolation("Cannot reach final state");
    }*/
}
