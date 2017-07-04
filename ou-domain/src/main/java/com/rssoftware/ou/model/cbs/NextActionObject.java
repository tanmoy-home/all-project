package com.rssoftware.ou.model.cbs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public final class NextActionObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7186319949986436954L;

	public enum NextActionObjectType implements Serializable{
		PAYER, 
		PAYEE, 
		PAYEE_OWN,
		PAYER_OWN,
		WHOLE_TRANSACTION // not sure whether this will be used anywhere
	};
	
	private NextActionObjectType type;
	private int payeeIndex;
	private String payeeSequence;
	private boolean reversal = false;
	private final Set<String> targetSystemOptions = new HashSet<>(1);

	public NextActionObject() {}
	
	// IMPORTANT : IF YOU ADD ANY ATTRIBUTE PLEASE INCLUDE IT INTO THE COPY CONSTRUCTOR
	public NextActionObject(NextActionObject nao) {
		if (nao != null){
			type = nao.type;
			payeeIndex = nao.payeeIndex;
			payeeSequence = nao.payeeSequence;
			reversal = nao.reversal;
			targetSystemOptions.addAll(nao.targetSystemOptions);
		}
	}
	
	public NextActionObjectType getType() {
		return type;
	}

	public void setType(NextActionObjectType type) {
		this.type = type;
	}

	public int getPayeeIndex() {
		return payeeIndex;
	}

	public void setPayeeIndex(int payeeIndex) {
		this.payeeIndex = payeeIndex;
	}

	public String getPayeeSequence() {
		return payeeSequence;
	}

	public void setPayeeSequence(String payeeSequence) {
		this.payeeSequence = payeeSequence;
	}

	public Set<String> getTargetSystemOptions() {
		return targetSystemOptions;
	}

	public boolean isReversal() {
		return reversal;
	}

	public void setReversal(boolean reversal) {
		this.reversal = reversal;
	}

	@Override
	public String toString() {
		return "NextActionObject [type=" + type + ", payeeIndex=" + payeeIndex
				+ ", payeeSequence=" + payeeSequence + ", reversal=" + reversal
				+ ", targetSystemOptions=" + targetSystemOptions + "]";
	}
}
