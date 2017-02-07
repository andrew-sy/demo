package sy.andrew.linkifier;
/**
 * Interface for a FIFO queue which performs some processing on its inputs
 * before making them available to consumers.
 */
public interface ProcessingQueue
{
    /**
     * Inserts the specified element into this queue, if possible.
     *
     * @param input the element to insert.
     * @return true if it was possible to add the element to this queue, else false
     */
    boolean offer(String input);

    /**
     * Retrieves and removes the head of this queue, or null if this queue is empty.
     * 
     * @return the head of this queue, or null if this queue is empty.
     */
    String poll();
}
