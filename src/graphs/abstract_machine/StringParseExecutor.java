package graphs.abstract_machine;

import collections.iterator.StringIterator;

/**
 * @author Patrick Plieschnegger
 */
@FunctionalInterface
public interface StringParseExecutor extends MachineExecutor<Character, StringBuilder> {

    default String process(CharSequence charSequence) {
        var stringIter = StringIterator.of(charSequence);

        return process(stringIter, new StringBuilder()).toString();
    }

    StringBuilder process(Iterable<Character> inputStream, StringBuilder empty);

}
