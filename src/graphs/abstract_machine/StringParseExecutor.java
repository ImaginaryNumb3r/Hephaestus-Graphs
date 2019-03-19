package graphs.abstract_machine;

import collections.iterator.StringIterator;

/**
 * @author Patrick Plieschnegger
 */
@FunctionalInterface
public interface StringParseExecutor extends MachineExecutor<Character, StringBuilder> {

    default String process(CharSequence charSequence) {
        var stringIter = StringIterator.of(charSequence);
        var bufferQueue = new BufferQueue<>(100, stringIter);

        return process(bufferQueue, new StringBuilder()).toString();
    }

    default String process(BufferQueue<Character> inputStream) {
        return process(inputStream, new StringBuilder()).toString();
    }

    StringBuilder process(Iterable<Character> inputStream, StringBuilder empty);

}
