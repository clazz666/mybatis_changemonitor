package com.github.changemonitor.enumerate;
import java.util.*;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月20日 下午6:51:32
 * Class Description 
 */
public enum DBActionTypeEnum {
	UPDATE("UPDATE","UPDATE"),
	INSERT("INSERT","INSERT"),
	DELETE("DELETE","DELETE");
	private String key;
	private String value;
	private DBActionTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}

