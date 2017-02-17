package sy.andrew.linkifier.util;

import sy.andrew.linkifier.model.OrderedLinkifyObject;

public class OLOLinkifier {
    private final StringLinkifier linky = new StringLinkifier();
  
    public OrderedLinkifyObject linkify(OrderedLinkifyObject olo) {
        String linkifiedString = linky.linkify(olo.getOriginalString());
        olo.setLinkifiedString(linkifiedString);
        return olo;
    }
}
