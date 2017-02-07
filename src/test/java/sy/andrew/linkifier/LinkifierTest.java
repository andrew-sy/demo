package sy.andrew.linkifier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinkifierTest {

    @Test
    public final void whenInputTextIsNull_outputIsEmptyString() {
        final String output = new Linkifier().linkify(null);
        assertEquals("Given null input, result should be empty string", "", output);
    }

    @Test
    public final void whenInputIsEmptyString_outputIsEmptyString() {
        final String output = new Linkifier().linkify("");
        assertEquals("Given empty string input, result should be empty string", "", output);
    }

    @Test
    public final void whenInputHasNoUrlNorTags_outputIsSameAsInput() {
        final String inputText = "##hi there bob! ";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void whenInputHasSchemeHttp_outputIsLinkified() {
        final String inputText = "http://boo";
        final String expectedOutput = "<a href=\"http://boo\">boo</a>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void whenInputHasSchemeSecureHttps_outputIsLinkified() {
        final String inputText = "https://boo";
        final String expectedOutput = "<a href=\"https://boo\">boo</a>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void whenInputHasUnrecognizedSchemeFtp_outputIsSameAsInput() {
        final String inputText = "ftp://boo";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void whenInputHasSchemeHttpWithoutBody_outputIsSameAsInput() {
        final String inputText = "http://";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testLinkifyIsCaseInsensitive() {
        final String inputText = "<tiTLe>hTTPs://millenialmedia.COM</Title>";
        final String expectedOutput = "<tiTLe><a href=\"hTTPs://millenialmedia.COM\">millenialmedia.COM</a></Title>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testHttpUrlWithQueryParams() {
        final String inputText = "https://nih.gov/array/query?a=1&b=2";
        final String expectedOutput = "<a href=\"https://nih.gov/array/query?a=1&b=2\">nih.gov/array/query?a=1&b=2</a>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testHttpUrlWithHashAndParentheses() {
        final String inputText = "http://docs.oracle.com/javase/6/docs/api/java/util/regex/Matcher.html#appendTail(java.lang.StringBuffer)";
        final String expectedOutput = "<a href=\"http://docs.oracle.com/javase/6/docs/api/java/util/regex/Matcher.html#appendTail(java.lang.StringBuffer)\">docs.oracle.com/javase/6/docs/api/java/util/regex/Matcher.html#appendTail(java.lang.StringBuffer)</a>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void whenInputIsEmptyTag_outputIsSameAsInput() {
        final String inputText = "<>< >";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testNestedHtmlElements() {
        final String inputText = "<html><title>The Title</title></html>";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testHttpSchemeInsideHtmlTag() {
        final String inputText = "<img src=\"http://boo\"/>";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testMultipleHttpUrlsAndMultipleHtmlTags() {
        final String inputText = "<html><body><div>https://millenialmedia.com/boo http://amazon.com</div><http://foo/></body></html>";
        final String expectedOutput = "<html><body><div><a href=\"https://millenialmedia.com/boo\">millenialmedia.com/boo</a> <a href=\"http://amazon.com\">amazon.com</a></div><http://foo/></body></html>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testRelinkifyingIsIdempotent() {
        final String inputText = "<html><body><div>https://millenialmedia.com/boo http://amazon.com</div><http://foo/></body></html>";
        final String linkifiedText = new Linkifier().linkify(inputText);
        final String relinkifiedText = new Linkifier().linkify(linkifiedText);
        assertEquals("relinkifiedText should equal linkifiedText", linkifiedText, relinkifiedText);
    }

    @Test
    public final void testRandomSpaces1() {
        final String inputText = "  \t<img  src=\"http://boo\" />  < link /> ";
        final String expectedOutput = inputText;
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testRandomSpaces2() {
        final String inputText = " http://foo/boo hi there \t mo&bo http://goo  ";
        final String expectedOutput = " <a href=\"http://foo/boo\">foo/boo</a> hi there \t mo&bo <a href=\"http://goo\">goo</a>  ";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }

    @Test
    public final void testMultipleLineInput() {
        final String inputText = "<html>\n" + "<body> \n" + "<div>\n"
                + "https://millenialmedia.com/boo\n"
                + " http://amazon.com</div><http://foo/></body></html>";
        final String expectedOutput = "<html>\n" + "<body> \n" + "<div>\n"
                + "<a href=\"https://millenialmedia.com/boo\">millenialmedia.com/boo</a>\n"
                + " <a href=\"http://amazon.com\">amazon.com</a></div><http://foo/></body></html>";
        final String output = new Linkifier().linkify(inputText);
        assertEquals("inputText=[" + inputText + "]", expectedOutput, output);
    }
}
