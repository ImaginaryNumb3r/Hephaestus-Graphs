package graphs.abstract_machine;

import collections.iterator.Iterators;
import graphs.abstract_machine.exception.StateViolation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
public class MachineExecutorImplBuilderTest {

    private MachineExecutor<Character, String> removeWhitespaceBuilder() {
        var startState = "ws_start";
        var builder = new AbstractMachineBuilder<String, Character, String>(startState);

        builder.addReflection(startState,
                (ch) -> !Character.isWhitespace(ch),
                (ch, buff) -> buff + ch);
        builder.setDefaultTransition(startState, (ch, buff) -> buff);

        return builder.construct();
    }

    @Test
    public void testClearWhiteSpaces() throws StateViolation {
        String string = "abc [c c ] de f";
        String expected = "abc[cc]def";
        var machine = removeWhitespaceBuilder();

        String result = machine.process(() -> Iterators.of(string), "");
        assertEquals(expected, result);
    }

    @Test
    public void testParseBrackets() throws StateViolation {
        String input = "abc [c c ] de f";
        String expected = "cc";
        checkParseBrackets(expected, input);

        input = "abc [c c ] d[]e [f]";
        expected = "ccf";
        checkParseBrackets(expected, input);

        input = "abc c c  de f";
        expected = "";
        checkParseBrackets(expected, input);

        input = "";
        expected = "";
        checkParseBrackets(expected, input);
    }

    public void checkParseBrackets(String expected, String input) throws StateViolation {
        String startState = "start";
        String parsingState = "parsing";

        var builder = new AbstractMachineBuilder<String, Character, String>(startState);

        builder.addTransition(startState, parsingState, (ch) -> ch == '[', (ch, buff) -> buff);
        builder.setDefaultTransition(startState, (ch, buff) -> buff);

        builder.addTransition(parsingState, startState, (ch) -> ch == ']', (ch, buff) -> buff);
        builder.addReflection(parsingState,
                (ch) -> !Character.isWhitespace(ch),
                (ch, buff) -> buff + ch);
        builder.setDefaultTransition(parsingState, (ch, buff) -> buff);

        var machine = builder.construct();

        String result = machine.process(() -> Iterators.of(input), "");
        assertEquals(expected, result);
    }
}
