package sy.andrew.linkifier.processors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import sy.andrew.linkifier.ProcessingQueue;
import sy.andrew.linkifier.model.OrderedLinkifyObject;

public class LinkifyProcessingQueue implements ProcessingQueue<OrderedLinkifyObject> {

    private final ArrayBlockingQueue<OrderedLinkifyObject> inputQueue;  
    private final ArrayBlockingQueue<OrderedLinkifyObject> outputQueue;
    private final SequentialOutputBuffer sob;
    private final ExecutorService executor;
    
    public LinkifyProcessingQueue(int firstSequenceNumber, 
            int capacity, int executorsThreadPoolSize) {
        
        if (capacity <= 0) { 
            throw new IllegalArgumentException("queueCapacity must be positive. queueCapacity=" + capacity); 
        }
        this.inputQueue = new ArrayBlockingQueue<>(capacity);        
        this.outputQueue = new ArrayBlockingQueue<>(capacity);
        
        this.sob = SequentialOutputBuffer.create(firstSequenceNumber, outputQueue);

        if (executorsThreadPoolSize <= 0) { 
            throw new IllegalArgumentException("executorsThreadPoolSize must be positive. executorsThreadPoolSize=" + executorsThreadPoolSize); 
        }
        this.executor = Executors.newFixedThreadPool(executorsThreadPoolSize);
        
        executor.submit(new LinkifyTask(inputQueue, sob));
    }

    @Override
    public boolean offer(OrderedLinkifyObject input) {
        return inputQueue.offer(input);
    }

    @Override
    public OrderedLinkifyObject poll() {
        return outputQueue.poll();
    }
    
    public void shutdown() {
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }        
    }

}
