package graphs.abstract_machine.lib;

import graphs.abstract_machine.State;

/**
 * @author Patrick Plieschnegger
 */
public class BiState<ACC, DATA> extends State<ACC, DATA> {
    private final DATA _value;

    public BiState(State<ACC, DATA> other, DATA value) {
        super(other.getTransitions());
        _value = value;
    }

    public DATA getValue() {
        return _value;
    }
}
