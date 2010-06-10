package ca.michaelmaguire.server.compressurlto.server;

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

		String shortenedUrl = aRequest.getRequestURI();
		// LOG.info(shortenedUrl);

		// Skip the initial '/' which is included in getRequestURI() API
		// response.
		shortenedUrl = shortenedUrl.substring(1);

		long key = Base36.convertFromBase36(shortenedUrl);
		LOG.info("shortenedurl: " + shortenedUrl + " key: " + key);

		String originalUrl = "/ErrorUrlNotFound.html";

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			UrlMap urlMap = (UrlMap) pm.getObjectById(UrlMap.class, new Long(
					key));

			originalUrl = urlMap.getOriginalurl();

		} catch (Exception e) {
			LOG.severe("Problem fetching urlmap: " + e.getLocalizedMessage());

		} finally {
			pm.close();
		}

		aResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		aResponse.addHeader("Location", originalUrl);
	}
}
