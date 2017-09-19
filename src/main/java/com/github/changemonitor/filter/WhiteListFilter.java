package com.github.changemonitor.filter;

import java.util.concurrent.CopyOnWriteArrayList;

import com.github.changemonitor.parse.MybatisInvocation;


public class WhiteListFilter implements Filter{
	@Override
	public Boolean validate(String tableName, MybatisInvocation mybatisInvocation) {
		CopyOnWriteArrayList<String> whiteList = mybatisInvocation.getWhiteCopyList();
		if(whiteList!=null && whiteList.size()>0){
			if(whiteList.contains(tableName)){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
}

