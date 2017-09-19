package com.github.changemonitor.filter;

import com.github.changemonitor.parse.MybatisInvocation;


public interface Filter {
	Boolean validate(String tableName,MybatisInvocation mybatisInvocation);
}

