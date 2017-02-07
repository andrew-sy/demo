package sy.andrew.linkifier;

public class LinkifyProcessingQueueDemo {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final String[] arrInputText = {
                "<html><body><p>http://linkify</p><img src=\"http://do-not-linkify\"/></body></html>",
                "<name>Andy</name><age>21</age>",
                " http://example.com/query?name=Bill http://linkify-me-too" };
        final LinkifyProcessingQueue queue = new LinkifyProcessingQueue();
        for (final String inputText : arrInputText) {
            if (!queue.offer(inputText)) {
                System.out.println("queue.offer() failed for inputText=[" + inputText + "]");
            } else {
                System.out.println();
                System.out.println("inputText=[" + inputText + "]");
                final String textPolledFromLinkifyQueue = queue.poll();
                System.out.println("textPolledFromLinkifyQueue=[" + textPolledFromLinkifyQueue
                        + "]");
            }
        }

    }
}
