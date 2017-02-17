 package sy.andrew.linkifier.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Used to "linkify" a given inputText. See {@link #linkify()}. Instances of
 * this class maintains no state, and so are multi-thread safe.
 * 
 * @author asy
 * 
 */
public class StringLinkifier {

    /**
     * Matches input string till first occurrence of one of the following is
     * found:
     * <ul>
     * <li>A "&lt;" followed by zero or more text, followed by a "&gt;"</li>
     * <li>
     * A "http://" or "https://" scheme followed by at least one or more valid
     * URI characters as specified by RFC 3986. The match ends at the first
     * invalid URI character found (such as a space character) or at end of
     * input string. Match is case-insensitive so the http scheme may be in
     * upper or lower case combinations, e.g. "HTTP://" or "hTTpS://"</li>
     * </ul>
     * <p>
     * For example, the following are matched:
     * <ul>
     * <li>"hello&lt;name&gt;"</li>
     * <li>"&lt;name&gt;"</li>
     * <li>"&lt;&gt;"</li>
     * <li>"&lt;/name&gt;"</li>
     * <li>
     * "see
     * HTTP://docs.oracle.com/javase/6/docs/api/java/util/regex/Matcher.html
     * #appendReplacement(java.lang.StringBuffer , java.lang.String)"</li>
     * </p>
     * 
     */
    private static final Pattern PATTERN = Pattern.compile(
            "(.*?)((<.*?>)|(https?://)([-a-zA-Z0-9._~:/?#\\[\\]@!$&'()*+,;=%]+))",
            Pattern.CASE_INSENSITIVE);

    /**
     * "Linkify" the inputText by finding all raw URLs prefixed with "http://"
     * or "https://" and converting them to HTML links. For example,
     * http://www.example.com becomes <a
     * href="http://www.example.com">www.example.com</a>. Any URLs found between
     * an opening and closing angle bracket will not be modified. E.g. <img
     * src="http://www.example.com/foo.png">foo</img> will not be modified.
     * 
     * @param inputText
     *            string to convert
     * @return linkified version of inputText. Return empty string if inputText
     *         is null.
     */
    public String linkify(final String inputText) {
        if (inputText == null) {
            return "";
        }

        final StringBuffer outputText = new StringBuffer();
        final Matcher matcher = PATTERN.matcher(inputText);

        while (matcher.find()) {
            if (tagFound(matcher)) {
                matcher.appendReplacement(outputText, "$1$3");
            } else {
                matcher.appendReplacement(outputText, "$1<a href=\"$4$5\">$5</a>");
            }
        }

        matcher.appendTail(outputText);

        return outputText.toString();
    }

    private boolean tagFound(final Matcher matcher) {
        return matcher.group(3) != null;
    }

}
