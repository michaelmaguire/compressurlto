package ca.michaelmaguire.server.compressurlto.server;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import ca.michaelmaguire.server.compressurlto.client.GreetingService;
import ca.michaelmaguire.server.compressurlto.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	private static final String BASE_URL = "http://compressurlto.appspot.com/";

	public String greetServer(String aLongUrl) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(aLongUrl)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		String shortenedUrl = aLongUrl; // Default if something goes wrong is
										// the original URL.

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String query = "select from " + UrlMap.class.getName()
					+ " where originalurl == '" + aLongUrl + "'";
			List<UrlMap> existingMapList = (List<UrlMap>) pm.newQuery(query)
					.execute();

			UrlMap firstExistingUrlMap = existingMapList.get(0);

			shortenedUrl = firstExistingUrlMap.getShortUrl();

		} catch (Exception e) {
			UrlMap newUrlMap = new UrlMap(aLongUrl, new Date());

			pm.makePersistent(newUrlMap);

			shortenedUrl = newUrlMap.getShortUrl();

		} finally {
			pm.close();
		}

		return "Shortened URL: " + BASE_URL + shortenedUrl;
	}
}
