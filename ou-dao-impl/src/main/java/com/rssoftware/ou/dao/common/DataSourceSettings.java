package com.rssoftware.ou.dao.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

public class DataSourceSettings {

	private List<Map<String, String>> clientDataSources = null;

	private List<String> clients = new ArrayList<String>();

	public List<String> getClients() {
		return clients;
	}

	public void setClients(List<String> clients) {
		this.clients = clients;
	}

	public List<Map<String, String>> clientDataSources() {
		if (clientDataSources == null) {
			clientDataSources = new ArrayList<Map<String, String>>();
			initialize();
		}
		return clientDataSources;
	}

	private void initialize() {
		for (String client : clients) {
			// extract client name
			String[] parts = client.split("\\|");
			// String clientName = parts[0];
			String url = parts[0];
			// client to datasource mapping
			// String dsName = url.substring(url.lastIndexOf("/") + 1);

			Map<String, String> datasourceDetails = new HashMap<>();
			datasourceDetails.put("url", url);
			datasourceDetails.put("username", parts[1]);
			datasourceDetails.put("password", new String(Base64.getDecoder().decode(parts[2])));
			datasourceDetails.put("driverClassName", parts[3]);
			if (parts.length > 4 && null != parts[4]) {
				datasourceDetails.put("maxActive", parts[4]);
				datasourceDetails.put("maxIdle", String.valueOf(Integer.parseInt(parts[4]) / 4));
				datasourceDetails.put("removeAbandonedTimeout", String.valueOf(Integer.parseInt(parts[4]) * 3));
			}
			if (parts.length > 5 && null != parts[5]) {
				datasourceDetails.put("testOnBorrow", parts[5]);
				datasourceDetails.put("removeAbandoned", parts[5]);
			}

			if (parts.length > 6 && null != parts[6]) {
				datasourceDetails.put("validationQuery", parts[6]);
			}

			clientDataSources.add(datasourceDetails);
		}
	}
}
