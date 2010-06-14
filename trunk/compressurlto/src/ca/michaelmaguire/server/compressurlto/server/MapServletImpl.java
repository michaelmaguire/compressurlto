package ca.michaelmaguire.server.compressurlto.server;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The server side implementation of the RPC service.
 */
public class MapServletImpl extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(MapServletImpl.class
			.getName());

	private static final long serialVersionUID = -6584147424779752747L;

	protected void service(HttpServletRequest aRequest,
			HttpServletResponse aResponse) {

		String requestURI = aRequest.getRequestURI();
		// LOG.info(requestURI);

		StringTokenizer stringTokenizer = new StringTokenizer(requestURI, "/");

		String shortenedHost = stringTokenizer.nextToken();

		String encodedKey = stringTokenizer.nextToken();

		long key = Base36.convertFromBase36(encodedKey);

		LOG.info("shortenedHost: " + shortenedHost + " encodedKey: "
				+ encodedKey + " key: " + key);
		Long longKey = new Long(key);

		String originalUrl = "/ErrorUrlNotFound.html";

		// See if it's in the MemCache.
		String cachedOriginalUrl = UrlMapCache.getOriginalUrlForKey(longKey);
		if (null != cachedOriginalUrl) {
			originalUrl = cachedOriginalUrl;
			// Worked! LOG.info( "MemCache hit! " + longKey + " " +
			// cachedOriginalUrl );

			// Verify the URL hasn't been tampered with.
			if (!UrlMap.produceShortHostname(originalUrl).equals(shortenedHost)) {
				originalUrl = "/ErrorUrlNotFound.html";
			}

		} else {

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {

				UrlMap urlMap = (UrlMap) pm
						.getObjectById(UrlMap.class, longKey);

				originalUrl = urlMap.getOriginalurl();

				// Verify the URL hasn't been tampered with.
				if (!UrlMap.produceShortHostname(originalUrl).equals(
						shortenedHost)) {
					originalUrl = "/ErrorUrlNotFound.html";
				} else {
					// Populate this into the MemCache.
					UrlMapCache.put(longKey, originalUrl);
				}

			} catch (Exception e) {
				LOG.severe("Problem fetching urlmap: "
						+ e.getLocalizedMessage());

			} finally {
				pm.close();
			}
		}

		aResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		aResponse.addHeader("Location", originalUrl);
	}
}
