package ca.michaelmaguire.server.compressurlto.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class UrlMap
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String	shorturl;

	@Persistent
	private String	originalurl;

	@Persistent
	private Date	date;

	public UrlMap( String originalurl, Date date )
	{
		this.originalurl = originalurl;
		this.date = date;
	}

	public String getShorturl()
	{
		return shorturl;
	}

	public String getOriginalurl()
	{
		return originalurl;
	}

	public Date getDate()
	{
		return date;
	}

	public void setShorturl( String shorturl )
	{
		this.shorturl = shorturl;
	}

	public void setOriginalurl( String originalurl )
	{
		this.originalurl = originalurl;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}
}