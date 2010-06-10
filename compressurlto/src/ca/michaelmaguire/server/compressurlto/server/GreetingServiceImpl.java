package ca.michaelmaguire.server.compressurlto.server;

import java.util.Date;

import javax.jdo.PersistenceManager;

import ca.michaelmaguire.server.compressurlto.client.GreetingService;
import ca.michaelmaguire.server.compressurlto.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService
{

	public String greetServer( String input ) throws IllegalArgumentException
	{
		// Verify that the input is valid. 
		if( !FieldVerifier.isValidName( input ) )
		{
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException( "Name must be at least 4 characters long" );
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader( "User-Agent" );
		
		UrlMap urlmap = new UrlMap(input, new Date());
		
		
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(urlmap);
        } finally {
            pm.close();
        }
		
		
		return "Shortened URL: http://compressurlto.appspot.com/" + urlmap.getShortUrl();
	}
}
