package com.github.changemonitor.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.changemonitor.parse.MybatisInvocation;

/**
 *
 * @author sclu
 * @date 2017年9月18日 下午3:04:29
 */
public class FilterContext {
	private static List<Filter> filterList = new ArrayList<>();
	static{
		filterList.add(new WhiteListFilter());
	}
	public static Boolean validate(String tableName, MybatisInvocation mybatisInvocation){
		Boolean validateResultBoolean;
		for(Filter filter : filterList){
			validateResultBoolean = filter.validate(tableName, mybatisInvocation);
			if(!validateResultBoolean){
				return false;
			}
		}
		return true;
	}
}

