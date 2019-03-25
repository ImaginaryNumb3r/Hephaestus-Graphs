package graphs.abstract_machine;

import essentials.annotations.Package;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Plieschnegger
 */
@Package class StringExecutor extends AbstractPullMachine<Character, StringBuilder> implements StringParseExecutor {

    protected StringExecutor(@NotNull State<Character, StringBuilder> initialState) {
        super(initialState);
    }
}
