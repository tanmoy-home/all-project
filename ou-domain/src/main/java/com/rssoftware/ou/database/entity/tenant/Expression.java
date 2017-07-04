package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the EXPRESSIONS database table.
 * 
 */
@Entity
@Table(name="EXPRESSIONS")
@NamedQuery(name="Expression.findAll", query="SELECT e FROM Expression e")
public class Expression implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXPRESSION_NAME")
	private String expressionName;

	@Column(name="EXPRESSION_VALUE")
	private String expressionValue;

	public Expression() {
	}

	public String getExpressionName() {
		return this.expressionName;
	}

	public void setExpressionName(String expressionName) {
		this.expressionName = expressionName;
	}

	public String getExpressionValue() {
		return this.expressionValue;
	}

	public void setExpressionValue(String expressionValue) {
		this.expressionValue = expressionValue;
	}

}