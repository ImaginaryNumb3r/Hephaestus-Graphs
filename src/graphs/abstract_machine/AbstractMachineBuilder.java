package graphs.abstract_machine;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * Creator: Patrick
 * Created: 27.02.2019
 * Purpose:
 */
public class AbstractMachineBuilder<ID, ACC, DATA> {
    private final Map<ID, State<ACC, DATA>> _states;
    private State<ACC, DATA> _initialState;

    public AbstractMachineBuilder(ID initialIdentifier) {
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

    /**
     * Moves from the start state to the targeted state if the condition is met.
     * Processes the data via the specified function.
     * TODO: THe processing should also be a state machine.
     */
    public void addTransition(@NotNull ID startID, @NotNull ID targetID,
                              @NotNull BiPredicate<ACC, DATA> condition,
                              @NotNull BiFunction<ACC, DATA, DATA> processing)
    {
        var start = getOrThrow(startID);
        var target = getOrThrow(targetID);
        var transition = TransitionFunction.ofStates(target, condition, processing);

        start.addTransition(transition);
    }

    /**
     * Stops the machine if the requirements are met. Also stops all supermachines.
     */
    public void addViolation(@NotNull ID stateId, @NotNull BiPredicate<ACC, DATA> condition) {
        var state = getOrThrow(stateId);
        var transition = TransitionFunction.ofViolation(state, condition);

        state.addTransition(transition);
    }

    /**
     * Stops the machine if the requirements are met. Supermachines are not terminated.
     */
    public void addTermination(@NotNull ID stateId, @NotNull BiPredicate<ACC, DATA> condition) {
        var state = getOrThrow(stateId);
        var transition = TransitionFunction.ofTermination(state, condition);

        state.addTransition(transition);
    }

    /**
     * Returns the corresponding state to the given ID.
     * Throws exception if no state exists for the given ID.
     */
    private State<ACC, DATA> getOrThrow(ID stateID) {
        var state = _states.get(stateID);
        if (state == null) throw new IllegalStateException("States must be declared before they are used in transitions.");

        return state;
    }
}
