package graphs.abstract_machine;

import essentials.annotations.Package;
import org.jetbrains.annotations.NotNull;

/**
 * Creator: Patrick
 * Created: 19.03.2019
 * Purpose:
 */
@Package class StringAutomaton extends AbstractPushMachine<Character, StringBuilder> implements StringParseAutomaton {

    protected StringAutomaton(@NotNull State<Character, StringBuilder> initialState, StringBuilder empty) {
        super(initialState, empty);
    }
}
