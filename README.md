# demo_linkifier
My solution to a coding assignment given by a previous (and now defunct) employer. 

## Problem Statement 
As given by the employer. 

### Processing Queue Problem:

Build a LinkifyProcessingQueue class that implements the ProcessingQueue interface (provided). 
This interface defines the following methods:

	boolean offer(String input);
	String poll();

The LinkifyProcessingQueue class will be responsible for receiving unprocessed data from multiple concurrent producers 
through the offer() method, applying the Linkify transformation (see below) to the data, 
and then adding the processed data to the tail of the queue. If any data has completed processing, poll() will remove it 
from the head of the queue and return it; if no data is available, then poll() will return null. The queue should be a 
FIFO queue, the first items in should also be the first items out.

The Linkify transformation should find raw URLs prefixed with "http://" in the input text and convert them to HTML links, 
for example `http://www.example.com` becomes `<a href="http://www.example.com">www.example.com</a>`. Any URLs that are already 
within HTML links should not be modified.

The solution should be thread-safe and include a unit test. Please include instructions on how to compile and test your project. 
Please be prepared to discuss how your solution could be scaled to accommodate very large volumes of data.

## My Solution
- thread safe implementation of the `LinkifyProcessingQueue` interface is based on `java.util.concurrent.ConcurrentLinkedQueue`
- linkifying logic makes use of regex `https?://[-a-zA-Z0-9._~:/?#\\[\\]@!$&'()*+,;=%]+` which is explained in the notes below.

### main() classes
- LinkifyProcessingQueueDemo is a single threaded demo that offers(inputText) and polls(). inputText and linkified result 
are printed side-by-side for comparison. 
- LinkifyProcessingQueueDemo2 is a multi-threaded demo with 2 producers and consumers sharing a single linkify queue. 

### Notes About The Code:
- I linkify not just `http://xxx` but also `https://xxx` (assuming of course the URL has not been linkified yet). 
- The regex Pattern object I use in my code is set to be case insensitive, so a scheme like `HTTP://` or `HTTPS://` 
(or `hTtP://` for that matter) will also be matched. 
- Re-linkifying a linkified text is of course idempotent. 
- Any http or https URL contained within the opening and closing brackets of a tag is considered to be linkified already, 
and will not be re-linkified. This applies not just to HTML anchor tags, such as `<a href="http://foo">`, but also to all 
other tags such as `<img src="http://foo">`
- The following however 
```
<a href="http://myurl">http://myurl</a> 
```

will be converted to 
```  
<a href="http://myurl"><a href="http://myurl"></a></a> 
```

- Coming up with a regex for a http/https URL that strictly complies with RFC 3986 (which defines URI syntax) is non-trivial. For example, see http://stackoverflow.com/questions/161738/what-is-the-best-regular-expression-to-check-if-a-string-is-a-valid-url
To simplify the regex for the purpose of this exercise, I'm using the below regex instead to match a http/https URL. Basically it matches using any character that can validly be used inside a URI, without regard for what component of the URI it appears in (e.g. in the scheme, authority, or path component). 

```
https?://[-a-zA-Z0-9._~:/?#\\[\\]@!$&'()*+,;=%]+
```

- It is also note worthing that any character that is not in the regex (such as the space character `0x20`, the left angle bracket `<` and the double quote `")` can be used to delimit the end of the URL. So for example, the regex would match the string below up till and including `#start(int)`
```
HTTP://docs.oracle.com/javase/6/docs/api/java/util/regex/Matcher.html#start(int)<div> 
```

And the regex would match the string below up till and including "b=2"
```
HTTP://myexample.com/foo?a=1&b=2 is the answer. 
```

### PERFORMANCE CONSIDERATIONS:

- If the input text is large, the following can reduce memory requirements
  - Change the offer(String input) method to offer(InputStream input). So instead of keeping the entire input in memory, it can be read and processed in chunks of appropriate size. Similarly, rather than build the entire output text in memory, write the linkified output to persistent store as you process the input in chunks. 
  
- The memory requirements of the queue can also grow quickly if
  - if the output text from linkifying is large and stored directly on the queue. 
  - or if calls to offer() add items to the queue much more quickly than calls to poll() can consume them.
  One way to deal with the latter case is to add more queue consumers to help poll() items off the queue more quickly. 
  To deal with the former case, we can keep the output text on non-memory storage, and keep only the references to them on the in-memory queue (whether this buys us anything depends on additional conditions). Or we can use a queue implementation that spills to disk. 
  
  
