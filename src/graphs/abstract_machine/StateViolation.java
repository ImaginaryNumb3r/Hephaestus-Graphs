package graphs.abstract_machine;

/**
 * @author Patrick Plieschnegger
 */
public class StateViolation extends Exception {

    public StateViolation() {
        super();
    }

    public StateViolation(String message) {
        super(message);
    }

    public StateViolation(String message, Throwable cause) {
        super(message, cause);
    }
}
