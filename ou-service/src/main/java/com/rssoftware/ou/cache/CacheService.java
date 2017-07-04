package com.rssoftware.ou.cache;

public interface CacheService {
	void writeToCache(String key, Object value, int expiry);

	Object readFromCache(String key);
}
