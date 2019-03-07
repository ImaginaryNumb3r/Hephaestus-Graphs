package graphs.abstract_machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Patrick Plieschnegger
 */
/*package*/ class State<ACC, DATA> {
    private final List<TransitionFunction<ACC, DATA>> _transitions;
    private final Object _id;

    public State(Object id) {
        this(new ArrayList<>(), id);
    }

    public State(List<TransitionFunction<ACC, DATA>> transitions, Object id) {
        _transitions = transitions;
        _id = id;
    }

    public List<TransitionFunction< ACC, DATA>> getTransitions() {
        return _transitions;
    }

    public void addTransition(TransitionFunction<ACC, DATA> transition) {
        _transitions.add(transition);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof State)) return false;
        State other = (State) obj;

        return _id == other._id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
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
