package ca.michaelmaguire.server.compressurlto.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UrlMap {
	private static final String BASE_URL = "http://curl.to/";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long key;

	@Persistent
	private String originalurl;

	@Persistent
	private Date date;

	public UrlMap(String originalurl, Date date) {
		this.originalurl = originalurl;
		this.date = date;
	}

	public Long getKey() {
		return key;
	}

	public String getOriginalurl() {
		return originalurl;
	}

	public Date getDate() {
		return date;
	}

	public void setOriginalurl(String originalurl) {
		this.originalurl = originalurl;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
	/* package */ static String produceShortHostname(String aUrl)
	{
		String shortHostname = "";

		try {
			URL url = new URL(aUrl);

			String hostname = url.getHost();

			StringTokenizer stringTokenizer = new StringTokenizer(hostname, ".");

			StringBuffer stringBuffer = new StringBuffer();

			int count = stringTokenizer.countTokens();

			if (1 == count) {
				// Weird. One word. Just use it.
				stringBuffer.append(stringTokenizer.nextElement());
			} else if (2 == count) {
				// Two words e.g. "amazon.com"  Just use them.
				stringBuffer.append(stringTokenizer.nextElement());
				stringBuffer.append(stringTokenizer.nextElement());
			}
			else if ( 3 <= count ) 
			{
				// Examine the last three 'words' and see whether to use all 3 or just last 2.
				
				// First read the last three tokens.
				String lastThree[] = new String[3]; 
				int tokenIndex = 0;
				int lastThreeIndex = 0;
				while( stringTokenizer.hasMoreTokens() )
				{
					String token = stringTokenizer.nextToken();
					tokenIndex++;
					
					if( tokenIndex > (count - 3))
					{
						lastThree[lastThreeIndex++] = token;
					}
				}
				
				// Now examine next to last.
				if( lastThree[1].length() > 3 )
				{
					// Next to last is long, so assume it's a real name, e.g. "www.amazon.com" => "amazon.com"
					stringBuffer.append(lastThree[1]);
					stringBuffer.append(lastThree[2]);
				}
				else
				{
					// Next to last is short, so assume it's a country sub-domain name, e.g. "www.bbc.co.uk" => "bbccouk"
					stringBuffer.append(lastThree[0]);
					stringBuffer.append(lastThree[1]);
					stringBuffer.append(lastThree[2]);
				}
			}
				

			shortHostname = stringBuffer.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return shortHostname;
		
	}
	
	public String getShortUrl() {

		String shortenedHost = produceShortHostname(originalurl);
		if( null != shortenedHost )
		{
			return BASE_URL + shortenedHost + "/" + Base36.convertToBase36(key);
		}
		else
		{
			return BASE_URL + Base36.convertToBase36(key);
		}
	}
}