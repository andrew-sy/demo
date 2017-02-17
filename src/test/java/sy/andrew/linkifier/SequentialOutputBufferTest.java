package sy.andrew.linkifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import sy.andrew.linkifier.model.OrderedLinkifyObject;

public class SequentialOutputBufferTest {

    @Test
    public void givenInputOutOfOrder_shouldProduceOutputInOrder() {
        // ARRANGE
        final ConcurrentLinkedQueue<OrderedLinkifyObject> outputQueue = new ConcurrentLinkedQueue<>();
        SequentialOutputBuffer sut = SequentialOutputBuffer.create(1, outputQueue);

        // ACT
        // Out of order inputs
        sut.put(new OrderedLinkifyObject(6, ""));
        sut.put(new OrderedLinkifyObject(5, ""));
        sut.put(new OrderedLinkifyObject(4, ""));
        sut.put(new OrderedLinkifyObject(1, ""));
        sut.put(new OrderedLinkifyObject(2, ""));
        sut.put(new OrderedLinkifyObject(3, ""));
        sut.put(new OrderedLinkifyObject(7, ""));
        sut.put(new OrderedLinkifyObject(8, ""));

        // ASSERT
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 1);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 2);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 3);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 4);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 5);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 6);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 7);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 8);
        assertTrue("outputQueue should be empty.", outputQueue.isEmpty());
    }

    @Test
    public void givenInputWithSkippedSequenceNumber_thenOutputShouldExcludeSequenceNumbersGreaterThanOrEqual() {
        // ARRANGE
        final ConcurrentLinkedQueue<OrderedLinkifyObject> outputQueue = new ConcurrentLinkedQueue<>();
        SequentialOutputBuffer sut = SequentialOutputBuffer.create(1, outputQueue);

        // ACT
        // Out of order inputs
        sut.put(new OrderedLinkifyObject(1, ""));
        sut.put(new OrderedLinkifyObject(2, ""));
        sut.put(new OrderedLinkifyObject(3, ""));
        //skip 4
        sut.put(new OrderedLinkifyObject(5, "")); 

        // ASSERT
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 1);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 2);
        assertEquals("Output should be in order.", outputQueue.poll().getSequenceNumber(), 3);
        assertTrue("outputQueue should be empty.", outputQueue.isEmpty());        
    }
    
    @Test
    public void givenNoInput_thenOutputShouldBeEmpty() {
        //ARRANGE 
        final ConcurrentLinkedQueue<OrderedLinkifyObject> outputQueue = 
                new ConcurrentLinkedQueue<>();
        SequentialOutputBuffer sut = SequentialOutputBuffer.create(1, outputQueue);

        //ACT - no inputs

        //ASSERT
        assertTrue("outputQueue should be empty.", outputQueue.isEmpty());        
    }
}