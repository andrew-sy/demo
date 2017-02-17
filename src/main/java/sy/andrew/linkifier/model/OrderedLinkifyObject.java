package sy.andrew.linkifier.model;

public class OrderedLinkifyObject {
    private final int sequenceNumber;
    private final String originalString;
    private String linkifiedString;
    
    public OrderedLinkifyObject(int sequenceNumber, String originalString) {
        this.sequenceNumber = sequenceNumber;
        this.originalString = originalString;
    }

    public String getLinkifiedString() {
        return this.linkifiedString;
    }

    public void setLinkifiedString(String linkifiedString) {
        this.linkifiedString = linkifiedString;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public String getOriginalString() {
        return this.originalString;
    }
    
}
