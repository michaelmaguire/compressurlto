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

		String longUrl = aRequest.getParameter("url");

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

		String response = "<html><body>Shortened URL: " + BASE_URL
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
