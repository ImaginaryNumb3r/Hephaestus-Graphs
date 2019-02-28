package graphs.abstract_machine;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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

    public AbstractMachine<ACC, DATA> construct() {
        if (_initialState == null) {
            throw new IllegalStateException("An initial state must be set before an abstract machine can be constructed.");
        }

        return new AbstractMachine<>(_initialState);
    }

    public void addState(ID identifier) {
        _states.putIfAbsent(identifier, new State<>());
    }

    /**
     * Moves from the start state to the targeted state if the condition is met.
     * Processes the data via the specified function.
     */
    public void addTransition(@NotNull ID startID, @NotNull ID targetID,
                              @NotNull BiPredicate<ACC, DATA> condition,
                              @NotNull Accumulator<ACC, DATA> processing)
    {
        var start = getOrAdd(startID);
        var target = getOrAdd(targetID);
        var transition = TransitionFunction.ofStates(target, condition, processing);

        start.addTransition(transition);
    }

    /**
     * Moves from the start state to the targeted state if the condition is met.
     * Processes the data via the specified function.
     */
    public void addTransition(@NotNull ID startID, @NotNull ID targetID,
                              @NotNull Predicate<ACC> condition,
                              @NotNull Accumulator<ACC, DATA> processing)
    {
        addTransition(startID, targetID, (ch, buff) -> condition.test(ch), processing);
    }

    /**
     * Moves from the start state to the targeted state if the condition is met.
     */
    public void addSoftTransition(@NotNull ID startID, @NotNull ID targetID, @NotNull Predicate<ACC> condition) {
        addTransition(startID, targetID, (ch, buff) -> condition.test(ch), (ch, buff) -> buff);
    }

    /**
     * Moves from the start state to the targeted state if the condition is met.
     */
    public void addSoftTransition(@NotNull ID startID, @NotNull ID targetID, @NotNull BiPredicate<ACC, DATA> condition) {
        addTransition(startID, targetID, condition, (ch, buff) -> buff);
    }

    /**
     * Does not change the state if condition is met. Processes the data via the specified function.
     */
    public void addReflection(@NotNull ID stateID, @NotNull BiPredicate<ACC, DATA> condition, @NotNull Accumulator<ACC, DATA> processing) {
        addTransition(stateID, stateID, condition, processing);
    }

    /**
     * Does not change the state if condition is met. Processes the data via the specified function.
     */
    public void addReflection(@NotNull ID stateID, @NotNull Predicate<ACC> condition, @NotNull Accumulator<ACC, DATA> processing) {
        addTransition(stateID, stateID, (ch, buff) -> condition.test(ch), processing);
    }

    /**
     * Does not change the state if condition is met. Processes the data via the specified function.
     */
    public void addMachine(@NotNull ID startID,@NotNull ID targetID,
                           @NotNull BiPredicate<ACC, DATA> condition,
                           @NotNull AbstractMachine<ACC, DATA> machine)
    {
        var start = getOrAdd(startID);
        var target = getOrAdd(targetID);
        var transition = TransitionFunction.ofMachine(target, condition, machine);

        start.addTransition(transition);
    }

    /**
     * Does not change the state if condition is met. Processes the data via the specified function.
     */
    public void addMachine(@NotNull ID startID,@NotNull ID targetID,
                           @NotNull Predicate<ACC> condition,
                           @NotNull AbstractMachine<ACC, DATA> machine)
    {
        addMachine(startID, targetID, (ch, buff) -> condition.test(ch), machine);
    }

    /**
     * Stops the machine if the requirements are met. Also stops all supermachines.
     */
    public void addViolation(@NotNull ID stateId, @NotNull BiPredicate<ACC, DATA> condition) {
        var state = getOrAdd(stateId);
        var transition = TransitionFunction.ofViolation(state, condition);

        state.addTransition(transition);
    }

    /**
     * Stops the machine if the requirements are met. Also stops all supermachines.
     */
    public void addViolation(@NotNull ID stateId, @NotNull Predicate<ACC> condition) {
        var state = getOrAdd(stateId);
        var transition = TransitionFunction.ofViolation(state, (ch, buff) -> condition.test(ch));

        state.addTransition(transition);
    }

    /**
     * Stops the machine if the requirements are met. Supermachines are not terminated.
     */
    public void addTermination(@NotNull ID stateId, @NotNull BiPredicate<ACC, DATA> condition) {
        var state = getOrAdd(stateId);
        var transition = TransitionFunction.ofTermination(state, condition);

        state.addTransition(transition);
    }

    /**
     * Stops the machine if the requirements are met. Supermachines are not terminated.
     */
    public void addTermination(@NotNull ID stateId, @NotNull Predicate<ACC> condition) {
        var state = getOrAdd(stateId);
        var transition = TransitionFunction.ofTermination(state, (ch, buff) -> condition.test(ch));

        state.addTransition(transition);
    }

    /**
     * Returns the corresponding state to the given ID.
     * Throws exception if no state exists for the given ID.
     * TODO: Remove
     */
    private State<ACC, DATA> getOrAdd(ID stateID) {
        _states.putIfAbsent(stateID, new State<>());

        return _states.get(stateID);
    }
}
