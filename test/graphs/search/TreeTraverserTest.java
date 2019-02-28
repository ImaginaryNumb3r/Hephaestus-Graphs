package graphs.search;

import collections.iteration.Iteration;
import graphs.node.GenericNode;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Creator: Patrick
 * Created: 28.02.2019
 * Purpose:
 */
public class TreeTraverserTest {

    public GenericNode<String> getTestGraph() {
        var root = new GenericNode<>("root");
        var node1 = new GenericNode<>("1");
        var node2 = new GenericNode<>("2");
        var node11 = new GenericNode<>("1.1");
        var node12 = new GenericNode<>("1.2");
        var node21 = new GenericNode<>("2.1");
        var node22 = new GenericNode<>("2.2");

        root.add(node1);
        root.add(node2);
        node1.add(node11);
        node1.add(node12);
        node2.add(node21);
        node2.add(node22);

        return root;
    }

    @Test
    public void testBFS() {
        var root = getTestGraph();
        var expectedSequence = Arrays.asList("root", "1", "2", "1.1", "1.2", "2.1", "2.2");
        var iterator = TreeTraverser.of(root, new BreadthFirstSearch<>());

        var resultSequence = Iteration.of(iterator)
                .map(GenericNode::getValue)
                .collect(Collectors.toList());

        assertEquals(expectedSequence, resultSequence);
    }

    @Test
    public void testDFS() {
        var root = getTestGraph();
        // DFS from right to left.
        var expectedSequence = Arrays.asList("root", "2", "2.2", "2.1", "1", "1.2", "1.1");
        var iterator = TreeTraverser.of(root, new DepthFirstSearch<>());

        var resultSequence = Iteration.of(iterator)
                .map(GenericNode::getValue)
                .collect(Collectors.toList());

        assertEquals(expectedSequence, resultSequence);
    }
}
