/*package com.rssoftware.test.HMAC;

import java.util.Date;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.rssoftware.ou.dao.test.TestConfiguration;
import com.rssoftware.ou.portal.common.OuRestTemplate;
import com.rssoftware.ou.security.RestUtil;

import in.co.rssoftware.bbps.schema.BillerCatagory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebIntegrationTest
public class HMACTest {

	@Test
	public void test() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		// headers.add("Content-Type", "application/json");
		String date = DateUtils.formatDate(new Date());
		System.out.println("Date: " + date);
		StringBuilder signature = new StringBuilder();
		signature.append("GET").append("\n").append(date).append("\n").append("/APIService/biller-category-list/OU05");

		String userName = "agent1";
		String auth = userName + ":" + RestUtil.calculateHMAC("password", signature.toString());
		headers.add("Date", date);
		headers.add("Authorization", auth);
		System.out.println("Authorization: " + auth);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		OuRestTemplate restTemplate = OuRestTemplate.createInstance();

		// "https://localhost:9090/APIService/biller-category-list/{tenantId}?agentId={agentId}",
		// "OU05", "AR10OXYG000001123458"
		ResponseEntity<BillerCatagory> responseEntity = restTemplate.exchange(
				"https://localhost:9090/APIService/biller-category-list/{tenantId}?agentId={agentId}", request,
				BillerCatagory.class, "OU05", "AR10OXYG000001123458");
		
		System.out.println("StatusCode----- "+responseEntity.getStatusCode());
	}

}
*/