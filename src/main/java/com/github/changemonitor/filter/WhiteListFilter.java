package com.github.changemonitor.filter;

import java.util.concurrent.CopyOnWriteArrayList;

import com.github.changemonitor.parse.MybatisInvocation;

/**
 *
 * @author sclu
 * @date 2017年9月18日 下午2:59:13
 */
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

