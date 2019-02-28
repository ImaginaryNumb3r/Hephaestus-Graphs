package graphs.abstract_machine.lib;

/**
 * @author Patrick Plieschnegger
 */
public interface StateMachine<DATA> {

    DATA process(DATA input);

}
