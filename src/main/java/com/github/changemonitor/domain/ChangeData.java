package com.github.changemonitor.domain;

import java.util.List;


public class ChangeData {
	public List<ChangeObject> afterColumnList ;
    public List<ChangeObject> beforeColumnList;
    public String eventType;
    public String schemaName;
    public String tableName;
    
	public List<ChangeObject> getAfterColumnList() {
		return afterColumnList;
	}
	public void setAfterColumnList(List<ChangeObject> afterColumnList) {
		this.afterColumnList = afterColumnList;
	}
	public List<ChangeObject> getBeforeColumnList() {
		return beforeColumnList;
	}
	public void setBeforeColumnList(List<ChangeObject> beforeColumnList) {
		this.beforeColumnList = beforeColumnList;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}

