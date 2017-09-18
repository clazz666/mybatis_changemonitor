package com.github.changemonitor.filter;

import com.github.changemonitor.parse.MybatisInvocation;

/**
 *
 * @author sclu
 * @date 2017年9月18日 下午2:59:43
 */
public interface Filter {
	Boolean validate(String tableName,MybatisInvocation mybatisInvocation);
}

