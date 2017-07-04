package com.rssoftware.ou.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
public class BillerResponseParams {
	public static final String BASE_BILL_AMOUNT = "BASE_BILL_AMOUNT";
	
	public static class AmountOption{
		private Set<String> amountBreakupSet;

		public Set<String> getAmountBreakupSet() {
			if (amountBreakupSet == null){
				amountBreakupSet = new HashSet<String>();
			}
			
			return amountBreakupSet;
		}
		
		@JsonIgnore
		@XmlTransient
		public List<String> getAmountBreakups() {
			List<String> amountBreakups = new ArrayList<String>();
			amountBreakups.addAll(getAmountBreakupSet());
			return amountBreakups;
		}
		
		public void setAmountBreakups(List<String> amountBreakups) {
			getAmountBreakupSet().clear();
			if (amountBreakups != null){
				getAmountBreakupSet().addAll(amountBreakups);
			}
		}
		
		@Override
		public String toString() {
			return "AmountOption [amountBreakupSet=" + amountBreakupSet + "]";
		}
	}
	
	private List<ParamConfig> params;
	
	private List<AmountOption> amountOptions;

	public List<ParamConfig> getParams() {
		if (params == null){
			params = new ArrayList<ParamConfig>();
		}
		
		return params;
	}

	public List<AmountOption> getAmountOptions() {
		if (amountOptions == null){
			amountOptions = new ArrayList<BillerResponseParams.AmountOption>();
		}
		
		// hack to filter out blank entries
		for (int i=0;i<amountOptions.size();i++){
			if (amountOptions.get(i) == null || amountOptions.get(i).getAmountBreakupSet().isEmpty()){
				amountOptions.remove(i);
				i--;
			}
		}
		
		return amountOptions;
	}

	@Override
	public String toString() {
		return "BillerResponseParams [params=" + params + ", amountOptions="
				+ amountOptions + "]";
	}
}
