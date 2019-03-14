package graphs.abstract_machine;

/**
 * @author Patrick Plieschnegger
 */
public class StringParserBuilder<ID> extends AbstractMachineBuilder<ID, Character, StringBuilder> {

    public StringParserBuilder(ID initialIdentifier) {
        super(initialIdentifier);
    }

    @Override
    public StringParseExecutor construct() {
        return new StringParser(getInitialState());
    }
}
