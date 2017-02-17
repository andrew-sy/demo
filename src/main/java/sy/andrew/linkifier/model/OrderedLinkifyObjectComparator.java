package sy.andrew.linkifier.model;

import java.util.Comparator;

public class OrderedLinkifyObjectComparator implements Comparator<OrderedLinkifyObject> {
    @Override
    public int compare(OrderedLinkifyObject a, OrderedLinkifyObject b) {
        if (a.getSequenceNumber() == b.getSequenceNumber()) {
            return 0;
        } else {
            return a.getSequenceNumber() > b.getSequenceNumber() ? 1 : -1;
        }   
    }

}
