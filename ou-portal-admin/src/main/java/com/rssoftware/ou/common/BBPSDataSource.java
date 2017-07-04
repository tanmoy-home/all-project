package com.rssoftware.ou.common;

public class BBPSDataSource extends org.apache.tomcat.jdbc.pool.DataSource{
	public BBPSDataSource() {
		super();
	}
	
	public BBPSDataSource(org.apache.tomcat.jdbc.pool.PoolConfiguration poolProperties){
		super(poolProperties);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof BBPSDataSource){
			if (getUrl().equals(((BBPSDataSource)obj).getUrl())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getUrl().hashCode();
	}
	
}
