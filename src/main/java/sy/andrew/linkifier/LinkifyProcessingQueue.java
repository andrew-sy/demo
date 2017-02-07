package sy.andrew.linkifier;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkifyProcessingQueue implements ProcessingQueue {

    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    private final Linkifier linky = new Linkifier();

    @Override
    public boolean offer(final String input) {
        final String linkifiedResult = linky.linkify(input);
        return queue.offer(linkifiedResult);
    }

    @Override
    public String poll() {
        return queue.poll();
    }

    protected boolean contains(final String element) {
        return queue.contains(element);
    }

    protected int size() {
        return queue.size();
    }

}
