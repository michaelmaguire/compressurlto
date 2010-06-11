package ca.michaelmaguire.server.compressurlto.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateMapServletImpl extends HttpServlet {

	private static final long serialVersionUID = 5722747728473797229L;

	private static final String BASE_URL = "http://compressurlto.appspot.com/";

	protected void service(HttpServletRequest aRequest,
			HttpServletResponse aResponse) {

		final String untouchedUrl = aRequest.getParameter("url");
		
		String longUrl = untouchedUrl;
		
		// Make sure that "thetimes.co.uk " maps to same thing as "thetimes.co.uk"
		longUrl = longUrl.trim();
		
		// If we don't check for this, there is a danger that if a user attempts to create a short
		// URL for e.g. "www.bob.com", then when we return that URL in the redirect, since it doesn't
		// start with http://, our server will consider it a relative redirect, and the client will
		// then attempt to fetch http://compressurlto.appspot.com/www.bob.com which will fail.
		if( ! longUrl.startsWith("http"))
		{
			longUrl = "http://" + longUrl;
		}

		String shortenedUrl = longUrl; // Default if something goes wrong is
		// the original URL.

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String query = "select from " + UrlMap.class.getName()
					+ " where originalurl == '" + longUrl + "'";
			List<UrlMap> existingMapList = (List<UrlMap>) pm.newQuery(query)
					.execute();

			UrlMap firstExistingUrlMap = existingMapList.get(0);

			shortenedUrl = firstExistingUrlMap.getShortUrl();

		} catch (Exception e) {
			UrlMap newUrlMap = new UrlMap(longUrl, new Date());

			pm.makePersistent(newUrlMap);

			shortenedUrl = newUrlMap.getShortUrl();

		} finally {
			pm.close();
		}

		String response = "<html><body>Original URL: "+untouchedUrl+" compressed to URL:</p></p>" + BASE_URL
				+ shortenedUrl + "</body></html>";

		PrintWriter pw = null;
		try {
			pw = aResponse.getWriter();
			pw.write(response);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
