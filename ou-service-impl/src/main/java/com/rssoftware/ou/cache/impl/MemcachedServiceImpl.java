package com.rssoftware.ou.cache.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.rssoftware.ou.cache.CacheService;

//import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

//@Component
public class MemcachedServiceImpl implements CacheService {
	final MemcachedClient c;
	
	public MemcachedServiceImpl() throws IOException {
		c = new MemcachedClient(new InetSocketAddress("localhost", 11211));

	}
	
	//@Override
	public void writeToCache(String key, Object value, int expiry) {
		c.set(key, expiry, value);
	}

	//@Override
	public Object readFromCache(String key) {
		Object myObj = null;
		
		Future<Object> f = c.asyncGet(key);
		try {
			myObj = f.get(5, TimeUnit.SECONDS);
			return myObj;
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
			// Since we don't need this, go ahead and cancel the operation. This
			// is not strictly necessary, but it'll save some work on the
			// server.
			f.cancel(false);
			return null;
			// Do other timeout related stuff
		}
	}

}
