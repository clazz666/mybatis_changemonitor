package com.github.changemonitor.enumerate;

/**
 *
 * @author sclu
 * @date 2017年8月15日 下午6:32:52
 */
public enum DBType {
	MY_SQL("mysql","mysql"),
	DB2("db2","db2"),
	ORACLE("oracle","oracle");
	
	private DBType(String value, String description) {
		this.value = value;
		this.description = description;
	}
	private String value;
	private String description;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}

