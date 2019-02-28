package graphs.abstract_machine;

import essentials.contract.Contract;
import graphs.abstract_machine.lib.BiState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * @author Patrick Plieschnegger
 */
interface TransitionFunction<ACC, DATA> {

    Optional<Transition<ACC, DATA>> utilize(ACC input, DATA buffer) throws StateViolation;

    static <ACC, DATA> DATA identity(ACC acc, DATA data) {
        return data;
    }

    static <ACC, DATA> boolean isTrue(ACC acc, DATA data) {
        return true;
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofStates(State<ACC, DATA> target,
                                                              BiPredicate<ACC, DATA> condition,
                                                              BiFunction<ACC, DATA, DATA> processing) {
        Contract.checkNulls(target, condition, processing);

        return (input, buffer) -> {
            Optional<Transition<ACC, DATA>> optionalState = Optional.empty();

            if (condition.test(input, buffer)) {
                DATA data = processing.apply(input, buffer);
                // TODO: Move the value here back to the abstract machine.
                optionalState = Optional.of(new Transition<>(target, data));
            }

            return optionalState;
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofViolation(@NotNull State<ACC, DATA> source, @NotNull BiPredicate<ACC, DATA> condition) {
        Contract.checkNulls(source, condition);

        return (input, buffer) -> {

            if (condition.test(input, buffer)) {
                throw new StateViolation();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }

    static <ACC, DATA> TransitionFunction<ACC, DATA> ofTermination(@NotNull State<ACC, DATA> source, @NotNull BiPredicate<ACC, DATA> condition) {
        Contract.checkNulls(source, condition);

        return (input, buffer) -> {

            if (condition.test(input, buffer)) {
                throw new MachineTermination();
            }

            return Optional.of(new Transition<>(source, buffer));
        };
    }
}
