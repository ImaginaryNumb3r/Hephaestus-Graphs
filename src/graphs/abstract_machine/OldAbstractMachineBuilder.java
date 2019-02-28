package graphs.abstract_machine;

import graphs.abstract_machine.lib.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author Patrick Plieschnegger
 */
public class OldAbstractMachineBuilder<ID, ACC, DATA> {
    private static final String STATE_EXCEPTION_MESSAGE = "States must be declared before they are used in transitions.";
    protected final Map<ID, State<ACC, DATA>> _states;
    protected State<ACC, DATA> _initialState;

    public OldAbstractMachineBuilder(ID initialIdentifier) {
        _states = new HashMap<>();
        State<ACC, DATA> initialState = new State<>();

        _states.put(initialIdentifier, initialState);
        _initialState = initialState;
    }

    public void addStates(ID... identifiers) {
        for (ID id : identifiers) {
            addStates(id);
        }
    }

    private BiPredicate<ACC, DATA> simplify(Predicate<ACC> predicate) {
        return (ch, buff) -> predicate.test(ch);
    }

    /**
     * Adds a state with the given id.
     * @param identifier for the state.
     */
    public void addStates(ID identifier) {
        _states.putIfAbsent(identifier, new State<>());
    }

    /**
     * Performs an action on the current state without. Does not change to another state.
     * @param stateID the concerning state.
     * @param processing the action to perform on the current state.
     */
    public void addHardReflection(ID stateID, BiFunction<ACC, DATA, DATA> processing) {
        addTransition(stateID, stateID, (ch, buff) -> true, processing);
    }

    /**
     * Performs an action on the current state if the requirements are set. Does not change to another state.
     * @param stateID the concerning state.
     * @param condition defining the requirements.
     * @param processing the action to perform on the current state.
     */
    public void addReflection(ID stateID, BiPredicate<ACC, DATA> condition, BiFunction<ACC, DATA, DATA> processing) {
        addTransition(stateID, stateID, condition, processing);
    }

    /**
     * Changes to another state. No action is performed.
     * @param startID the start state.
     * @param targetID the target state.
     */
    public void addHardTransition(ID startID, ID targetID) {
        addTransition(startID, targetID, (ch, buff) -> true, (ch, buff) -> buff);
    }

    /**
     * Changes to another state if the requirements are set. No action is performed.
     * @param startID the start state.
     * @param targetID the target state.
     * @param condition defining the requirements.
     */
    public void addSoftTransition(ID startID, ID targetID, BiPredicate<ACC, DATA> condition) {
        addTransition(startID, targetID, condition, (ch, buff) -> buff);
    }

    public void addTransition(ID startID, ID targetID, BiPredicate<ACC, DATA> condition,
                              BiFunction<ACC, DATA, DATA> processing) {
        State<ACC, DATA> start = _states.get(startID);
        State<ACC, DATA> target = _states.get(targetID);

        if (start == null || target == null) throw new IllegalStateException(STATE_EXCEPTION_MESSAGE);

        start.addTransition(target, condition, processing);
    }

    public void addViolation(ID startID, BiPredicate<ACC, DATA> condition) {
        State<ACC, DATA> start = _states.get(startID);
        if (start == null) throw new IllegalStateException(STATE_EXCEPTION_MESSAGE);

        start.addTransition(TransitionFunction.ofViolation(start, condition));
    }

    public void setInitialState(State<ACC, DATA> state) {
        _initialState = state;
    }

    public StateMachine<DATA> construct(Iterable<ACC> source) {
        if (_initialState == null) {
            throw new IllegalArgumentException("The initial state must be set before an abstract machine can be constructed.");
        }
        return input -> {
            State<ACC, DATA> state = _initialState;
            DATA data = null;

            try {
                for (ACC element : source) {
                    State<ACC, DATA> target = state.transition(element, input);

                    state = target;
                    data = target.getValue();
                }
            } catch (StateViolation ex) {
                // Stop machine.
            }

            return data;
        };
    }
}
