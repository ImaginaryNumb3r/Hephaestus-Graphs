package graphs.abstract_machine;

/**
 * @author Patrick Plieschnegger
 */
public interface PushdownAutomaton <Acc, Data> {

    Status process(Acc input);

    Status getStatus();

    Data getData();

    enum Status {
        RUNNING, HALTED, VIOLATED
    }
}
