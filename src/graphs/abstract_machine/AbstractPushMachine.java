package graphs.abstract_machine;

import graphs.abstract_machine.exception.MachineTermination;
import graphs.abstract_machine.exception.StateViolation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Creator: Patrick
 * Created: 19.03.2019
 * Purpose:
 */
public class AbstractPushMachine<Acc, Out> extends AbstractMachine<Acc, Out> implements PushdownAutomaton<Acc, Out> {
    private State<Acc, Out> _currentState;
    private Status _status;
    private Out _data;

    protected AbstractPushMachine(@NotNull State<Acc, Out> initialState, Out empty) {
        super(initialState);
        _status = Status.RUNNING;
        _currentState = _initialState;
        _data = empty;
    }

    @Override
    public Status process(Acc input) {
        if (_status != Status.RUNNING) {
            throw new IllegalStateException("Cannot continue parsing with status: " + _status);
        }

        try {
            var endPoint = transition(_currentState, input, _data);

            _currentState = endPoint._state;
            _data = endPoint._data;
        } catch (MachineTermination signal) {
            _status = Status.HALTED;
        } catch (StateViolation signal) {
            _status = Status.VIOLATED;
        }

        return _status;
    }

    @Override
    public Out getData() {
        if (_status == Status.VIOLATED) {
            throw new IllegalStateException("Cannot getData on violated machine");
        }

        return _data;
    }
}
