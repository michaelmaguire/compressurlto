package ca.michaelmaguire.server.compressurlto.server;

import junit.framework.TestCase;

public class UrlMapTest extends TestCase {

	public void testProduceShortHostname() {
		
		assertEquals("amazon", UrlMap.produceShortHostname("http://amazon"));
		assertEquals("amazoncouk", UrlMap.produceShortHostname("http://amazon.co.uk"));
		assertEquals("amazoncouk", UrlMap.produceShortHostname("http://www.amazon.co.uk"));
		assertEquals("amazoncouk", UrlMap.produceShortHostname("http://www.amazon.co.uk:8080"));
		assertEquals("amazoncouk", UrlMap.produceShortHostname("http://www.amazon.co.uk/this/is/a/test"));
		assertEquals("amazoncom", UrlMap.produceShortHostname("http://amazon.com"));
		assertEquals("amazoncom", UrlMap.produceShortHostname("http://www.amazon.com"));
		assertEquals("amazoncom", UrlMap.produceShortHostname("http://www.amazon.com:8080"));
		assertEquals("amazoncom", UrlMap.produceShortHostname("http://www.amazon.com/this/is/a/test"));
		
	}

}
