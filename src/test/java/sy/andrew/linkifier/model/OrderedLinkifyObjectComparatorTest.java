package sy.andrew.linkifier.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import sy.andrew.linkifier.model.OrderedLinkifyObject;
import sy.andrew.linkifier.model.OrderedLinkifyObjectComparator;

public class OrderedLinkifyObjectComparatorTest {

    @Test
    public void ifAEqualsB_thenReturn0() {
        //ARRANGE
        final OrderedLinkifyObject a = new OrderedLinkifyObject(1, "a");
        final OrderedLinkifyObject b = new OrderedLinkifyObject(1, "b");
        
        //ACT
        int actual = new OrderedLinkifyObjectComparator().compare(a, b);
        
        //ASSERT
        assertEquals("Given a = b, ", actual, 0);
    }

    @Test
    public void ifAGreaterB_thenReturn1() {
        //ARRANGE
        final OrderedLinkifyObject a = new OrderedLinkifyObject(2, "a");
        final OrderedLinkifyObject b = new OrderedLinkifyObject(1, "b");
        
        //ACT
        int actual = new OrderedLinkifyObjectComparator().compare(a, b);
        
        //ASSERT
        assertEquals("Given a = b, ", actual, 1);
    }

    @Test
    public void ifALessThanB_thenReturnMinus1() {
        //ARRANGE
        final OrderedLinkifyObject a = new OrderedLinkifyObject(1, "a");
        final OrderedLinkifyObject b = new OrderedLinkifyObject(2, "b");
        
        //ACT
        int actual = new OrderedLinkifyObjectComparator().compare(a, b);
        
        //ASSERT
        assertEquals("Given a = b, ", actual, -1);
    }
}

