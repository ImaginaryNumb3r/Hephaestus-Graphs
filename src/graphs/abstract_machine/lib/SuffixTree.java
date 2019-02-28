package graphs.abstract_machine.lib;

import java.util.*;

/**
 * @author Patrick Plieschnegger
 */
public class SuffixTree {
    private final Map<Character, Suffix> _root = new HashMap<>();

    public SuffixTree(List<String> suffixes) {
        suffixes.sort(String::compareTo);

        for (String suffix : suffixes) {

        }
    }

    private final class Suffix {
        private final Map<Character, Suffix> _childNodes;
        private final char _character;
        private boolean _end;

        private Suffix(char character) {
            _childNodes = new HashMap<>();
            _character = character;
        }

        public Suffix getOrCreate(char character) {
            return _childNodes.computeIfAbsent(character, Suffix::new);
        }
    }
}
