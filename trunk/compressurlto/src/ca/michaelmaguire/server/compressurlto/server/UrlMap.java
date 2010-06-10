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
	private Long	key;

	@Persistent
	private String	originalurl;

	@Persistent
	private Date	date;

	public UrlMap( String originalurl, Date date )
	{
		this.originalurl = originalurl;
		this.date = date;
	}

	public Long getKey()
	{
		return key;
	}

	public String getOriginalurl()
	{
		return originalurl;
	}

	public Date getDate()
	{
		return date;
	}

	public void setOriginalurl( String originalurl )
	{
		this.originalurl = originalurl;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}
	
	public String getShortUrl()
	{
		return Base36.convertToBase36(key);
	}
}