package ca.michaelmaguire.server.compressurlto.server;

import java.util.Collections;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

public final class UrlMapCache
{
	private static Cache		iCache;
	private static final Logger	LOG	= Logger.getLogger( UrlMapCache.class.getName() );

	private synchronized static Cache getCache()
	{
		if( null == iCache )
		{
			try
			{
				iCache = CacheManager.getInstance().getCacheFactory().createCache( Collections.emptyMap() );
			}
			catch( CacheException e )
			{
				LOG.severe( "Unable to create cache: " + e.getLocalizedMessage() );
			}
		}
		return iCache;
	}

	/**
	 * 
	 * @param aKey
	 * @return null if there is a problem.
	 */
	public static String getOriginalUrlForKey( Long aKey )
	{
		String originalUrl = null;
		try
		{
			originalUrl = (String) getCache().get( aKey );
		}
		catch( Exception e )
		{
			LOG.info( "Cache get error for key: " + aKey + " : " + e.getLocalizedMessage() );
		}
		return originalUrl;
	}

	public static void put( Long aKey, String aOriginalUrl )
	{
		try
		{
			getCache().put( aKey, aOriginalUrl );
		}
		catch( Exception e )
		{
			LOG.info( "Cache error for put: " + aKey + " " + aOriginalUrl + " " + e.getLocalizedMessage() );
		}
	}

}
