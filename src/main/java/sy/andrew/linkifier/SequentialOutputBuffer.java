package sy.andrew.linkifier;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.model.OrderedLinkifyObjectComparator;

/** 
 * Holds OrderedLinkifyObjects until they are ready to be 
 * moved to the finalOutputQueue for consumers to pickup. Each  
 * OrderedLinkifyObject should be made available to consumers in strict 
 * sequential order based on sequence number. This buffer maintains
 * information about the sequence number that is next in line to be
 * added to the finalOutputQueue.  
 * Any OrderedLinkifyObject whose sequence number is greater will be 
 * held in this buffer until their turn comes up, at which point 
 * it is automatically moved over to the finalOutputQueue.
 *  
 * @author asy
 *
 */
public class SequentialOutputBuffer {
    private int sequenceNumberToWaitFor = 0;
    private final ConcurrentLinkedQueue<OrderedLinkifyObject> finalOutputQueue; 
    
    //access to this non-synchronized queue should be protected by using "this" as monitor
    private final PriorityQueue<OrderedLinkifyObject> holdingHeap = new PriorityQueue(200, new OrderedLinkifyObjectComparator());
    
    public static SequentialOutputBuffer create(int firstSequenceNumber, 
            ConcurrentLinkedQueue<OrderedLinkifyObject> finalOutputQueue) {
        //TODO: do we want to return singleton instead? 
        return new SequentialOutputBuffer(firstSequenceNumber, finalOutputQueue);
    }

    private SequentialOutputBuffer(int firstSequenceNumber, 
            ConcurrentLinkedQueue<OrderedLinkifyObject> finalOutputQueue) {
        this.sequenceNumberToWaitFor = firstSequenceNumber;
        this.finalOutputQueue = finalOutputQueue;
    }
    
    public void put(OrderedLinkifyObject incomingOlo) {
        synchronized(this) {
            if (incomingOlo.getSequenceNumber() > sequenceNumberToWaitFor) {
                holdingHeap.add(incomingOlo);
            } else {
                promote(incomingOlo);  
                promoteAllQualifiedOlos();
            }                     
        }
    }
    
    protected void promote(OrderedLinkifyObject olo) {
        synchronized(this) {
            finalOutputQueue.offer(olo);
            sequenceNumberToWaitFor++;
        }    
    }
    
    protected void promoteAllQualifiedOlos() {
        synchronized(this) {
            while(qualifiedOloExists()) {
                OrderedLinkifyObject minOlo = holdingHeap.poll();
                promote(minOlo);
            }
        }
    }
    
    protected boolean qualifiedOloExists() {
        synchronized(this) {
            OrderedLinkifyObject minOlo = holdingHeap.peek();
            return (minOlo != null && minOlo.getSequenceNumber() <= sequenceNumberToWaitFor);            
        }
    }
    
}
