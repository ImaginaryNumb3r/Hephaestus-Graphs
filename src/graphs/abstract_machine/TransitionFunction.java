package graphs.abstract_machine;

import essentials.contract.Contract;
import graphs.abstract_machine.exception.MachineTermination;
import graphs.abstract_machine.exception.StateViolation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author Patrick Plieschnegger
 */
/*package*/ interface TransitionFunction<ACC, DATA> {

    /**
     * @param input that must have at least one element remaining.
     * @return empty optional if the transition was not applicable.
     */
    Optional<Transition<ACC, DATA>> utilize(Iterator<ACC> input, DATA buffer);

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofStates(State<ACC, DATA> target,
                                                              BiPredicate<ACC, DATA> condition,
                                                              Accumulator<ACC, DATA> processing) {
        Contract.checkNulls(target, condition, processing);

        return (stream, buffer) -> {
            Optional<Transition<ACC, DATA>> optionalState = Optional.empty();
            ACC input = stream.next(); // TODO: Doesnt work because every check that might fail is also a mutating operation.

            if (condition.test(input, buffer)) {
                // TODO: The Iterator should only
                DATA data = processing.apply(input, buffer);
                optionalState = Optional.of(new Transition<>(target, data));
            }

            return optionalState;
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofViolation(@NotNull State<ACC, DATA> source, @NotNull BiPredicate<ACC, DATA> condition) {
        Contract.checkNulls(source, condition);

        return (stream, buffer) -> {
            ACC input = stream.next();

            if (condition.test(input, buffer)) {
                throw new StateViolation();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofTermination(@NotNull State<ACC, DATA> source, @NotNull BiPredicate<ACC, DATA> condition) {
        Contract.checkNulls(source, condition);

        return (stream, buffer) -> {
            ACC input = stream.next();

            if (condition.test(input, buffer)) {
                throw new MachineTermination();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofMachine(State<ACC, DATA> target,
                                                       BiPredicate<ACC, DATA> condition,
                                                       MachineExecutor<ACC, DATA> machine)
    {
        Contract.checkNulls(target, condition);

        return (stream, buffer) -> {
            DATA output = machine.process(() -> stream, buffer);

            // TODO: What if the inner machine halts/is violated?
            return Optional.of(new Transition<>(target, output));
        };
    }
}
