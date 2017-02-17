package sy.andrew.linkifier.demo;

import java.util.Arrays;
import java.util.List;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.processors.LinkifyProcessingQueue;

public class MultithreadDemo {
    /**
     * @param args
     */
    public static void main(final String[] args) {
        final List<OrderedLinkifyObject> inputOlos_0 = Arrays.asList(
                new OrderedLinkifyObject(0, "<html><body>http://foo</body></html>"),
                new OrderedLinkifyObject(2, "<name>Andy</name><age>21</age>"),
                new OrderedLinkifyObject(4, " http://example.com/query?name=Bill http://linkify-me-too"),
                new OrderedLinkifyObject(6, "hi"),
                new OrderedLinkifyObject(8, "ho"),
                new OrderedLinkifyObject(10, "boo"),
                new OrderedLinkifyObject(12, "dog"),
                new OrderedLinkifyObject(14, "cat")
                );

        final List<OrderedLinkifyObject> inputOlos_1 = Arrays.asList(
                new OrderedLinkifyObject(1, "<html><body>http://foo</body></html>"),
                new OrderedLinkifyObject(3, "<name>Andy</name><age>21</age>"),
                new OrderedLinkifyObject(5, " http://example.com/query?name=Bill http://linkify-me-too"),
                new OrderedLinkifyObject(7, "hi"),
                new OrderedLinkifyObject(9, "ho"),
                new OrderedLinkifyObject(11, "boo"),
                new OrderedLinkifyObject(13, "dog"),
                new OrderedLinkifyObject(15, "cat")
                );

        try {
            final LinkifyProcessingQueue queue = createLinkifyProcessingQueue();

            final Consumer consumer1 = new Consumer("Consumer0", queue);
            new Thread(consumer1).start();

            final Producer producer1 = new Producer("Producer0", queue, inputOlos_0);
            new Thread(producer1).start();

            final Producer producer2 = new Producer("Producer1", queue, inputOlos_1);
            new Thread(producer2).start();

            final Consumer consumer2 = new Consumer("Consumer1", queue);
            new Thread(consumer2).start();

            Thread.sleep(5000);
            queue.shutdown();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static LinkifyProcessingQueue createLinkifyProcessingQueue() {
        int firstSequenceNumber = 0;
        int capacity = 100;
        int executorsThreadPoolSize = 4;
        return new LinkifyProcessingQueue(firstSequenceNumber, capacity, 
                executorsThreadPoolSize);
   
    }
    

    private static class Producer implements Runnable {
        private final String name;
        private final LinkifyProcessingQueue queue;
        private final List<OrderedLinkifyObject> inputOloList;

        private Producer(final String name, final LinkifyProcessingQueue queue,
                final List<OrderedLinkifyObject> inputOloList) {
            this.name = name;
            this.queue = queue;
            this.inputOloList = inputOloList;
        }

        @Override
        public void run() {
            int successfulOffersCount = 0;
            for (final OrderedLinkifyObject inputOlo : inputOloList) {
                if (queue.offer(inputOlo)) {
                    successfulOffersCount++;
                    System.out.println();
                    System.out.println(name + ": successfulOffersCount=" + successfulOffersCount
                            + ", inputOlo=[" + inputOlo + "]");
                } else {
                    System.out.println();
                    System.out.println(name + ": offer failed. inputOlo=[" + inputOlo + "]");
                }
                try {
                    Thread.sleep((long) Math.random() * 5000);
                } catch (final InterruptedException e) {
                    // Just ignore it.
                }
            }
            System.out.println();
            System.out.println(name + ": done!");
        }
    }

    private static class Consumer implements Runnable {
        private final String name;
        private static final int MAX_POLL_ATTEMPTS = 30;
        private final LinkifyProcessingQueue queue;

        private Consumer(final String name, final LinkifyProcessingQueue queue) {
            this.name = name;
            this.queue = queue;
        }

        @Override
        public void run() {
            int successfulPollsCount = 0;
            for (int pollAttempts = 0; pollAttempts <= MAX_POLL_ATTEMPTS; pollAttempts++) {
                final OrderedLinkifyObject polledOlo = queue.poll();
                if (polledOlo != null) {
                    successfulPollsCount++;
                    System.out.println();
                    System.out.println(name + ": successfulPollsCount=" + successfulPollsCount
                            + ", polledOlo=[" + polledOlo + "]");
                } else {
                    System.out.println();
                    System.out.println(name + ": polledOlo is null. ");
                }

                try {
                    Thread.sleep((long) Math.random() * 50000);
                } catch (final InterruptedException e) {
                    // Just ignore it.
                }
            }
            System.out.println();
            System.out.println(name + ": done!");
        }
    }

}
