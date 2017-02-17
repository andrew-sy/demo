package sy.andrew.linkifier.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.processors.LinkifyProcessingQueue;

public class SingleThreadDemo {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final LinkifyProcessingQueue queue = createLinkifyProcessingQueue();
        
        final List<OrderedLinkifyObject> inputOlos = Arrays.asList(
                new OrderedLinkifyObject(0, "<html><body><p>http://linkify</p><img src=\"http://do-not-linkify\"/></body></html>"),
                new OrderedLinkifyObject(1, "<name>Andy</name><age>21</age>"),
                new OrderedLinkifyObject(2, " http://example.com/query?name=Bill http://linkify-me-too")
                );

        for (final OrderedLinkifyObject inputOlo : inputOlos) {
            if (!queue.offer(inputOlo)) {
                System.out.println("queue.offer() failed for olo=[" + inputOlo + "]");
            } else {
                System.out.println();
                System.out.println("input=[" + inputOlo + "]");
                OrderedLinkifyObject output = pollTillMaxTry(queue, 500);
                System.out.println("output=[" + output
                        + "]");
            }
        }

    }
    
    private static LinkifyProcessingQueue createLinkifyProcessingQueue() {
        int firstSequenceNumber = 0;
        int capacity = 100;
        int executorsThreadPoolSize = 4;
        return new LinkifyProcessingQueue(firstSequenceNumber, capacity, 
                executorsThreadPoolSize);
   
    }
    
    private static OrderedLinkifyObject pollTillMaxTry(final LinkifyProcessingQueue linkifierQueue, 
            final int MAX_TRY) {
        
        OrderedLinkifyObject output = null;
        for(int attempts = 0; 
                output == null && attempts < MAX_TRY; 
                attempts++) {
            
            output = linkifierQueue.poll();
        }
        
        return output;
    }

    
}
