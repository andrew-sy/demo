package sy.andrew.linkifier.processors;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.model.OrderedLinkifyObjectComparator;

/**
 * Holds OrderedLinkifyObjects until they are ready to be moved to the
 * finalOutputQueue for consumers to pickup. Each OrderedLinkifyObject should be
 * made available to consumers in strict sequential order based on sequence
 * number. This buffer maintains information about the sequence number that is
 * next in line to be added to the finalOutputQueue. Any OrderedLinkifyObject
 * whose sequence number is greater will be held in this buffer until their turn
 * comes up, at which point it is automatically moved over to the
 * finalOutputQueue.
 * 
 * This class uses a promotion strategy to handle a scenario like this: Let's say we have
 * written out 1,2,3 to outputQ. 4,5,6 have been picked up from the inputQueue
 * and are being processed. Processing for 5 and 6 finish before 4. While
 * waiting for 4, we park 5 and 6 in the holdingHeap. When 4 finishes, we see
 * that sequenceNumberToWaitFor is 4, so we "promote" 4 directly to the outputQ,
 * bypassing holdingHeap. At the same time we increment sequenceNumberToWaitFor
 * from 4 to 5. We then look in holdingHeap and see 5, which is next in line
 * (sequenceNumberToWaitFor==5). So we promote 5, and then similarly we do it
 * for 6.
 * 
 * Now let's say we alter the scenario a bit. 4,5,6,7 have been picked up from
 * the inputQueue and are being processed. Processing for 7 finishes first. We
 * park 7 in the holdingHeap, while waiting for 4, 5 and 6 . When 4 finishes, we
 * see that sequenceNumberToWaitFor is 4, so we "promote" 4 directly to the
 * outputQ, bypassing holdingHeap. At the same time we increment
 * sequenceNumberToWaitFor from 4 to 5. We then look in holdingHeap and see 7 at
 * the top of the heap (since 5 and 6 are still being processed). We cannot
 * promote 7 yet. So we do nothing for now.
 * 
 * @author asy
 *
 */
public class SequentialOutputBuffer {
    private int sequenceNumberToWaitFor = 0;
    private final ArrayBlockingQueue<OrderedLinkifyObject> finalOutputQueue;

    // access to this non-synchronized queue should be protected by using "this"
    // as monitor
    private final PriorityQueue<OrderedLinkifyObject> holdingHeap = new PriorityQueue<>(200,
            new OrderedLinkifyObjectComparator());

    public static SequentialOutputBuffer create(int firstSequenceNumber,
            ArrayBlockingQueue<OrderedLinkifyObject> finalOutputQueue) {
        // TODO: do we want to return singleton instead?
        return new SequentialOutputBuffer(firstSequenceNumber, finalOutputQueue);
    }

    private SequentialOutputBuffer(int firstSequenceNumber, ArrayBlockingQueue<OrderedLinkifyObject> finalOutputQueue) {
        this.sequenceNumberToWaitFor = firstSequenceNumber;
        this.finalOutputQueue = finalOutputQueue;
    }

    public void put(OrderedLinkifyObject incomingOlo) {
        synchronized (this) {
            if (incomingOlo.getSequenceNumber() > sequenceNumberToWaitFor) {
                holdingHeap.add(incomingOlo);
            } else {
                promote(incomingOlo);
                promoteAllQualifiedOlos();
            }
        }
    }

    protected void promote(OrderedLinkifyObject olo) {
        synchronized (this) {
            finalOutputQueue.offer(olo);
            sequenceNumberToWaitFor++;
        }
    }

    protected void promoteAllQualifiedOlos() {
        synchronized (this) {
            while (qualifiedOloExists()) {
                OrderedLinkifyObject minOlo = holdingHeap.poll();
                promote(minOlo);
            }
        }
    }

    protected boolean qualifiedOloExists() {
        synchronized (this) {
            OrderedLinkifyObject minOlo = holdingHeap.peek();
            return (minOlo != null && minOlo.getSequenceNumber() <= sequenceNumberToWaitFor);
        }
    }

}
