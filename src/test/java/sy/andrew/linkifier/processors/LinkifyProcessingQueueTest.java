package sy.andrew.linkifier.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.processors.LinkifyProcessingQueue;
import sy.andrew.linkifier.util.OLOLinkifier;

public class LinkifyProcessingQueueTest {
    private final OLOLinkifier linky = new OLOLinkifier();
    private LinkifyProcessingQueue linkifierQueue;
    private final List<OrderedLinkifyObject> orderedInput = Arrays.asList(
            new OrderedLinkifyObject(0, "<html><body>http://foo</body></html>"),
            new OrderedLinkifyObject(1, "<name>Andy</name>"),
            new OrderedLinkifyObject(2, "<name>Mom</name>"),
            new OrderedLinkifyObject(3, "<name>Pop</name>"),
            new OrderedLinkifyObject(4, "<name>Baby</name>"),
            new OrderedLinkifyObject(5, "<name>Jane</name>"),
            new OrderedLinkifyObject(6, "<name>Mom</name>"),
            new OrderedLinkifyObject(7, "<name>Mom</name>"),
            new OrderedLinkifyObject(8, "<name>Mom</name>"),
            new OrderedLinkifyObject(9, "<name>Mom</name>"),
            new OrderedLinkifyObject(10, "<name>Mom</name>"),
            new OrderedLinkifyObject(11, "<name>Mom</name>"),
            new OrderedLinkifyObject(12, "<name>Mom</name>"),
            new OrderedLinkifyObject(13, "<name>Mom</name>"),
            new OrderedLinkifyObject(14, "<name>Mom</name>"),
            new OrderedLinkifyObject(15, "<name>Mom</name>"),
            new OrderedLinkifyObject(16, "<name>Mom</name>"),
            new OrderedLinkifyObject(17, "<name>Mom</name>"),
            new OrderedLinkifyObject(18, "<name>Mom</name>"),
            new OrderedLinkifyObject(19, "<name>Mom</name>")
            );

    @Before
    public void setUp() {
        int firstSequenceNumber = 0;
        int inputCapacity = 100;
        int executorsThreadPoolSize = 4;
        linkifierQueue = new LinkifyProcessingQueue(firstSequenceNumber, inputCapacity, 
                executorsThreadPoolSize);
    }

    @Test
    public final void testOrderedOutput() {
        List<OrderedLinkifyObject> expectedOutput = orderedInput;
        
        orderedInput.stream().forEach(olo -> linkifierQueue.offer(olo));
        
        final int MAX_TRY = expectedOutput.size() * 256000;
        List<OrderedLinkifyObject> actualOutput = pollTillMaxTry(MAX_TRY, 
                expectedOutput.size(), linkifierQueue);
            
        assertEquals("Sizes should match.", expectedOutput.size(), actualOutput.size());
        assertEquals("Checking poll output.", expectedOutput, actualOutput);
    }
    
    private List<OrderedLinkifyObject> pollTillMaxTry(final int MAX_TRY, 
            final int expectedOutputSize, LinkifyProcessingQueue linkifierQueue) {
        
        List<OrderedLinkifyObject> actualOutput = new ArrayList();
        for(int attempts = 0; 
                actualOutput.size() < expectedOutputSize && attempts < MAX_TRY; 
                attempts++) {
            
            OrderedLinkifyObject output = linkifierQueue.poll();
            if(output != null) { actualOutput.add(output); }
        }
        
        return actualOutput;
    }

    @Test
    public final void testEmptyQueuePoll() {
        assertEquals("polling empty queue should return null.", null, linkifierQueue.poll());
    }
}
