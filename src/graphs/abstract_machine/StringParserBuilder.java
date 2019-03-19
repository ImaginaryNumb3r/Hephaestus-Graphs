package graphs.abstract_machine;

/**
 * @author Patrick Plieschnegger
 */
public class StringParserBuilder<ID> extends MachineExecutorBuilder<ID, Character, StringBuilder> {

    public StringParserBuilder(ID initialIdentifier) {
        super(initialIdentifier);
    }

    @Override
    public StringParseExecutor buildExecutor() {
        return new StringExecutor(getInitialState());
    }

    public StringParseAutomaton buildPushdown() {
        return new StringAutomaton(getInitialState(), new StringBuilder());
    }
}
