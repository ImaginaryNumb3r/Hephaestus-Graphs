package graphs.abstract_machine;

import essentials.annotations.Package;

/**
 * Creator: Patrick
 * Created: 27.02.2019
 * Purpose:
 */
@Package class Transition<Acc, Data> {
    public final State<Acc, Data> _state;
    public final Data _data;

    public Transition(State<Acc, Data> state, Data data) {
        _state = state;
        _data = data;
    }
}
