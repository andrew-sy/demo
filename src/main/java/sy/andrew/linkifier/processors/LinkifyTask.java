package sy.andrew.linkifier.processors;

import java.util.concurrent.ArrayBlockingQueue;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.util.OLOLinkifier;

public class LinkifyTask implements Runnable {

    private final ArrayBlockingQueue<OrderedLinkifyObject> inputQueue;  
    private final SequentialOutputBuffer sob;
    private final OLOLinkifier linky = new OLOLinkifier();

    public LinkifyTask(ArrayBlockingQueue<OrderedLinkifyObject> inputQueue, 
            SequentialOutputBuffer sob) {
        this.inputQueue = inputQueue;
        this.sob = sob;   
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                OrderedLinkifyObject olo = inputQueue.take();
                linky.linkify(olo);
                sob.put(olo);
            }
        } catch (InterruptedException e) {
            //just ignore it. 
        }
    }
    

}
