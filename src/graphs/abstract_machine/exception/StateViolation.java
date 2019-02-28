package graphs.abstract_machine.exception;

/**
 * @author Patrick Plieschnegger
 */
public class StateViolation extends RuntimeException {

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
