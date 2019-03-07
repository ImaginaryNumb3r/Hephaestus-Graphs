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

    private AbstractMachineBuilder<String, Character, String> removeWhitespaceBuilder() {
        var builder = new AbstractMachineBuilder<String, Character, String>("start");
        var startState = "start";

        builder.addReflection(startState,
                (ch, buff) -> {
                    boolean accept = !Character.isWhitespace(ch);
                    return accept;
                },
                (ch, buff) -> buff + ch);
        builder.addReflection(startState,
                (ch, buff) -> {
                    boolean accept = Character.isWhitespace(ch);
                    return accept;
                },
                (ch, buff) -> buff);

        return builder;
    }

    @Test
    public void testClearWhiteSpaces() throws StateViolation {
        String string = "abc [c c ] fi n";
        String expected = "abc[cc]fin";
        var machine = removeWhitespaceBuilder().construct();

        boolean whitespace = Character.isWhitespace('[');
        boolean whitespace1 = Character.isWhitespace('}');
        boolean whitespace2 = Character.isWhitespace('{');
        boolean whitespace3 = Character.isWhitespace('>');
        boolean whitespace4 = Character.isWhitespace('<');

        String result = machine.process(() -> Iterators.of(string), "");
        assertEquals(expected, result);
    } /*

    @Test
    public void testParseBrackets() {
        String string = "abc [c c ] fi n";
        String expected = "cc";

        var builder = removeWhitespaceBuilder();
        builder.addTermination("start", ch -> ch == ']');
        var removeWhitespaceMachine = builder.construct();

        builder = new AbstractMachineBuilder<>("start");

        builder.addSoftTransition("start", "parsing", (ch) -> ch == '[');
        builder.addMachine("start", "fin", (ch) -> ch != '}', removeWhitespaceMachine);
        builder.addTermination("fin", (ch) -> true);

        var machine = builder.construct();
        final String result = machine.process(Iterators.of(string), "");

        assertEquals(expected, result);
        System.out.println();
    }*/
}
