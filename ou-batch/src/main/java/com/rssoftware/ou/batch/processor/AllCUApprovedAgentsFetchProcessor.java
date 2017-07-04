package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.AgentDetails;

public class AllCUApprovedAgentsFetchProcessor  implements ItemProcessor<AgentDetails,AgentDetails>  {

	@Override
	public AgentDetails process(AgentDetails arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Read content for CU approved agents=====================");
		System.out.println("Agent attributes - agent id "+arg0.getAgentId());
		System.out.println("Agent attributes - agent name "+arg0.getAgentName());
		return arg0;
	}

	
}
