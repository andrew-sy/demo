package sy.andrew.linkifier;

import java.util.Arrays;
import java.util.List;

public class MultithreadDemo {

    private static class Producer implements Runnable {
        private final String name;
        private final LinkifyProcessingQueue queue;
        private final List<String> inputTextList;

        private Producer(final String name, final LinkifyProcessingQueue queue,
                final List<String> inputTextList) {
            this.name = name;
            this.queue = queue;
            this.inputTextList = inputTextList;
        }

        @Override
        public void run() {
            int successfulOffersCount = 0;
            for (final String inputText : inputTextList) {
                if (queue.offer(inputText)) {
                    successfulOffersCount++;
                    System.out.println();
                    System.out.println(name + ": successfulOffersCount=" + successfulOffersCount
                            + ", inputText=[" + inputText + "]");
                } else {
                    System.out.println();
                    System.out.println(name + ": offer failed. inputText=[" + inputText + "]");
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
                final String polledText = queue.poll();
                if (polledText != null) {
                    successfulPollsCount++;
                    System.out.println();
                    System.out.println(name + ": successfulPollsCount=" + successfulPollsCount
                            + ", polledText=[" + polledText + "]");
                } else {
                    System.out.println();
                    System.out.println(name + ": polledText is null. ");
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

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final String[] arrInputText = { "<html><body>http://foo</body></html>",
                "<name>Andy</name>", " http://example.com/query?name=Bill", "hi", "ho", "boo",
                "dog", "cat" };
        final LinkifyProcessingQueue queue = new LinkifyProcessingQueue();

        final Consumer consumer1 = new Consumer("Consumer1", queue);
        new Thread(consumer1).start();

        final Producer producer1 = new Producer("Producer1", queue, Arrays.asList(arrInputText));
        new Thread(producer1).start();

        final Producer producer2 = new Producer("Producer2", queue, Arrays.asList(arrInputText));
        new Thread(producer2).start();

        final Consumer consumer2 = new Consumer("Consumer2", queue);
        new Thread(consumer2).start();

    }

}
