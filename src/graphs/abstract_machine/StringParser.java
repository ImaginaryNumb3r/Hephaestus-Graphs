package graphs.abstract_machine;

import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Plieschnegger
 */
public class StringParser extends AbstractMachine<Character, StringBuilder> implements StringParseExecutor {

    protected StringParser(@NotNull State<Character, StringBuilder> initialState) {
        super(initialState);
    }
}
