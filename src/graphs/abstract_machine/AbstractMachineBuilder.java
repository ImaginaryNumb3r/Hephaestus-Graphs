package graphs.abstract_machine;

import essentials.functional.Predicates;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Creator: Patrick
 * Created: 27.02.2019
 * TODO: We should expose the state directly and give more direct control over it. This allows to change the order of transitions.
 */
public class AbstractMachineBuilder<ID, ACC, DATA> {
    private final Map<ID, State<ACC, DATA>> _states;
    private State<ACC, DATA> _initialState;

    public AbstractMachineBuilder(ID initialIdentifier) {
        _states = new HashMap<>();
        State<ACC, DATA> initialState = new State<>(initialIdentifier);

        _states.put(initialIdentifier, initialState);
        _initialState = initialState;
    }

    public MachineExecutor<ACC, DATA> construct() {
        if (_initialState == null) {
            throw new IllegalStateException("An initial state must be set before an abstract machine can be constructed.");
        }

        return new AbstractMachine<>(_initialState);
    }

    protected State<ACC, DATA> getInitialState() {
        return _initialState;
    }


    //<editor-fold desc="Transitions">
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
    //</editor-fold>

    //<editor-fold desc="Soft Transitions">
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
    //</editor-fold>

    //<editor-fold desc="Reflections">
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
    public void setDefaultTransition(@NotNull ID stateID, @NotNull Accumulator<ACC, DATA> processing) {
        setDefaultTransition(stateID, stateID, processing);
    }

    /**
     * Does not change the state if condition is met. Processes the data via the specified function.
     */
    public void setDefaultTransition(@NotNull ID startID, @NotNull ID targetID, @NotNull Accumulator<ACC, DATA> processing) {
        var start = getOrAdd(startID);
        var target = getOrAdd(targetID);
        var transition = TransitionFunction.defaultTransition(target, processing);

        start.setDefaultTransition(transition);
    }
    //</editor-fold>

    //<editor-fold desc="Violations and Terminations">
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
     * Stops the machine if the requirements are met. Supermachines are not terminated.
     */
    public void addTermination(@NotNull ID stateId) {
        var state = getOrAdd(stateId);
        var transition = TransitionFunction.ofTermination(state, Predicates::alwaysTrue);

        state.addTransition(transition);
    }
    //</editor-fold>

    /**
     * Returns the corresponding state to the given ID.
     * Throws exception if no state exists for the given ID.
     */
    private State<ACC, DATA> getOrAdd(ID stateID) {
        _states.putIfAbsent(stateID, new State<>(stateID));

        return _states.get(stateID);
    }




    /*
    //<editor-fold desc="Sub Machines">
    public void addMachine(@NotNull ID startID, @NotNull ID targetID,
                           @NotNull BiPredicate<ACC, DATA> enterCondition,
                           @NotNull MachineExecutor<ACC, DATA> machine)
    {
        var start = getOrAdd(startID);
        var target = getOrAdd(targetID);
        var transition = TransitionFunction.ofMachine(target, enterCondition, null, machine);

        start.addTransition(transition);
    }

    public void addMachine(@NotNull ID startID, @NotNull ID targetID,
                           @NotNull Predicate<ACC> enterCondition,
                           @NotNull MachineExecutor<ACC, DATA> machine)
    {
        addMachine(startID, targetID, (ch, buff) -> enterCondition.test(ch), machine);
    }

    public void addMachine(@NotNull ID startID, @NotNull ID targetID,
                           @NotNull BiPredicate<ACC, DATA> condition,
                           @NotNull BiPredicate<ACC, DATA> exitCondition,
                           @NotNull MachineExecutor<ACC, DATA> machine)
    {
        var start = getOrAdd(startID);
        var target = getOrAdd(targetID);
        var transition = TransitionFunction.ofMachine(target, condition, null, machine);

        start.addTransition(transition);
    }

    public void addMachine(@NotNull ID startID, @NotNull ID targetID,
                           @NotNull Predicate<ACC> enterCondition,
                           @NotNull Predicate<ACC> exitCondition,
                           @NotNull MachineExecutor<ACC, DATA> machine)
    {
        addMachine(startID, targetID, (ch, buff) -> enterCondition.test(ch), machine);
    }
    //</editor-fold>
    */
}
