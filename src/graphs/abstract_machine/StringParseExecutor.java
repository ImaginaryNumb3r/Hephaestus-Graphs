package graphs.abstract_machine;

/**
 * @author Patrick Plieschnegger
 */
@FunctionalInterface
public interface StringParseExecutor extends MachineExecutor<Character, StringBuilder> {

    default String process(BufferQueue<Character> inputStream) {
        return process(inputStream, new StringBuilder()).toString();
    }


    StringBuilder process(BufferQueue<Character> inputStream, StringBuilder empty);

}
