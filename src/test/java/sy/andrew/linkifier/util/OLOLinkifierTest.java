package sy.andrew.linkifier.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sy.andrew.linkifier.model.OrderedLinkifyObject;

public class OLOLinkifierTest {

    @Test
    public void testLinkify() {
        //ARRANGE
        final OLOLinkifier sut = new OLOLinkifier();

        final String stringToLinkify = "http://boo";
        
        final OrderedLinkifyObject inputOlo =  new OrderedLinkifyObject(2, stringToLinkify);

        //expectedOutput
        final OrderedLinkifyObject expectedOutput =  new OrderedLinkifyObject(2, "http://boo");
        final String linkifiedString = new StringLinkifier().linkify(stringToLinkify);
        expectedOutput.setLinkifiedString(linkifiedString);
        
        //ACT
        OrderedLinkifyObject actualOutput = sut.linkify(inputOlo);
        
        //ASSERT
        assertEquals("After linkifying, ", actualOutput, expectedOutput);
        
    }

}
