package com.rssoftware.ou.batch.common;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.rssoftware.ou.batch.to.AgentDetails;

public class CUApprovedAgentsFieldSetMapper implements FieldSetMapper<AgentDetails>{

	@Override
	public AgentDetails mapFieldSet(FieldSet arg0) throws BindException {
		for (String val : arg0.getValues()){
			System.out.println("printing line value:"+val);
		}
		AgentDetails agentDetails = new AgentDetails();
		agentDetails.setAgentId(arg0.readString("agentId"));
		agentDetails.setAgentName(arg0.readString("agentName"));
		agentDetails.setAgentAliasName(arg0.readString("agentAliasName"));
		agentDetails.setAgentMobileNo(arg0.readString("agentMobileNo"));
		agentDetails.setAgentShopName(arg0.readString("agentShopName"));
		agentDetails.setAgentBusnsType(arg0.readString("agentBusnsType"));
		agentDetails.setAgentPaymentModes(arg0.readString("agentPaymentModes"));
		
		agentDetails.setDummyAgent(arg0.readBoolean("dummyAgent"));
		agentDetails.setAgentLinkedAgentInst(arg0.readString("agentLinkedAgentInst"));
		agentDetails.setAgentPaymentChannels(arg0.readString("agentPaymentChannels"));
		agentDetails.setAgentRegisteredAdrline(arg0.readString("agentRegisteredAdrline"));
		agentDetails.setAgentRegisteredState(arg0.readString("agentRegisteredState"));
		agentDetails.setAgentRegisteredCity(arg0.readString("agentRegisteredCity"));
		agentDetails.setAgentRegisteredPinCode(arg0.readString("agentRegisteredPinCode"));
		agentDetails.setAgentEffctvFrom(arg0.readString("agentEffctvFrom"));
		agentDetails.setAgentEffctvTo(arg0.readString("agentEffctvTo"));
		agentDetails.setAgentType(arg0.readString("agentType"));
		agentDetails.setAgentGeoCode(arg0.readString("agentGeoCode"));
		agentDetails.setAgentCUID(arg0.readString("agentCUID"));
		return agentDetails;
	}

}
