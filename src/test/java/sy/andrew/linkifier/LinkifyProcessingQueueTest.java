package sy.andrew.linkifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LinkifyProcessingQueueTest {
    private Linkifier linky;
    private LinkifyProcessingQueue queue;
    private final String[] arrInputText = { "<html><body>http://foo</body></html>",
            "<name>Andy</name>" };

    @Before
    public void setUp() {
        linky = new Linkifier();
        queue = new LinkifyProcessingQueue();
    }

    @Test
    public final void testOffer() {
        int expectedSizeOfQueue = 0;
        for (final String inputText : arrInputText) {
            final String linkifiedText = linky.linkify(inputText);
            if (queue.offer(inputText)) {
                expectedSizeOfQueue++;
                assertTrue("queue should contain linkifiedText=[" + linkifiedText + "]",
                        queue.contains(linkifiedText));
            }
        }
        assertEquals("size of queue", expectedSizeOfQueue, queue.size());
    }

    @Test
    public final void testFifo() {
        final List<String> linkifiedTextList = new ArrayList<>(arrInputText.length);
        for (final String inputText : arrInputText) {
            if (queue.offer(inputText)) {
                linkifiedTextList.add(linky.linkify(inputText));
            }
        }

        for (final String expectedLinkifiedText : linkifiedTextList) {
            final String textPolledFromQueue = queue.poll();
            assertEquals("expectedLinkifiedText should match textPolledFromQueue.",
                    expectedLinkifiedText, textPolledFromQueue);
        }
    }

    @Test
    public final void testEmptyQueuePoll() {
        assertEquals("polling empty queue should return null.", null, queue.poll());
    }
}
