package com.rssoftware.ou.common;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = 6085002351075416019L;

	private Long categoryId;
	private String categoryName;
	private String entityType;

	public Category() {
		super();
	}

	public Category(Long categoryId, String categoryName, String entityType) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.entityType = entityType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName="
				+ categoryName + ", entityType=" + entityType + "]";
	}

}