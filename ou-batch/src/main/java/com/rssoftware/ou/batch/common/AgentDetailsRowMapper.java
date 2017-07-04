package com.rssoftware.ou.batch.common;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.AgentDetails;
import com.rssoftware.ou.batch.to.TxnReport;

public class AgentDetailsRowMapper implements RowMapper<AgentDetails> {

	
//	SELECT agent_id, agent_busns_type, agent_payment_modes, agent_linked_agent_inst, 
//    agent_name, agent_alias_name, agent_geo_code, agent_shop_name, 
//    agent_mobile_no, agent_registered_adrline, agent_registered_city, 
//    agent_registered_pin_code, agent_registered_state, agent_registered_country, 
//    agent_effctv_from, agent_effctv_to, entity_status, crtn_ts, crtn_user_id, 
//    updt_ts, updt_user_id, agent_payment_channels, agent_cu_id, agent_dummy, 
//    agent_bank_account, agent_scheme_id, agent_type, is_upload
//FROM agent_details where entity_status='PENDING_ACTIVATION'
	@Override
	public AgentDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		AgentDetails agentDetails = new AgentDetails();
		agentDetails.setAgentId(rs.getString("agent_id"));
		agentDetails.setAgentName(rs.getString("agent_name"));
		agentDetails.setAgentAliasName(rs.getString("agent_alias_name"));
		agentDetails.setAgentMobileNo(rs.getString("agent_mobile_no"));
		agentDetails.setAgentShopName(rs.getString("agent_shop_name"));
		agentDetails.setAgentBusnsType(rs.getString("agent_busns_type"));
		agentDetails.setAgentPaymentModes(rs.getString("agent_payment_modes") == null ? null : rs.getString("agent_payment_modes").replaceAll(",", "\",\""));
		
		agentDetails.setDummyAgent(rs.getBoolean("agent_dummy"));
		agentDetails.setAgentLinkedAgentInst(rs.getString("agent_linked_agent_inst"));
		agentDetails.setAgentPaymentChannels(rs.getString("agent_payment_channels") == null ? null : rs.getString("agent_payment_channels").replaceAll(",", "\",\""));
		agentDetails.setAgentRegisteredAdrline(rs.getString("agent_registered_adrline").replaceAll(",", "\",\""));
		agentDetails.setAgentRegisteredState(rs.getString("agent_registered_state"));
		agentDetails.setAgentRegisteredCity(rs.getString("agent_registered_city"));
		agentDetails.setAgentRegisteredPinCode(rs.getString("agent_registered_pin_code"));
		agentDetails.setAgentEffctvFrom(rs.getString("agent_effctv_from"));
		agentDetails.setAgentEffctvTo(rs.getString("agent_effctv_to"));
		agentDetails.setAgentType(rs.getString("agent_type"));
		agentDetails.setAgentGeoCode(rs.getString("agent_geo_code"));
		
		return agentDetails;
	}

}