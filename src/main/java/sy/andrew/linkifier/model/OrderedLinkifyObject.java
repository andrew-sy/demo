package sy.andrew.linkifier.model;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(this.sequenceNumber, this.originalString, this.linkifiedString);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) { return false; }
        if (this.getClass() != other.getClass()) { return false; }
        
        final OrderedLinkifyObject that = (OrderedLinkifyObject) other;
        return Objects.equals(this.sequenceNumber, that.sequenceNumber) 
                && Objects.equals(this.originalString, that.originalString)
                && Objects.equals(this.linkifiedString, that.linkifiedString)
                ;
    }

    @Override
    public String toString() {
        return "OrderedLinkifyObject [sequenceNumber=" + this.sequenceNumber + ", originalString=" + this.originalString
                + ", linkifiedString=" + this.linkifiedString + "]";
    }
    
    
    
}
