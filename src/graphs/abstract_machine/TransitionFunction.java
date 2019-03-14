package graphs.abstract_machine;

import essentials.annotations.Package;
import essentials.contract.Contract;
import graphs.abstract_machine.exception.MachineTermination;
import graphs.abstract_machine.exception.StateViolation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @author Patrick Plieschnegger
 */
@Package interface TransitionFunction<ACC, DATA> {

    /**
     * @param input that must have at least one element remaining.
     * @return empty optional if the transition was not applicable.
     */
    Optional<Transition<ACC, DATA>> utilize(BufferQueue<ACC> input, DATA buffer) throws RuntimeException;

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofStates(State<ACC, DATA> target,
                                                                     BiPredicate<ACC, DATA> condition,
                                                                     Accumulator<ACC, DATA> processing
    ) {
        Contract.checkNulls(target, condition, processing);

        return (queue, buffer) -> {
            Optional<Transition<ACC, DATA>> state = Optional.empty();
            ACC input = queue.peek();

            if (condition.test(input, buffer)) {
                DATA data = processing.apply(input, buffer);
                state = Optional.of(new Transition<>(target, data));

                queue.pop();
            }

            return state;
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofMachine(State<ACC, DATA> target,
                                                               BiPredicate<ACC, DATA> enterCondition,
                                                               BiPredicate<ACC, DATA> exitCondition,
                                                               MachineExecutor<ACC, DATA> machine)
    {
        Contract.checkNulls(target, enterCondition, exitCondition, machine);

        return  (queue, buffer) -> {
            Optional<Transition<ACC, DATA>> state = Optional.empty();
            ACC input = queue.peek();

            if (enterCondition.test(input, buffer)) {
                DATA data = machine.process(queue, buffer);
                state = Optional.of(new Transition<>(target, data));

                // input element is not removed from queue as this wil be done in the machine itself.
            }

            return state;
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> defaultTransition(State<ACC, DATA> target,
                                                                       Accumulator<ACC, DATA> processing
    ) {
        Contract.checkNulls(target, processing);

        return (queue, buffer) -> {
            Optional<Transition<ACC, DATA>> state;
            ACC input = queue.pop();

            DATA data = processing.apply(input, buffer);
            state = Optional.of(new Transition<>(target, data));

            return state;
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofViolation(@NotNull State<ACC, DATA> source, @NotNull BiPredicate<ACC, DATA> condition) {
        Contract.checkNulls(source, condition);

        return (queue, buffer) -> {
            ACC input = queue.peek();

            if (condition.test(input, buffer)) {
                throw new StateViolation();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofTermination(@NotNull State<ACC, DATA> source,
                                                                   @NotNull BiPredicate<ACC, DATA> condition
    ) {
        Contract.checkNulls(source, condition);

        return (queue, buffer) -> {
            ACC input = queue.peek();

            if (condition.test(input, buffer)) {
                throw new MachineTermination();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }
}
